package howzu.gm.chats

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.home2.*

class Fragment_Home : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home2, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.layoutManager = LinearLayoutManager(activity)

        plus.setOnClickListener { startActivity(Intent(activity, ActivityContacts::class.java)) }

        /*Intent intent = new Intent(getActivity(), ActivityChat.class);
        intent.putExtra("num", "");
        startActivity(intent);*/
    }
}
