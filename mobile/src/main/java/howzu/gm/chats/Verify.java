package howzu.gm.chats;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Random;

public class Verify extends Fragment {

    View view;
    ProgressBar bar;
    AppCompatImageButton edit;
    AppCompatButton again;
    AppCompatTextView number, title;
    String message, username;
    SharedPreferences preferences;
    CountDownTimer timer;
    workToDo callBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_creation, container, false);
        username = getArguments().getString("username");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        int status = preferences.getInt("user", 0);
        String user = preferences.getString("user", null);

        bar = (ProgressBar) view.findViewById(R.id.load);
        title = (AppCompatTextView) view.findViewById(R.id.text);
        again = (AppCompatButton) view.findViewById(R.id.again);

        number = (AppCompatTextView) view.findViewById(R.id.number);
        edit = (AppCompatImageButton) view.findViewById(R.id.edit);

        number.setText(user);

        verifying();

        bar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
        edit.getDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.grey500), PorterDuff.Mode.MULTIPLY);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.showInputNum();
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgWork();
            }
        });

        msgWork();
    }

    private void msgWork(){
        Random r = new Random();
        int number = (r.nextInt(9976 - 2251 + 1) + 2251);
        message = "Hi ur pin is " + number;
        SmsManager sms = SmsManager.getDefault();

        verifying();

        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_SENT"), 0);

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        view.getContext().registerReceiver(Receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
                        timer.start();
                        break;
                    default:
                        problem();
                        break;

                }
            }
        }, new IntentFilter("SMS_SENT"));

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 0);
        else
            sms.sendTextMessage(username, null, message, sentPI, null);

        timer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                if (Receiver != null) {
                    getContext().unregisterReceiver(Receiver);
                    Receiver = null;
                }
                problem();
            }
        }.start();
    }

    private void problem(){
        title.setText("Some problem occured");
        bar.setVisibility(View.GONE);
        again.setVisibility(View.VISIBLE);
        again.setClickable(true);
    }

    private void verifying(){
        bar.setVisibility(View.VISIBLE);
        title.setText("Verifying");
        again.setVisibility(View.INVISIBLE);
        again.setClickable(false);
    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] sms = (Object[]) bundle.get("pdus");

                for (int i = 0; i < sms.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String messagerecieved = currentMessage.getMessageBody();
                    if (phoneNumber.equals(username) && messagerecieved.equals(message)) {
                        preferences.edit().putString("username", username).putInt("user", 1).apply();
                        callBack.accountView();
                        //getContext().startActivity(new Intent(getContext(), Subscription.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            view.getContext().unregisterReceiver(Receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (workToDo) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnHeadlineSelectedListener");
        }
    }
}
