package howzu.gm.chats

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ActivitySearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val theme = preferences.getInt("theme", 0)

        if (theme == 0)
            setTheme(R.style.lighttheme)
        else
            setTheme(R.style.darktheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1frame)

        supportFragmentManager.beginTransaction().add(R.id.layout, Search(), "search").addToBackStack(null).commit()

    }
}
