package howzu.gm.chats

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

class Activity_Main : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences: SharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val account: Int = preferences.getInt("account", 0)

        //check if login was done homepage was displayed last time then display home else display login

    }
}