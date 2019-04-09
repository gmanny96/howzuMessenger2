package howzu.gm.chats

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import java.util.Collections

class countriesAdapter(internal var context: Context, internal var callback: workToDo) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater
    internal var country: Array<String>
    internal var cc: Array<String>

    init {
        inflater = LayoutInflater.from(context)
        country = context.resources.getStringArray(R.array.country)
        cc = context.resources.getStringArray(R.array.country_code)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return country(inflater.inflate(R.layout.country_row, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val c = holder as country
        c.country.text = country[position]
        c.code.text = cc[position]
    }

    override fun getItemCount(): Int {
        return country.size
    }

    fun searchCountries(country: String) {

    }

    inner class country(view: View) : RecyclerView.ViewHolder(view) {

        internal var country: AppCompatTextView
        internal var code: AppCompatTextView
        internal var layout: RelativeLayout

        init {
            layout = view.findViewById<View>(R.id.layout) as RelativeLayout
            country = view.findViewById<View>(R.id.country) as AppCompatTextView
            code = view.findViewById<View>(R.id.code) as AppCompatTextView
            layout.setOnClickListener {
                //callback.rowClick();
                //if (clicklistener != null)
                //    clicklistener.rowClicked(data.get(getAdapterPosition()).number);
            }
        }
    }
}
