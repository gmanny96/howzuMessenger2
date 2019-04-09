package howzu.gm.chats;

import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class image_chooser extends Fragment implements imageAdapter.Clicklistener{

    View view;
    Toolbar toolbar;
    RecyclerView list;
    imageAdapter adapter;
    Uri[] mUrls, nUrls;
    Cursor cc;
    RelativeLayout layout;
    boolean single;

    public image_chooser(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image_chooser, container, false);
        single = getArguments().getBoolean("single");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        list = (RecyclerView) view.findViewById(R.id.list);
        layout = (RelativeLayout) view.findViewById(R.id.layout);

        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey50));

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
        if(!single) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            setHasOptionsMenu(true);
        }
        toolbar.setTitle("Choose image");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        cc = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cc != null) {
            mUrls = new Uri[cc.getCount()];
            for(int i = 0; i<cc.getCount(); i++){
                cc.moveToNext();
                mUrls[i] = Uri.parse(cc.getString(1));
            }
        }

        adapter = new imageAdapter(getContext(), mUrls, cc.getCount(), this);
        closeCursor();
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(getContext(), 3));

    }

    private void closeCursor(){
        if(cc !=null && !cc.isClosed())
            cc.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCursor();
    }

    private void next(){
        for(int i = 0; i<adapter.getItemCount();i++){
            RecyclerView.ViewHolder vh = list.findViewHolderForAdapterPosition(i);
            View v = vh.itemView;
            AppCompatCheckBox checkBox = (AppCompatCheckBox) v.findViewById(R.id.check);
            if(checkBox.isChecked()){
                int n = nUrls.length;
                nUrls[n+1]=mUrls[i];
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next:
                next();
                break;
        }
        return true;
    }

    @Override
    public void rowClicked(int p, AppCompatCheckBox cb) {
        if(single) {
            Bundle b = new Bundle();
            b.putString("path", mUrls[p].getPath());
            imageCropper ic = new imageCropper();
            ic.setArguments(b);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, ic, "image_chooser").addToBackStack(null).commit();
        }
        else{
            if(cb.isChecked())
                cb.setChecked(false);
            else
                cb.setChecked(true);
        }
    }
}
