package howzu.gm.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class createAccountView extends Fragment{

    View view;
    AppCompatTextView text, hint, number;
    AppCompatButton again;
    ProgressBar bar;
    SharedPreferences preferences;
    workToDo callBack;
    AppCompatImageButton edit;
    String user;

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
        view = inflater.inflate(R.layout.account_creation, container, false);
        getActivity().startService(new Intent(view.getContext(), newAccountService.class));
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(myReceiver, new IntentFilter("account_creation"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        user = preferences.getString("user", null);

        bar = (ProgressBar) view.findViewById(R.id.load);
        text = (AppCompatTextView) view.findViewById(R.id.text);
        again = (AppCompatButton) view.findViewById(R.id.again);

        number = (AppCompatTextView) view.findViewById(R.id.number);
        edit = (AppCompatImageButton) view.findViewById(R.id.edit);


        bar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);

        working();

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                working();
            }
        });

        hint = (AppCompatTextView) view.findViewById(R.id.hint);
        hint.setText("Sit back and relax, we will notify you when work is done.");
    }

    private void working(){
        number.setText(user);
        text.setText("Working on account");
        bar.setVisibility(View.VISIBLE);
        again.setVisibility(View.INVISIBLE);
        again.setClickable(false);

        getActivity().startService(new Intent(view.getContext(), newAccountService.class));
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(myReceiver, new IntentFilter("account_creation"));
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int user = preferences.getInt("user", 1);
            switch (user) {
                case 2:
                    try {
                        getActivity().getApplicationContext().unregisterReceiver(myReceiver);
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    getActivity().startService(new Intent(view.getContext(), newAccountService.class));
                    text.setText("Some problem Occured");
                    bar.setVisibility(View.GONE);
                    again.setVisibility(View.VISIBLE);
                    again.setClickable(true);
                    break;
                case 3:
                    callBack.startHome();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().getApplicationContext().unregisterReceiver(myReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
