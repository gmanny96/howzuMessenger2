package howzu.gm.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class verifyNumDialog extends DialogFragment{

    View view;
    AppCompatTextView text;
    AppCompatButton cancel, verify;
    workToDo callBack;
    String num;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.verify_num, container, false);

        Log.e("dd", "calling create again");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        text = (AppCompatTextView) view.findViewById(R.id.text);
        cancel = (AppCompatButton) view.findViewById(R.id.cancel);
        verify = (AppCompatButton) view.findViewById(R.id.verify);

        num = getArguments().getString("num");
        text.setText("Verify " +num+ " ?");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.verifyNumber(num);
            }
        });

        return view;
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
