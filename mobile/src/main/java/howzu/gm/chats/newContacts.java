package howzu.gm.chats;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class newContacts extends Fragment implements LoaderManager.LoaderCallbacks<List<contactsData>>,contactsAdapter.Clicklistener{

    View view;
    Toolbar toolbar;
    RecyclerView list;
    workToDo callBack;
    SharedPreferences preferences;
    contactsAdapter adapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_contacts, container, false);

        startLoader();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = view.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        list = (RecyclerView) view.findViewById(R.id.list);

        toolbar.setTitle("Contacts");
        //toolbar.setNavigationIcon(R.drawable.back);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = view.getContext().getTheme();
        theme.resolveAttribute(R.attr.messengerBack, typedValue, true);
        //int color = typedValue.data;

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);
        toolbar.getNavigationIcon().setColorFilter(typedValue.data, PorterDuff.Mode.MULTIPLY);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(view.getContext(),R.color.blue700));
        }*/

        callBack.changeBarColor(1);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.changeBarColor(0);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setList(null);
    }

    private void startLoader(){
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    public void setList(List<contactsData> data){
        adapter = new contactsAdapter(getContext(), data, this, callBack);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    @Override
    public void rowClicked(String number) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Page", number).apply();

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new chatPage(), "chatpage").addToBackStack(null).commit();
    }

    @Override
    public Loader<List<contactsData>> onCreateLoader(int id, Bundle args) {
        //Log.e("adapter me", "call jayegi");
        return new contactsDataFetch(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<contactsData>> loader, List<contactsData> data) {
        Log.e("adapter me", "call khm");
        setList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<contactsData>> loader) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
