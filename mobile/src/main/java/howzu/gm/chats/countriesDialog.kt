package howzu.gm.chats

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class countriesDialog : DialogFragment() {

    internal var callBack: workToDo
    internal var view: View
    internal var search: AppCompatEditText
    internal var list: RecyclerView
    internal var adapter: countriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.country, container, false)

        search = view.findViewById<View>(R.id.search) as AppCompatEditText
        list = view.findViewById<View>(R.id.list) as RecyclerView

        adapter = countriesAdapter(context!!, callBack)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

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
}
