package howzu.gm.chats

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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

class contactsAdapter(internal var context: Context, data: MutableList<contactsData>, private val clicklistener: Clicklistener?, internal var callBack: workToDo?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    internal var data: MutableList<contactsData>? = emptyList<contactsData>()

    init {
        Log.e("adapter me", "call gya")
        inflater = LayoutInflater.from(context)
        this.data = data
    }

    override fun getItemViewType(position: Int): Int {
        val current = data!![position]
        return if (current.type == 1)
            0
        else if (current.type == 0)
            1
        else
            2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1)
            contact(inflater.inflate(R.layout.contact_row, parent, false))
        else if (viewType == 0)
            last(inflater.inflate(R.layout.toolbar_row, parent, false))
        else
            options(inflater.inflate(R.layout.settings_row, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, arg1: Int) {
        val current = data!![arg1]
        if (holder is contact) {

            Log.e("adapter me", arg1.toString())

            Log.e("adapter me", current.number)
            val name = chatU.getName(context, current.number)
            holder.name.text = name
            val p = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.resources.displayMetrics).toInt()

            if (current.pic == 0) {
                //Log.e("adapter me", name);
                //viewHolder.image.setText(String.valueOf(name.charAt(0)));
                holder.image.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.contact_circle))
                holder.image.background.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.MULTIPLY)
            } else {
                Picasso.with(context).load(loadImageFromStorage(current.number)).resize(p, p).transform(getCircleBitmap()).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        holder.image.setBackgroundDrawable(BitmapDrawable(context.resources, bitmap))
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable) {
                        holder.image.setText(name!![0].toInt())
                        holder.image.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.on_m))
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                    }
                })
            }
        } else if (holder is options) {
            Log.e("option ", arg1.toString())
            holder.text.text = current.number
            Log.e(current.number, current.number)
        } else {

        }
    }

    fun adddata(current: contactsData) {
        data!!.add(current)
        notifyItemInserted(0)
    }

    override fun getItemCount(): Int {
        return if (data != null) {
            data!!.size
        } else {
            0
        }
    }

    internal inner class contact(arg0: View) : RecyclerView.ViewHolder(arg0) {

        var name: AppCompatTextView
        var image: AppCompatTextView
        var layout: RelativeLayout

        init {
            layout = arg0.findViewById<View>(R.id.layout) as RelativeLayout
            name = arg0.findViewById<View>(R.id.name) as AppCompatTextView
            image = arg0.findViewById<View>(R.id.image) as AppCompatTextView

            layout.setOnClickListener {
                clicklistener?.rowClicked(data!![adapterPosition].number)
            }
        }
    }

    internal inner class options(arg0: View) : RecyclerView.ViewHolder(arg0) {

        var text: AppCompatTextView
        var layout: RelativeLayout

        init {
            layout = arg0.findViewById<View>(R.id.layout) as RelativeLayout
            text = arg0.findViewById<View>(R.id.text) as AppCompatTextView

            layout.setOnClickListener {
                if (callBack != null)
                    callBack!!.optionClicked("rc")
            }
        }
    }

    internal inner class last(arg0: View) : RecyclerView.ViewHolder(arg0) {

        var text: AppCompatTextView

        init {
            text = arg0.findViewById<View>(R.id.text) as AppCompatTextView
        }
    }

    interface Clicklistener {
        fun rowClicked(number: String)
    }

    private fun loadImageFromStorage(number: String): File {
        return File(Environment.getExternalStorageDirectory().toString() + "/Chats/Media/Profile Images", number)
    }
}
