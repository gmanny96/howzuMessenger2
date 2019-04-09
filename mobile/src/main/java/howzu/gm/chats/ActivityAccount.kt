package howzu.gm.chats

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import howzu.gm.chats.R
import howzu.gm.chats.chatPage

internal class ActivityAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences: SharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val theme: Int = preferences.getInt("theme", 0)

        if (theme == 0)
            setTheme(R.style.lighttheme)
        else
            setTheme(R.style.darktheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1frame)

        val editor = preferences.edit()
        editor.putString("Page", intent.getStringExtra("num")).apply()

        supportFragmentManager.beginTransaction().add(R.id.activity, chatPage(), "chatpage").addToBackStack(null).commit()
    }
}
