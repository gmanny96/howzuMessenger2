package howzu.gm.chats;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class newHome extends Fragment{

    View view;
    TabLayout tabs;
    FrameLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = (FrameLayout) view.findViewById(R.id.tabs);
        tabs = (TabLayout) view.findViewById(R.id.tab);

        tabs.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.homev));
        tabSelected(0);
        selectTab(0);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.bulletv));
        tabUnselected(1);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.magnifyv));
        tabUnselected(2);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.accountv));
        tabUnselected(3);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.settingsv));
        tabUnselected(4);

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
    }

    private void selectTab(int tab)
    {
        switch (tab)
        {
            case 0:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newChats(), "chats").commit();
                break;
            case 1:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new Search(), "search").commit();
                break;
            case 2:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new newAccountPage(), "account").commit();
                break;
            case 3:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new setting(), "setting").commit();

                break;
        }
    }



    private void tabSelected(int p){
        tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
    }

    private void tabUnselected(int p){
        tabs.getTabAt(p).getIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.grey500), PorterDuff.Mode.MULTIPLY);
    }
}
