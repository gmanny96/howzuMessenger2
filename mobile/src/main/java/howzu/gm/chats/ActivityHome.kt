package howzu.gm.chats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate

internal class ActivityHome : AppCompatActivity() {

    private val myReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra("from") == num) {
                val frag = supportFragmentManager.findFragmentByTag("chatpage") as chatPage
                frag.adddatarecieved(intent.getStringExtra("msg"), java.lang.Long.valueOf("time"))
            }
            chatUpdated(true, intent.getStringExtra("from"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val theme = preferences.getInt("theme", 0)

        if (theme == 0)
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            setTheme(R.style.darktheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1frame)

        if (supportFragmentManager.findFragmentByTag("homev") == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.activity, new Home(), "homev").commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.activity, new newHome(), "homev").commit();
            supportFragmentManager.beginTransaction().replace(R.id.layout, Fragment_Home(), "homev").commit()
        }
    }
}
