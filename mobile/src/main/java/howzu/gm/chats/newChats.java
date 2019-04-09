package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class newChats extends Fragment implements LoaderManager.LoaderCallbacks<List<chatsData>>,chatsAdapter.Clicklistener{

    View view;
    //Toolbar bar;
    RecyclerView list;
    chatsAdapter adapter;
    SharedPreferences preferences;
    FloatingActionButton contacts, search;
    TabLayout tabs;
    workToDo callBack;
    //AppBarLayout ablayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (workToDo) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_chat, container, false);
        startLoader();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //tabs = (TabLayout) view.findViewById(R.id.up);
        //bar = (Toolbar) view.findViewById(R.id.bar);
        search = (FloatingActionButton) view.findViewById(R.id.search);
        contacts = (FloatingActionButton) view.findViewById(R.id.contacts);
        list = (RecyclerView) view.findViewById(R.id.list);
        //ablayout = (AppBarLayout) view.findViewById(R.id.ablayout);

        //ablayout.setExpanded(false, true);

        //tabs.addTab(tabs.newTab().setText("Recent"));
        //tabs.addTab(tabs.newTab().setText("Contacts"));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(view.getContext(),R.color.blue700));
        }*/

        //tabs.setTabTextColors(ContextCompat.getColor(view.getContext(),R.color.grey500),ContextCompat.getColor(view.getContext(),R.color.blue500));
        //tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(view.getContext(),R.color.blue500));

        /*bar.setNavigationIcon(R.drawable.magnifyv);
        bar.setTitle("");

        //selectTab(0);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (contacts.isShown()) {
                        contacts.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!contacts.isShown()) {
                        contacts.show();
                    }
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity, new newContacts(), "chats").addToBackStack(null).commit();
            }
        });

        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    /*private void selectTab(int p) {
        switch (p)
        {
            case 0:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, new newChatList(), "chats").addToBackStack(null).commit();
                break;
            case 1:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, new newContacts(), "chats").addToBackStack(null).commit();
                break;
        }
    }
*/
    private void startLoader(){
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    public void setlist(List<chatsData> data){
        adapter = new chatsAdapter(view.getContext(), data, callBack);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void updateChat(String number){
        Boolean found = false;
        for(int i = 0; i<adapter.getItemCount()-1; i++){
            if(adapter.findNumber(i)!=null && adapter.findNumber(i).equals(number)){
                //adapter.checkToday();
                adapter.updaterow(i);
                found = true;
            }
        }
        if(!found){
            //adapter.checkToday();
            adapter.addrow(addrow(number));
        }
    }

    public chatsData addrow(String user){
        chatsData data = new chatsData();
        data.setNumber(user);
        data.setType(1);
        return data;
    }

    @Override
    public Loader<List<chatsData>> onCreateLoader(int id, Bundle args) {
        Log.d("ff", "chatsdatafetch");
        return new chatsDataFetch(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<chatsData>> loader, List<chatsData> data) {
        Log.d("ff", "chatsdatafetchkhm");
        setlist(data);
    }

    @Override
    public void onLoaderReset(Loader<List<chatsData>> loader) {

    }

    @Override
    public void rowClicked(String number) {

    }

    @Override
    public void rowContext(int pos, String number, String name) {

    }
}
