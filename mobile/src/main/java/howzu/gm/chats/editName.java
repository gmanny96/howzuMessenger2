package howzu.gm.chats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class editName extends Fragment {

    View view;
    TabLayout buttons;
    TextInputLayout layout;
    AppCompatEditText name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inputname2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = (TextInputLayout) view.findViewById(R.id.input);
        name = (AppCompatEditText) view.findViewById(R.id.name);

        name.requestFocus();
        layout.setCounterEnabled(true);
        layout.setCounterMaxLength(20);

        buttons.addTab(buttons.newTab().setText("Cancel"));
        buttons.addTab(buttons.newTab().setText("Save"));

        buttons.setSelectedTabIndicatorColor(Color.TRANSPARENT);

        buttons.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                click(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                click(tab);
            }
        });
    }

    private void click(TabLayout.Tab tab){
        if(tab.getText().toString().equals("Save")) {
            if (!name.getText().toString().trim().isEmpty()) {
                getActivity().startService(new Intent(getContext(), service_setName.class).putExtra("name", name.getText()));
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                layout.setError("Name cant be null");
            }
        }
        else{
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
