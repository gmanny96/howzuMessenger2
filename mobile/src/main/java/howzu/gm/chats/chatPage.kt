package howzu.gm.chats

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import java.io.File

import howzu.gm.chats.BitmapWorkerTask.getBitmapWorkerTask

class chatPage : Fragment(), chatPageAdapter.Clicklistener {

    internal var toolbar: Toolbar
    internal var recyclerView: RecyclerView
    internal var editText: AppCompatEditText
    internal var username: String? = null
    internal var send: AppCompatImageButton
    internal var button1: AppCompatImageButton? = null
    internal var button2: AppCompatImageButton? = null
    internal var adapter: chatPageAdapter
    internal var view: View
    internal var name: String? = null
    internal var tabLayout: TabLayout
    internal var pic: Int = 0
    internal var layout: RelativeLayout
    internal var callBack: workToDo

    private val myReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val r = intent.getIntExtra("r", 0)
            if (r == 0) {
                //setLists();
            } else {
                if (intent.getStringExtra("from") == username)
                    adddatarecieved(intent.getStringExtra("msg"), java.lang.Long.valueOf("time"))
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            callBack = activity as workToDo?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnHeadlineSelectedListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.chatpage, container, false)
        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(myReceiver, IntentFilter("Chatmsgs_updated"))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val preferences = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        username = preferences.getString("Page", "")

        loaderManager.initLoader(1, null, this).forceLoad()

        //getActivity().startService(new Intent(getActivity(), getMsgs.class).putExtra("username", username));

        toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        editText = view.findViewById<View>(R.id.message) as AppCompatEditText
        recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        send = view.findViewById<View>(R.id.send) as AppCompatImageButton
        tabLayout = view.findViewById<View>(R.id.tab) as TabLayout
        emoji = view.findViewById<View>(R.id.emoji) as AppCompatImageButton
        layout = view.findViewById<View>(R.id.layout) as RelativeLayout

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val arg = arrayOf<String>(username)
        val projection = arrayOf<String>(Dbhelper.pic)
        val uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data")
        val cursor = context!!.contentResolver.query(uri, projection, Dbhelper.phonenumber + "= ? ", arg, null)

        if (cursor != null && cursor.moveToNext())
            pic = cursor.getInt(cursor.getColumnIndex(Dbhelper.pic))
        if (cursor != null && !cursor.isClosed)
            cursor.close()

        if (pic == 0) {
            toolbar.setNavigationIcon(R.drawable.back)
        } else {
            val p = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, resources.displayMetrics).toInt()
            Picasso.with(view.context).load(loadImageFromStorage()).resize(p, p).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    toolbar.logo = BitmapDrawable(bitmap)
                }

                override fun onBitmapFailed(errorDrawable: Drawable) {
                    //toolbar.setNavigationIcon(R.drawable.back);
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                }
            })
        }

        toolbar.navigationIcon!!.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        callBack.changeBarColor(1)

        toolbar.setNavigationOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
            callBack.changeBarColor(0)
        }

        name = chatU.getName(view.context, username)

        if (name == null)
            toolbar.title = username
        else
            toolbar.title = name

//images videos together for camera as well as storage
/*tabLayout.addTab(tabLayout.newTab().setText("Image"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera"));
        tabLayout.addTab(tabLayout.newTab().setText("Music"));
        tabLayout.addTab(tabLayout.newTab().setText("Location"));
        tabLayout.addTab(tabLayout.newTab().setText("File"));
        tabLayout.addTab(tabLayout.newTab().setText("contacts"));

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.transparent));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Bundle b = new Bundle();
                        b.putBoolean("single", false);
                        image_chooser ic = new image_chooser();
                        ic.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, ic, "image_chooser").addToBackStack(null).commit();
                        break;
                    case 1:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 0);
                        break;
                    case 2:
                        startActivityForResult(Intent.createChooser(new Intent().setType("video/*").setAction(Intent.ACTION_GET_CONTENT), "Select Video"), 1);
                        break;
                    case 3:
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new get_music(), "music").addToBackStack(null).commit();
                        break;
                    case 4:
                        LocationService gps = new LocationService(getContext());
                        if (gps.canGetLocation()) {
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                        } else {
                            gps.showSettingsAlert();
                        }
                        break;
                    case 5:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        //emoji.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.click), PorterDuff.Mode.MULTIPLY);
        //send.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.click), PorterDuff.Mode.MULTIPLY);

        toolbar.setOnClickListener(object:View.OnClickListener {
public override fun onClick(v:View) {
getActivity()!!.getSupportFragmentManager().beginTransaction().add(R.id.activity, profilePage(), "profilepage").addToBackStack(null).commit()
}
})

emoji.setOnClickListener(object:View.OnClickListener {
public override fun onClick(v:View) {
callBack.openEmoji()
}
})

setHasOptionsMenu(true)

 //setLists();
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))

editText.requestFocus()

send.setOnClickListener(object:View.OnClickListener {
public override fun onClick(v:View) {
if (editText.getText().toString() != "")
{

val message = editText.getText().toString()
val tsLong = System.currentTimeMillis() / 1000
val to = username
val na = name

 //my number/reference/to/time for username
                    val Id = ""

if (meLists.getInstance().getConnection() != null)
{
Log.e("ff", "message send service call")
getActivity()!!.startService(Intent(view.getContext(), newSendMessageService::class.java).putExtra("Message", editText.getText().toString()).putExtra("Time_written", tsLong).putExtra("To", username).putExtra("Id", Id))
}

val imuri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/insert")
val values = ContentValues()
values.put(Dbhelper.phonenumber, to)
values.put(Dbhelper.time, tsLong)
values.put(Dbhelper.message, message)
values.put(Dbhelper.mode, 0)
values.put(Dbhelper.msgId, Id)
getActivity()!!.getContentResolver().insert(imuri, values)

val uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data")
val args = arrayOf<String>(to)
val proj = arrayOf<String>(Dbhelper.phonenumber)
val cursor = getActivity()!!.getContentResolver().query(uri, proj, Dbhelper.phonenumber + " = ? ", args, null)
if (cursor == null || !cursor!!.moveToNext())
{
val iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert")
val valuesm = ContentValues()
valuesm.put(Dbhelper.phonenumber, to)
valuesm.put(Dbhelper.displayname, na)
valuesm.put(Dbhelper.chat, 1)
 //valuesm.put(Dbhelper.message, message);
                        valuesm.put(Dbhelper.time, tsLong)
getActivity()!!.getContentResolver().insert(iuri, valuesm)
Log.e("ff", "insertchat")
}
else
{
val uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update")
val valuesc = ContentValues()
valuesc.put(Dbhelper.chat, 1)
 //valuesc.put(Dbhelper.message, message);
                        valuesc.put(Dbhelper.time, tsLong)
getActivity()!!.getContentResolver().update(uuri, valuesc, null, args)
Log.e("ff", "updatechat")
}
if (cursor != null && !cursor!!.isClosed())
{
cursor!!.close()
}

adddata(message, Id, 0, tsLong!!)

editText.setText(null)
 //recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    callBack.chatUpdated(true, username)
 //getActivity().sendBroadcast(new Intent("Chats_updated").putExtra("msg", true).putExtra("username", username));
                }
}
})
}

private fun adddata(msg:String, id:String, mode:Int, ff:Long) {
adapter.getExpanded()
addDate(ff)
val data = chatPageData()
data.id = id
data.message = msg
data.mode = mode
data.tordtext = (ff).toString()
adapter.adddata(data)

addTime(8, ff)
}

internal fun adddatarecieved(msg:String, time:Long?) {
adapter.getExpanded()
addDate(time!!)
val data = chatPageData()
data.message = msg
data.tord = chatU.getTime(getContext(), time!!)
data.tordtext = (time).toString()
data.mode = 4
adapter.adddata(data)

addTime(9, time)
}

private fun addDate(time:Long) {
val timetext = chatU.getDateTime(view.getContext(), time, 0)
if (adapter.getItemCount() > 0)
{
if (timetext != chatU.getDateTime(view.getContext(), java.lang.Long.valueOf(adapter.gettime()), 0))
{
val current3 = chatPageData()
current3.tordtext = (time).toString()
current3.tord = timetext
current3.mode = 7
adapter.adddata(current3)
}
}
else
{
val current3 = chatPageData()
current3.tord = timetext
current3.mode = 7
adapter.adddata(current3)
}
}

private fun addTime(type:Int, time:Long?) {
val current3 = chatPageData()
current3.tord = chatU.getTime(view.getContext(), time!!)
current3.mode = type
current3.tordtext = (time).toString()
current3.expanded = false
adapter.adddata(current3)
}

public override fun onDestroyView() {
super.onDestroyView()
LocalBroadcastManager.getInstance(getActivity()!!.getApplicationContext()).unregisterReceiver(myReceiver)
 //Lists.getInstance().setMsgList(null);
    }

private fun setLists(data:List<chatPageData>) {
adapter = chatPageAdapter(view.getContext(), data, this)
recyclerView.setAdapter(adapter)
if (adapter.getItemCount() != 0)
recyclerView.scrollToPosition(adapter.getItemCount() - 1)
}

public override fun onCreateOptionsMenu(menu:Menu?, inflater:MenuInflater?) {
menu!!.clear()
super.onCreateOptionsMenu(menu, inflater)
inflater!!.inflate(R.menu.chats, menu)
val searchView = menu!!.findItem(R.id.search).getActionView() as SearchView

searchView.setIconified(false)

val v = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_plate)
v.setBackgroundColor(Color.TRANSPARENT)

val txtSearch = (searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText)
txtSearch.setHint("Search")
txtSearch.setTextColor(Color.WHITE)

}

public override fun onOptionsItemSelected(item:MenuItem?):Boolean {
when (item!!.getItemId()) {
R.id.call -> {
val callIntent = Intent(Intent.ACTION_CALL)
callIntent.setData(Uri.parse("tel:" + username!!))
startActivity(callIntent)
}
R.id.message -> {
val smsUri = Uri.parse("tel:" + username!!)
val intent = Intent(Intent.ACTION_VIEW, smsUri)
intent.putExtra("address", username)
intent.setType("vnd.android-dir/mms-sms")
startActivity(intent)
}
}
return true
}

 fun searchText(text:String) {
for (i in adapter.getItemCount() - 1 downTo 0)
{
if (adapter.getItemViewType(i) != 1)
{
val holder = recyclerView.findViewHolderForAdapterPosition(i)
val view = holder.itemView
val tv:AppCompatTextView
if (adapter.getItemViewType(i) == 0)
tv = view.findViewById<View>(R.id.message) as AppCompatTextView
else
tv = view.findViewById<View>(R.id.text) as AppCompatTextView
if (tv.getText().toString() == "%" + text + "%")
recyclerView.scrollToPosition(i)
}
}
}

public override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
if (requestCode == 0 && resultCode == getActivity()!!.RESULT_OK)
{
 //data.getExtras().get("data");
        }
}

private fun loadImageFromStorage():File {
return File(Environment.getExternalStorageDirectory() + "/Chats/Media/My Profile Images", "+919560083681")
}

public override fun rowClicked(type:Int, pos:Int, timetext:String) {
if (adapter.getItemCount() > pos + 1)
{
if (adapter.getItemViewType(pos + 1) == 3 || adapter.getItemViewType(pos + 1) == 4)
{
adapter.removerow(pos + 1)
}
else
{
val time = chatU.getTime(view.getContext(), java.lang.Long.valueOf(timetext)!!)
val current3 = chatPageData()
current3.tord = time
current3.mode = type
current3.expanded = true
current3.tordtext = timetext
adapter.addtimerow(current3, pos)
}
}
else
{
val time = chatU.getTime(view.getContext(), java.lang.Long.valueOf(timetext)!!)
val current3 = chatPageData()
current3.tord = time
current3.mode = type
current3.expanded = true
current3.tordtext = timetext
adapter.addtimerow(current3, pos)
}
if (adapter.getItemCount() - 1 == pos + 1)
{
recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount())
}
}
*/
    }
}