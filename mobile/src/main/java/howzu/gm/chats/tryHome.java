package howzu.gm.chats;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class tryHome extends Fragment{

    View view;
    //Toolbar bar;
    TabLayout tabs;
    FloatingActionButton contacts;
    workToDo callBack;

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
        view = inflater.inflate(R.layout.try_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabs = (TabLayout) view.findViewById(R.id.tab);
        //bar = (Toolbar) view.findViewById(R.id.toolbar);
        contacts = (FloatingActionButton) view.findViewById(R.id.add);

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity, new newContacts(), "contacts").addToBackStack(null).commit();
            }
        });

        callBack.changeBarColor(0);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(ContextCompat.getColor(view.getContext(),R.color.indigo700));
            //window.setStatusBarColor(ContextCompat.getColor(view.getContext(),R.color.blue700));
        }*/

        //bar.setTitle("");
        //tabs.addTab(tabs.newTab().setText("Recent"));
        //tabSelected(0);
        //tabs.addTab(tabs.newTab().setText("Contacts"));
        //tabUnselected(1);

        tabs.addTab(tabs.newTab().setIcon(R.drawable.homev));
        tabSelected(0);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.magnifyv));
        tabUnselected(1);
        //tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab().setIcon(R.drawable.accountv));
        tabUnselected(2);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.settingsv));
        tabUnselected(3);

        selectTab(0);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelected(tab.getPosition());
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabUnselected(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newChats(), "chats").commit();

        //tabs.setTabTextColors(Color.BLACK, Color.WHITE);

        tabs.setSelectedTabIndicatorColor(Color.TRANSPARENT);
    }

    private void selectTab(int p)
    {
        switch (p)
        {
            case 0:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newChats(), "chats").commit();
                break;
            case 1:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newExplore(), "explore").commit();
                break;
            case 2:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newAccountPage(), "profile").commit();
                break;
            default:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new setting(), "setting").commit();
                break;
        }
    }

    private void tabSelected(int p){
        //tabs.getTabAt(p).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
    }

    private void tabUnselected(int p){
        tabs.getTabAt(p).getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        //tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.grey500), PorterDuff.Mode.MULTIPLY);
    }

    public void keyboardAppeared()
    {
        tabs.setVisibility(GONE);
        contacts.hide();
    }

    public void keyboardDAppeared()
    {
        tabs.setVisibility(VISIBLE);
        contacts.show();
    }
}
