package howzu.gm.chats

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager

internal class Activity_Login_Signup : AppCompatActivity(), workToDo {

    var preferences: SharedPreferences? = null
    var user: Int = 0
    var num: String? = null
    var input = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        var theme = preferences?.getInt("theme", 0)

        if (theme == 0)
            setTheme(R.style.lighttheme)
        else
            setTheme(R.style.darktheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        //user = preferences.getInt("user", 0);

        fragmentManager.addOnBackStackChangedListener { }

        //startHome()

        //if(getSupportFragmentManager().findFragmentByTag("1st") == null) {
        //    getSupportFragmentManager().beginTransaction().replace(R.id.activity, new Login(), "1st").commit();
        //}

        /*if(savedInstanceState != null) {

            if(input){
                showInputNum();
            }
            if(savedInstanceState.getString("chat")!= null && !savedInstanceState.getString("chat").equals(null)){
                this.num = savedInstanceState.getString("chat");
                showChatPage();
            }
        }
        else{
            start();
        }*/
    }

    private fun start() {
        when (user) {
            0 -> if (supportFragmentManager.findFragmentByTag("1st") == null) {
                supportFragmentManager.beginTransaction().replace(R.id.activity, page1st(), "1st").commit()
            }
            1 -> //login or signup page and then think of the sequence and open the one which was previously open
            2 -> accountView()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (num != null)
            outState!!.putString("chat", num)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun showInputNum() {
        input = true
        if (supportFragmentManager.findFragmentByTag("input") == null)
            supportFragmentManager.beginTransaction().replace(R.id.activity, inputNum(), "input").commit()
    }

    override fun openVerifyDialog(num: String) {
        val f = verifyNumDialog()
        val args = Bundle()
        args.putString("num", num)
        f.arguments = args
        f.show(supportFragmentManager, "verifyNum")
    }

    override fun verifyNumber(num: String) {
        val bundle = Bundle()
        bundle.putString("username", num)
        val verify = Verify()
        verify.arguments = bundle
        if (supportFragmentManager.findFragmentByTag("verify") == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity, verify, "verify").commit()
        }
    }

    override fun accountView() {
        if (supportFragmentManager.findFragmentByTag("create_account") == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity, createAccountView(), "create account").commit()
        }
    }

    /*override fun openContextMenu() {

        startActionMode(object : android.view.ActionMode.Callback {
            override fun onCreateActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.chat1, menu)
                return true
            }

            override fun onPrepareActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: android.view.ActionMode, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode) {

            }
        })

    }

    override fun chatUpdated(b: Boolean?, number: String) {
        val call = supportFragmentManager.findFragmentByTag("chats") as newChats
        call.updateChat(number)

        //meLists.getInstance().getChats().updateChat(number);
    }

    override fun optionClicked(tag: String) {
        when (tag) {
            "rc" -> startService(Intent(this, newRefreshContactService::class.java))
        }
    }

    override fun rowClick(num: String) {
        val editor = preferences?.edit()
        editor?.putString("Page", num)?.apply()

        this.num = num
        showChatPage()
    }

    override fun rowLongPress(num: String, name: String) {
        /*Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("num",num);
        chatsOptions f = new chatsOptions();
        f.setArguments(args);
        f.show(getFragmentManager(), "options");
        */
    }

    private fun showChatPage() {
        if (supportFragmentManager.findFragmentByTag("chatpage") == null) {
            if (!supportFragmentManager.findFragmentByTag("homev").isHidden) {
                supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag("homev")).add(R.id.activity, chatPage(), "chatpage").addToBackStack(null).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.activity, chatPage(), "chatpage").addToBackStack(null).commit()
            }
        }
    }

    override fun changeBarColor(which: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            when (which) {
                0 -> window.statusBarColor = ContextCompat.getColor(this, R.color.grey700)
                1 -> window.statusBarColor = ContextCompat.getColor(this, R.color.blue700)
            }
        }
    }

    override fun openSearch() {

    }*/
}