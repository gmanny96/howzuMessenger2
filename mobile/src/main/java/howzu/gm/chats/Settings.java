package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Settings extends Fragment{

    settingsAdapter adapter;
    Toolbar toolbar;
    View view;
    RecyclerView list;
    RelativeLayout layout;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = view.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        int theme = preferences.getInt("theme", 0);

        layout = (RelativeLayout) view.findViewById(R.id.layout);
        if(theme == 0)
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.lighttheme));
        else
            layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.darktheme));

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        list = (RecyclerView) view.findViewById(R.id.list);
        adapter = new settingsAdapter(getActivity().getApplicationContext());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }
}