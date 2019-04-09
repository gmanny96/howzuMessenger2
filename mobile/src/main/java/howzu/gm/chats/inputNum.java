package howzu.gm.chats;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class inputNum extends Fragment {

    View view;
    AppCompatEditText input;
    String[] country_code;
    String username, cc;
    SharedPreferences preferences;
    AppCompatButton button,tc;
    AppCompatTextView cctext;
    Toolbar toolbar;
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
        view = inflater.inflate(R.layout.input_num, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        input = (AppCompatEditText) view.findViewById(R.id.input);
        button = (AppCompatButton) view.findViewById(R.id.button);
        cctext = (AppCompatTextView) view.findViewById(R.id.cc);
        tc = (AppCompatButton) view.findViewById(R.id.tc);

        toolbar.setTitle("Phone Number");

        country_code = getResources().getStringArray(R.array.country_code);

        input.requestFocus();

        GetCountryZipCode();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(R.array.country, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cc = country_code[which];
                cctext.setText(cc);
            }
        });

        final AlertDialog dialog = builder.create();

        cctext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handled = checkpermission();
                }
                return handled;
            }
        });
    }

    private boolean checkpermission(){
        boolean handled = false;
        if (!input.getText().toString().trim().equals("") || !input.getText().toString().trim().isEmpty()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            if (android.provider.Settings.System.getInt(getContext().getContentResolver(), android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) != 0)
                Toast.makeText(getContext(), "Airplane mode is on.", Toast.LENGTH_LONG).show();
            else {
                number();
                handled = true;
            }
        }
        else{
            Toast.makeText(getContext(), "Number cant be empty.", Toast.LENGTH_LONG).show();
        }
        return handled;
    }

    private void number() {
        username = cc + input.getText().toString();
        callBack.openVerifyDialog(username);

        /*AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(R.string.Verify_Number);
        alertDialog.setMessage(view.getContext().getString(R.string.Verify) + " " + username + " " + view.getContext().getString(R.string.number) + ".");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle=new Bundle();
                bundle.putString("username", username);
                Verify verify = new Verify();
                verify.setArguments(bundle);
                if(getActivity().getSupportFragmentManager().findFragmentByTag("verify") == null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity, verify, "verify").commit();
                }
            }
        });
        alertDialog.show();
        */
    }

    private void GetCountryZipCode() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = getResources().getStringArray(R.array.country_ID);

        for (int i = 0; i<rl.length;i++) {
            if (rl[i].equals(CountryID.trim())) {
                cc = country_code[i];
                cctext.setText("("+rl[i]+") "+cc);
                break;
            }
        }
    }
}
