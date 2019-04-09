package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Status extends Fragment{

    RelativeLayout layout;
    SharedPreferences preferences;
    AppCompatEditText statusinput;
    RecyclerView statuslist;
    TextInputLayout statuslayout;
    statusAdapter adapter;
    Toolbar toolbar;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.status,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int theme = preferences.getInt("theme", 0);

        layout = (RelativeLayout) view.findViewById(R.id.layout);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        statusinput = (AppCompatEditText) view.findViewById(R.id.statusinput);
        statuslist = (RecyclerView) view.findViewById(R.id.list);
        statuslayout = (TextInputLayout) view.findViewById(R.id.til);

        toolbar.setTitle("Status");

        toolbar.setNavigationIcon(R.drawable.back);

        if (theme == 0){
            toolbar.getNavigationIcon().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(view.getContext(), R.color.lightprimary), PorterDuff.Mode.MULTIPLY));
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.lighttheme));
        }else
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.darktheme));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        statuslayout.setCounterEnabled(true);

        statusinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter=new statusAdapter(getActivity().getApplicationContext());
        statuslist.setAdapter(adapter);
        statuslist.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }
}