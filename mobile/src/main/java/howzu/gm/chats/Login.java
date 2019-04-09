package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Login extends Fragment {

    View view;
    AppCompatTextView cc;
    FloatingActionButton con;
    Toolbar toolbar;
    AppCompatEditText name, number;
    workToDo callBack;
    String[] country_code;
    SharedPreferences preferences;
    String username = null;

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
        view = inflater.inflate(R.layout.login, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        cc = (AppCompatTextView) view.findViewById(R.id.cc);
        con = (FloatingActionButton) view.findViewById(R.id.con);
        name = (AppCompatEditText) view.findViewById(R.id.name);
        number = (AppCompatEditText) view.findViewById(R.id.number);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setTitle("Login");

        country_code = getResources().getStringArray(R.array.country_code);

        String nameText = preferences.getString("name", null);
        String numText = preferences.getString("user", null);

        if(nameText!=null)
            name.setText(nameText);

        if(numText!=null)
            number.setText(numText);

        GetCountryZipCode();

        Cursor c = getContext().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (c != null && c.moveToNext()) {
            String columnValue = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            name.setText(columnValue);
        }
        if (c != null)
            c.close();

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeButton();
            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeButton();
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countriesDialog f = new countriesDialog();
                f.show(getActivity().getSupportFragmentManager(), "countries");
            }
        });
    }

    private void changeButton(){
        if(name.getText().toString().trim().isEmpty() || number.getText().toString().trim().isEmpty()){
            con.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.grey500));
        }
        else{
            con.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.blue500));
        }
    }

    private boolean checkpermission(){
        boolean handled = false;
        if (!name.getText().toString().trim().isEmpty() || !number.getText().toString().trim().isEmpty()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(number.getWindowToken(), 0);
            if (android.provider.Settings.System.getInt(getContext().getContentResolver(), android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) != 0)
                Toast.makeText(getContext(), "Airplane mode is on.", Toast.LENGTH_LONG).show();
            else {
                number();
                handled = true;
            }
        }
        else{
            Toast.makeText(getContext(), "Number and name cant be empty.", Toast.LENGTH_LONG).show();
        }
        return handled;
    }

    private void number() {
        username = cc + number.getText().toString();
        preferences.edit().putString("user", username).putString("name", name.getText().toString()).apply();
        callBack.openVerifyDialog(username);
    }

    private void GetCountryZipCode() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = getResources().getStringArray(R.array.country_ID);

        for (int i = 0; i<rl.length;i++) {
            if (rl[i].equals(CountryID.trim())) {
                cc.setText("("+rl[i]+") "+country_code[i]);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(username != null)
            preferences.edit().putString("user", username).apply();

        if(!name.getText().toString().trim().isEmpty())
            preferences.edit().putString("user", name.getText().toString().trim()).apply();
    }
}
