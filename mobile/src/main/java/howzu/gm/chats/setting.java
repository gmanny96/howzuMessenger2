package howzu.gm.chats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class setting extends Fragment implements settingAdapter.Clicklistener{

    View view;
    RecyclerView list;
    settingAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RelativeLayout layout;
    int theme, width;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = view.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        theme = preferences.getInt("theme", 0);

        layout = (RelativeLayout) view.findViewById(R.id.layout);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width  = layout.getMeasuredWidth();
            }
        });

        adapter = new settingAdapter(getContext(), this, width);
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void rowClicked(View view, int p) {
        switch (p){
            /*case 0:
                editNameDialog dFragment = new editNameDialog();
                // Show DialogFragment
                dFragment.show(getActivity().getFragmentManager(), "Dialog Fragment");
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new editName(), "editName").addToBackStack(null).commit();
                break;*/
            case 0:
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new Status()).addToBackStack(null).commit();
                break;
            case 1:
                break;
            case 2:
                if (theme == 0)
                    editor.putInt("theme", 1);
                else
                    editor.putInt("theme", 0);
                editor.commit();
                startActivity(new Intent(view.getContext(), Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
