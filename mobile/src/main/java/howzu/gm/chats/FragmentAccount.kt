package howzu.gm.chats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentAccount : Fragment() {

    internal var view: View
    internal var button: AppCompatButton
    internal var preferences: SharedPreferences

    private val myReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val user = preferences.getInt("user", 1)
            when (user) {
                1 -> {
                    button.visibility = View.VISIBLE
                    button.text = "Try again"
                }
                2 -> {
                    preferences.edit().putInt("user", 3).apply()
                    button.visibility = View.INVISIBLE
                    if (activity.supportFragmentManager.findFragmentByTag("mypage") == null) {
                        activity.supportFragmentManager.beginTransaction().replace(R.id.activity, inputName(), "mypage").commit()
                    }
                    activity.startService(Intent(getContext(), serviceAccount::class.java))
                }
                else -> {
                }
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater!!.inflate(R.layout.subscription, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = view.context.getSharedPreferences("data", Context.MODE_PRIVATE)

        button = view.findViewById<View>(R.id.button) as AppCompatButton
        button.setOnClickListener { view.context.startService(Intent(context, serviceAccount::class.java)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            view.context.unregisterReceiver(myReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }
}
