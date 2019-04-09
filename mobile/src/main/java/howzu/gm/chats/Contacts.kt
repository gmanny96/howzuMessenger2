package howzu.gm.chats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Contacts : Fragment(), LoaderManager.LoaderCallbacks<List<contactsData>>, contactsAdapter.Clicklistener {

    internal var list: RecyclerView
    internal var adapter: contactsAdapter
    internal var preferences: SharedPreferences
    internal var view: View
    internal var callBack: workToDo

    private val myReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            //setLists();
            startLoader()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.refreshlist, container, false)
        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(myReceiver, IntentFilter("Contacts_updated"))
        //getActivity().startService(new Intent(getActivity(), serviceGetContacts.class));

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
        preferences = view.context.getSharedPreferences("data", Context.MODE_PRIVATE)

        list = view.findViewById<View>(R.id.list) as RecyclerView
        setList(null)
    }

    private fun startLoader() {
        loaderManager.initLoader(1, null, this).forceLoad()
    }

    fun setList(data: List<contactsData>?) {
        adapter = contactsAdapter(context, data, this, callBack)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        LocalBroadcastManager.getInstance(activity!!.applicationContext).unregisterReceiver(myReceiver)
        activity!!.stopService(Intent(activity, serviceGetContacts::class.java))
        Lists.getInstance().contactList = null
    }

    override fun rowClicked(number: String) {
        val editor = preferences.edit()
        editor.putString("Page", number).apply()

        activity!!.supportFragmentManager.beginTransaction().add(R.id.activity, chatPage(), "chatpage").addToBackStack(null).commit()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<contactsData>> {
        //Log.e("adapter me", "call jayegi");
        return contactsDataFetch(activity)
    }

    override fun onLoadFinished(loader: Loader<List<contactsData>>, data: List<contactsData>) {
        Log.e("adapter me", "call khm")
        setList(data)
    }

    override fun onLoaderReset(loader: Loader<List<contactsData>>) {

    }
}