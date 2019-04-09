package howzu.gm.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class groups extends Fragment {

    View view;
    AppCompatEditText text;
    AppCompatButton button;
    AppCompatImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_createpage, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        text = (AppCompatEditText) view.findViewById(R.id.text);
        button = (AppCompatButton) view.findViewById(R.id.button);
        imageView = (AppCompatImageView) view.findViewById(R.id.pic);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), service_creategroup.class);
                getActivity().startService(intent);
            }
        });
    }
}
