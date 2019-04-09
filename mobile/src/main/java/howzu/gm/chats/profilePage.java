package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class profilePage extends Fragment{

    View view;
    Toolbar toolbar;
    CollapsingToolbarLayout layout;
    RecyclerView list;
    int theme, pic;
    AppCompatImageView image;
    SharedPreferences preferences;
    String status;
    int p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilepage, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        theme = preferences.getInt("theme", 0);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        layout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        image = (AppCompatImageView) view.findViewById(R.id.image);

        if(theme == 0)
            layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lighttheme));
        else
            layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darktheme));

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.post(new Runnable() {
                    public void run() {
                      p = view.getWidth();
                    }
                });
            }
        });

        image.setMaxHeight(p);
        image.setMinimumWidth(p);

        String[] arg = {""};
        String[] projection = {Dbhelper.pic, Dbhelper.status};
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        Cursor cursor = getContext().getContentResolver().query(uri, projection, Dbhelper.phonenumber + "= ? ", arg, null);

        if(cursor != null && cursor.moveToNext())
        {
            pic = cursor.getInt(cursor.getColumnIndex(Dbhelper.pic));
            status = cursor.getString(cursor.getColumnIndex(Dbhelper.status));
        }

        if(cursor!=null && !cursor.isClosed())
            cursor.close();

        if(pic == 1) {

            Picasso.with(getContext()).load(loadImageFromStorage("")).resize(p, p).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    image.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thumb));

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        else{
            image.setMinimumHeight(0);
        }
    }

    private File loadImageFromStorage(String num) {
        return new File(Environment.getExternalStorageDirectory() + "/Chats/Media/Profile Images", num);
    }
}
