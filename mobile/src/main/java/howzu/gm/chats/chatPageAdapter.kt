package howzu.gm.chats

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import java.util.Collections

internal class chatPageAdapter(var context: Context, data: MutableList<chatPageData>, var clicklistener: Clicklistener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater
    var data: MutableList<chatPageData>? = emptyList<chatPageData>()
    var current: chatPageData

    init {
        inflater = LayoutInflater.from(context)
        this.data = data
    }

    override fun getItemViewType(position: Int): Int {
        current = data!![position]
        return if (current.mode == 4 || current.mode == 5 || current.mode == 6) {
            0
        } else if (current.mode == 7) {
            1
        }/* else if (current.mode == 8) {
            return 3;
        } else if (current.mode == 9) {
            return 4;
        }*/
        else {
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> return rec(inflater.inflate(R.layout.chatmsg_rec, parent, false))
            1 -> return header(inflater.inflate(R.layout.toolbar_row, parent, false))
        /*case 3:
                return new sendtime(inflater.inflate(R.layout.msg_send_time, parent, false));
            case 4:
                return new rectime(inflater.inflate(R.layout.msg_rec_time, parent, false));
            */else -> return new_send(inflater.inflate(R.layout.test_msg_layout, parent, false))
        }//return new send(inflater.inflate(R.layout.chatmsg_send, parent, false));
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        current = data!![position]
        if (holder is rec) {
            holder.message.text = current.message
            holder.message.background.setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.MULTIPLY)
            /*} else if (holder instanceof send) {
            send hholder = (send) holder;
            hholder.message.setText(current.message);
            hholder.message.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.blue500), PorterDuff.Mode.MULTIPLY);
        } else if(holder instanceof sendtime){
            sendtime stholder = (sendtime) holder;
            stholder.text.setText(current.tord);
        } else if(holder instanceof rectime){
            rectime rtholder = (rectime) holder;
            rtholder.text.setText(current.tord);
        */
        } else if (holder is new_send) {
            holder.message.text = current.message
            holder.time.text = current.tord
            holder.message.background.setColorFilter(ContextCompat.getColor(context, R.color.blue500), PorterDuff.Mode.MULTIPLY)
        } else {
            val sholder = holder as header
            sholder.text.text = current.tord
        }
    }

    fun adddatarow(current: chatPageData, position: Int) {
        data!!.add(position, current)
        notifyItemInserted(position)
    }

    fun updaterow(current: chatPageData, position: Int) {
        data!![position] = current
        notifyItemChanged(position)
    }

    fun gettime(): String? {
        val time = data!![itemCount - 1]
        return time.tordtext
    }

    fun adddata(current: chatPageData) {
        data!!.add(current)
        notifyItemInserted(itemCount + 1)
    }

    fun addtimerow(current: chatPageData, pos: Int) {
        data!!.add(pos + 1, current)
        notifyItemInserted(pos + 1)
    }

    fun adddatarecieved(current: chatPageData) {
        data!!.add(current)
        notifyDataSetChanged()
    }

    fun removerow(position: Int) {
        data!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getExpanded() {
        if (itemCount != 0) {
            if (data!![itemCount - 1].mode == 8 || data!![itemCount - 1].mode == 9) {
                if ((!data!![itemCount - 1].expanded)!!)
                    removerow(itemCount - 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (data != null)
            data!!.size
        else
            0
    }

    private inner class rec internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var message: AppCompatTextView
        internal var layout: RelativeLayout

        init {
            layout = itemView.findViewById<View>(R.id.layout) as RelativeLayout
            message = itemView.findViewById<View>(R.id.message) as AppCompatTextView

            message.setOnClickListener {
                if (clicklistener != null)
                    clicklistener!!.rowClicked(9, adapterPosition, data!![adapterPosition].tordtext)
            }
        }
    }

    private inner class new_send internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var message: AppCompatTextView
        internal var time: AppCompatTextView
        internal var layout: RelativeLayout

        init {
            message = itemView.findViewById<View>(R.id.message) as AppCompatTextView
            time = itemView.findViewById<View>(R.id.time) as AppCompatTextView
            layout = itemView.findViewById<View>(R.id.layout) as RelativeLayout

            message.setOnClickListener {
                if (time.visibility == View.GONE)
                    expand(time)
                else
                    collapse(time)
                /*if(clicklistener!=null)
                        clicklistener.rowClicked(8, getAdapterPosition(), data.get(getAdapterPosition()).tordtext);


               */
            }
        }
    }

    /*class send extends RecyclerView.ViewHolder {

        AppCompatTextView message;
        RelativeLayout layout;

        public send(View itemView) {
            super(itemView);
            message = (AppCompatTextView) itemView.findViewById(R.id.message);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clicklistener!=null)
                        clicklistener.rowClicked(8, getAdapterPosition(), data.get(getAdapterPosition()).tordtext);
                }
            });
        }
    }

    class sendtime extends RecyclerView.ViewHolder {

        AppCompatTextView text;

        public sendtime(View itemView) {
            super(itemView);
            text = (AppCompatTextView) itemView.findViewById(R.id.time);
        }
    }

    class rectime extends RecyclerView.ViewHolder {

        AppCompatTextView text;

        public rectime(View itemView) {
            super(itemView);
            text = (AppCompatTextView) itemView.findViewById(R.id.time);
        }
    }
*/
    internal inner class header(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: AppCompatTextView

        init {
            text = itemView.findViewById<View>(R.id.text) as AppCompatTextView
        }
    }

    interface Clicklistener {
        fun rowClicked(type: Int, pos: Int, timetext: String?)
    }

    private fun expand(v: View) {
        //set Visible
        v.visibility = View.VISIBLE

        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(widthSpec, heightSpec)

        val mAnimator = slideAnimator(v, 0, v.measuredHeight)
        mAnimator.start()
    }

    private fun collapse(v: View) {
        val finalHeight = v.height

        val mAnimator = slideAnimator(v, finalHeight, 0)

        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                v.visibility = View.GONE
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        mAnimator.start()
    }

    private fun slideAnimator(v: View, start: Int, end: Int): ValueAnimator {

        val animator = ValueAnimator.ofInt(start, end)
        animator.addUpdateListener { valueAnimator ->
            //Update Height
            val value = valueAnimator.animatedValue as Int
            val layoutParams = v.layoutParams
            layoutParams.height = value
            v.layoutParams = layoutParams
        }
        return animator
    }
}
