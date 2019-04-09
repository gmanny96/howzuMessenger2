package howzu.gm.chats

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Chats : Fragment(), LoaderManager.LoaderCallbacks<List<chatsData>>, chatsAdapter.Clicklistener {

    internal var callBack: workToDo
    internal var text: AppCompatTextView
    internal var list: RecyclerView
    internal var adapter: chatsAdapter
    internal var preferences: SharedPreferences
    internal var view: View
    internal var number: String? = null
    internal var name: String? = null
    internal var pos: Int = 0
    internal var contacts: FloatingActionButton
    internal var search: FloatingActionButton

    private val myReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getBooleanExtra("msg", false)) {
                var found: Boolean? = false
                for (i in 0 until adapter.itemCount) {
                    if (adapter.findNumber(i) == intent.getStringExtra("username")) {
                        adapter.updaterow(i)
                        found = true
                    }

                }
                if ((!found)!!) {
                    adapter.addrow(addrow(intent.getStringExtra("username")))
                }

            } else {
                //setlist();
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.tab_new_list, container, false)
        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(myReceiver, IntentFilter("Chats_updated"))
        //getActivity().startService(new Intent(getContext(), serviceGetChats.class));
        startLoader()
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            callBack = activity as workToDo?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnHeadlineSelectedListener")
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        list = view.findViewById<View>(R.id.list) as RecyclerView
        text = view.findViewById<View>(R.id.text) as AppCompatTextView
        contacts = view.findViewById<View>(R.id.add) as FloatingActionButton
        search = view.findViewById<View>(R.id.search) as FloatingActionButton

        search.setOnClickListener { activity!!.supportFragmentManager.beginTransaction().replace(R.id.activity, newExplore(), "search").addToBackStack(null).commit() }

        contacts.setOnClickListener { activity!!.supportFragmentManager.beginTransaction().replace(R.id.activity, newContacts(), "contacts").addToBackStack(null).commit() }

        list.layoutManager = LinearLayoutManager(activity)

        if (view.context.NOTIFICATION_SERVICE != null) {
            val nMgr = view.context.getSystemService(view.context.NOTIFICATION_SERVICE) as NotificationManager
            nMgr.cancelAll()
        }

        /*if(savedInstanceState!=null){
            this.number = savedInstanceState.getString("number");
            this.name = savedInstanceState.getString("name");
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(!getActivity().isFinishing() && number!=null)
                    getActivity().openContextMenu(list);
            }
        });*/
    }

    private fun startLoader() {
        loaderManager.initLoader(1, null, this).forceLoad()
    }

    fun updateChat(number: String) {
        var found: Boolean? = false
        for (i in 0 until adapter.itemCount) {
            if (adapter.findNumber(i) != null && adapter.findNumber(i) == number) {
                //adapter.checkToday();
                adapter.updaterow(i)
                found = true
            }
        }
        if ((!found)!!) {
            //adapter.checkToday();
            adapter.addrow(addrow(number))
        }
    }

    fun addrow(user: String): chatsData {
        val data = chatsData()
        data.number = user
        data.type = 1
        return data
    }

    fun updaterow(msg: String, time: String): chatsData {
        val data = chatsData()
        data.message = msg
        data.time = chatU.getDateTime(view.context, java.lang.Long.getLong(time), 1)
        return data
    }

    fun setlist(data: List<chatsData>) {
        adapter = chatsAdapter(context, data, callBack)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)
        empty()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            LocalBroadcastManager.getInstance(context!!.applicationContext).unregisterReceiver(myReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        //getActivity().stopService(new Intent(getContext(), serviceGetChats.class));
        Lists.getInstance().chatList = null
    }

    override fun rowClicked(number: String) {
        val editor = preferences.edit()
        editor.putString("Page", number).apply()

        callBack.rowClick(number)

        //if(getActivity().getSupportFragmentManager().findFragmentByTag("chatpage") == null)
        //    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new chatPage(), "chatpage").addToBackStack(null).commit();
    }

    override fun rowContext(pos: Int, num: String, name: String) {

        //this.pos = pos;
        val args = Bundle()
        args.putString("name", name)
        args.putString("num", num)
        val f = chatsOptions()
        f.setArguments(args)
        f.show(activity!!.fragmentManager, "options")
        //number = num;
        //this.name = name;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putString("number", number);
        //outState.putString("name", name);
    }

    /*@Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(menu[2])) {
            Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
            String[] args = {number};
            ContentValues values = new ContentValues();
            values.put(Dbhelper.chat, 0);
            getActivity().getContentResolver().update(uuri, values, null, args);
            Uri imuri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/delete");
            getActivity().getContentResolver().delete(imuri, null, args);
            adapter.delete(pos);
        }
        else if(item.getTitle().equals(menu[0])) {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
            startActivity(intent);
        }
        else{
            Intent intent1 = new Intent(Intent.ACTION_INSERT_OR_EDIT);
            intent1.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent1.putExtra(ContactsContract.Intents.Insert.PHONE, number);
            startActivity(intent1);
        }
        return true;
    }*/

    fun empty() {
        if (adapter.itemCount > 0)
            text.visibility = View.GONE
        else
            text.visibility = View.VISIBLE
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<chatsData>> {
        Log.d("ff", "chatsdatafetch")
        return chatsDataFetch(activity)
    }

    override fun onLoadFinished(loader: Loader<List<chatsData>>, data: List<chatsData>) {
        Log.d("ff", "chatsdatafetchkhm")
        setlist(data)
    }

    override fun onLoaderReset(loader: Loader<List<chatsData>>) {

    }
}