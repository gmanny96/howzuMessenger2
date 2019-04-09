package howzu.gm.chats

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import java.io.File
import java.util.Collections

internal class chatsAdapter(var context: Context, data: MutableList<chatsData>, private val callback: workToDo) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    var data: MutableList<chatsData>? = emptyList<chatsData>()
    var current: chatsData? = null
    private val clicklistener: Clicklistener

    init {
        this.clicklistener = clicklistener
        inflater = LayoutInflater.from(context)
        this.data = data
    }

    override fun getItemViewType(position: Int): Int {
        //current = data.get(position);
        return if (position == 0)
            0
        else
            1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1)
            chat(inflater.inflate(R.layout.new_chats_row, parent, false))
        else
            date(inflater.inflate(R.layout.sub_header, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var current: chatsData? = null


        Log.e("ff", "more than 0")

        if (position > 0) {

            current = data!![position - 1]

            Log.e("ff", "more than 0")
            val cholder = holder as chat

            val name = chatU.getName(context, current.number)
            cholder.name.text = name

            /*String[] arg = {current.number};
            String[] projection = {Dbhelper.message, Dbhelper.time, Dbhelper.mode};
            Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg");
            Cursor cursor = context.getContentResolver().query(uri, projection, Dbhelper.phonenumber + "= ? ", arg, Dbhelper.time + " DESC limit 1");

            if (cursor != null && cursor.moveToNext()) {
                cholder.message.setText(cursor.getString(cursor.getColumnIndex(Dbhelper.message)));
                cholder.timetext.setText(chatU.getDateTime(context, Long.valueOf(cursor.getString(cursor.getColumnIndex(Dbhelper.time))), 1));
            }

            if (cursor != null) {
                cursor.close();
            }
*/
            val p = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, context.resources.displayMetrics).toInt()

            cholder.type.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.on_m))
            //cholder.type.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.MULTIPLY);

            if (current.pic == 0) {
                cholder.image.text = name!![0].toString()
                cholder.image.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.contact_circle))
                cholder.image.background.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.MULTIPLY)
            } else {
                Picasso.with(context).load(loadImageFromStorage(current.number)).resize(p, p).transform(getCircleBitmap()).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        cholder.image.setBackgroundDrawable(BitmapDrawable(bitmap))
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable) {
                        cholder.image.setText(name!![0].toInt())
                        cholder.image.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.on_m))
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                    }
                })
            }
        } else {
            val dholder = holder as date
            dholder.date.text = "Recent"
        }

    }

    override fun getItemCount(): Int {
        return if (data != null) {
            data!!.size + 1
        } else {
            0
        }
    }

    inner class chat(view: View) : RecyclerView.ViewHolder(view) {

        internal var name: AppCompatTextView
        internal var image: AppCompatTextView
        internal var type: AppCompatTextView //message, timetext,
        internal var layout: RelativeLayout

        init {
            layout = view.findViewById<View>(R.id.layout) as RelativeLayout
            name = view.findViewById<View>(R.id.name) as AppCompatTextView
            //message = (AppCompatTextView) view.findViewById(R.id.message);
            image = view.findViewById<View>(R.id.image) as AppCompatTextView
            type = view.findViewById<View>(R.id.type) as AppCompatTextView

            //timetext = (AppCompatTextView) view.findViewById(R.id.time);
            layout.setOnClickListener {
                callback.rowClick(data!![adapterPosition - 1].number)
                //if (clicklistener != null)
                //    clicklistener.rowClicked(data.get(getAdapterPosition()).number);
            }
            layout.setOnLongClickListener {
                callback.openContextMenu()
                //callback.rowLongPress(data.get(getAdapterPosition()-1).number, name.getText().toString());
                //if (clicklistener != null) {
                //    clicklistener.rowContext(getAdapterPosition(), data.get(getAdapterPosition()).number, name.getText().toString());
                //}
                false
            }
        }
    }

    private inner class date internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        internal var date: AppCompatTextView
        internal var layout: RelativeLayout? = null

        init {
            //layout = (RelativeLayout) view.findViewById(R.id.layout);
            date = view.findViewById<View>(R.id.text) as AppCompatTextView
        }
    }

    fun delete(position: Int) {
        data!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun checkToday() {
        if (data!![0].time !== "Today") {
            addToday()
        }
    }

    private fun addToday() {
        val current1 = chatsData()
        current1.type = 0
        current1.time = "Today"
        data!!.add(0, current1)
        notifyItemInserted(0)
    }

    fun addrow(`object`: chatsData) {
        data!!.add(1, `object`)
        notifyItemInserted(1)
    }

    fun updaterow(position: Int) {
        notifyItemChanged(position)
        notifyItemMoved(position, 1)
    }

    fun findNumber(position: Int): String? {
        //if(data.get(position).type == 1)
        return data!![position].number
        //else
        //  return null;
    }

    interface Clicklistener {
        fun rowClicked(number: String)
        fun rowContext(pos: Int, number: String, name: String)
    }

    private fun loadImageFromStorage(num: String?): File {
        return File(Environment.getExternalStorageDirectory().toString() + "/Chats/Media/Profile Images", num!!)
    }

}
