package howzu.gm.chats;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Home extends Fragment{

    View view;
    //ViewPager pager;
    //int page = 0;
    TabLayout tabs;
    FloatingActionButton search;
    Toolbar searchBar;
    AppCompatEditText input;
    workToDo callBack;
    RecyclerView options;
    homeOptAdapter adapter;


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
        view = inflater.inflate(R.layout.home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabs = (TabLayout) view.findViewById(R.id.tab);
        //pager = (ViewPager) view.findViewById(R.id.pager);
        searchBar = (Toolbar) view.findViewById(R.id.toolbar);
        input = (AppCompatEditText) view.findViewById(R.id.input);
        search = (FloatingActionButton) view.findViewById(R.id.search);
        options = (RecyclerView) view.findViewById(R.id.options);


        adapter = new homeOptAdapter(getContext(), callBack);
        options.setAdapter(adapter);
        options.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        searchBar.setNavigationIcon(R.drawable.back);
        searchBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);

        searchBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.GONE);
                search.show();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                search.hide();
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(input.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new Search(), "search").addToBackStack(null).commit();

            }
        });

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(view.getContext(),R.color.blue700));
        }*/

        tabs.addTab(tabs.newTab().setText("Home"));
        //tabSelected(0);
        tabs.addTab(tabs.newTab().setText("Settings"));
        //tabUnselected(1);

        tabs.setTabTextColors(ContextCompat.getColor(view.getContext(),R.color.grey500),ContextCompat.getColor(view.getContext(),R.color.blue500));

        //tabs.addTab(tabs.newTab().setIcon(R.drawable.homev));
        //tabSelected(0);
        //tabs.addTab(tabs.newTab().setIcon(R.drawable.bulletv));
        //tabUnselected(1);
        //tabs.addTab(tabs.newTab().setIcon(R.drawable.settingsv));
        //tabUnselected(1);

        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(view.getContext(),R.color.blue500));

        //tabs.getTabAt(0).select();
        selectTab(0);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tabSelected(tab.getPosition());
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tabUnselected(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        //pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        //tabs.setupWithViewPager(pager);
        /*tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                tabSelected(tab.getPosition());
                if(tab.getPosition() == 2){
                    search.hide();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabUnselected(tab.getPosition());
                if(tab.getPosition() == 2){
                    search.show();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    private void selectTab(int p)
    {
        if(p==0)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new Chats(), "chats").commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new setting(), "settings").commit();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Chats chats = new Chats();
                    meLists.getInstance().setChats(chats);
                    return chats;
                //case 1:
                  //  return new Contacts();
                default:
                    return new Contacts();
            }
        }
    }


    private void tabSelected(int p){
        tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
    }

    private void tabUnselected(int p){
        tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.grey500), PorterDuff.Mode.MULTIPLY);
    }
}
