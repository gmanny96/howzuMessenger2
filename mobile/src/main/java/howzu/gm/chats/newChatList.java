package howzu.gm.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class newChatList extends Fragment implements LoaderManager.LoaderCallbacks<List<chatsData>>,chatsAdapter.Clicklistener{

    workToDo callBack;
    View view;
    RecyclerView list;
    chatsAdapter adapter;

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
        view = inflater.inflate(R.layout.chat_list, container, false);
        startLoader();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void startLoader(){
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    public void setlist(List<chatsData> data){
        adapter = new chatsAdapter(view.getContext(), data, callBack);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    @Override
    public Loader<List<chatsData>> onCreateLoader(int id, Bundle args) {
        Log.d("ff", "chatsdatafetch");
        return new chatsDataFetch(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<chatsData>> loader, List<chatsData> data) {
        Log.d("ff", "chatsdatafetchkhm");
        setlist(data);
    }

    @Override
    public void onLoaderReset(Loader<List<chatsData>> loader) {

    }

    @Override
    public void rowClicked(String number) {

    }

    @Override
    public void rowContext(int pos, String number, String name) {

    }
}
