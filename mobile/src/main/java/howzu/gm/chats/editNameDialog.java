package howzu.gm.chats;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class editNameDialog extends DialogFragment {

    AppCompatTextView title, counter, save;
    AppCompatEditText input;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inputname2, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        title = (AppCompatTextView) view.findViewById(R.id.title);
        counter = (AppCompatTextView) view.findViewById(R.id.counter);

        save = (AppCompatTextView) view.findViewById(R.id.save);

        input = (AppCompatEditText) view.findViewById(R.id.input);

        counter.setText("");

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString().trim().isEmpty()){
                    counter.setText("26");
                }
                else{
                    counter.setText(String.valueOf(26 - input.getText().toString().length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        return view;
    }
    private void click() {
        if (!input.getText().toString().trim().isEmpty()) {
            getActivity().startService(new Intent(view.getContext(), service_setName.class).putExtra("name", input.getText()));
            dismiss();
        }else {
            Toast.makeText(view.getContext(), "Name cant be null", Toast.LENGTH_SHORT).show();
        }
    }

}
