package howzu.gm.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

public class Search extends Fragment implements searchAdapter.Clicklistener {

    RecyclerView list;
    AppCompatEditText search;
    searchAdapter adapter;
    View view;
    Bitmap bitmap;
    String searchword;
    RelativeLayout layout;
    Toolbar toolbar;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, container, false);
        getContext().registerReceiver(myReceiver, new IntentFilter("search_updated"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int theme = preferences.getInt("theme", 0);
        Boolean global_search = preferences.getBoolean("global_search", false);

        layout = (RelativeLayout) view.findViewById(R.id.layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        list = (RecyclerView) view.findViewById(R.id.list);
        search = (AppCompatEditText) view.findViewById(R.id.search);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if(theme == 0)
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.lighttheme));
        else
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.darktheme));



        /*if(global_search)
            switchCompat.setChecked(true);
        else
            switchCompat.setChecked(false);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    preferences.edit().putBoolean("global_search", true).apply();
                }
                else{
                    preferences.edit().putBoolean("global_search", false).apply();
                }
            }
        });*/

        setlist();
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        search.requestFocus();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchword = search.getText().toString();
                getActivity().stopService(new Intent(getActivity(), service_searchdata.class));
                if(!searchword.equals(""))
                    getActivity().startService(new Intent(getActivity(), service_searchdata.class).putExtra("searchword", searchword));
            }
        });
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setlist();
        }
    };

    public void setlist() {
        adapter = new searchAdapter(view.getContext(), Lists.getInstance().getSearchList(), this);
        list.setAdapter(adapter);
    }

    @Override
    public void rowClicked(View view, String number) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Page", number).apply();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new chatPage()).addToBackStack(null).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.getContext().unregisterReceiver(myReceiver);
        getActivity().stopService(new Intent(view.getContext(), service_searchdata.class));
        Lists.getInstance().setSearchList(null);
    }
}
