package howzu.gm.chats;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class myPage extends Fragment {

    Uri picUri;
    View view;
    AppCompatTextView image;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RelativeLayout layout;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mypage, container, false);
        view.getContext().registerReceiver(myReceiver, new IntentFilter("pic"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = preferences.edit();

        int theme = preferences.getInt("theme", 0);
        layout = (RelativeLayout) view.findViewById(R.id.layout);
        image = (AppCompatTextView) view.findViewById(R.id.image);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if(theme == 0)
            layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lighttheme));
        else
            layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darktheme));

        int pic = preferences.getInt("pic", 0);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int p = metrics.widthPixels/2;

        image.setHeight(p);
        image.setWidth(p);

        setImage();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ff", "clicked");
                if (Lists.getInstance().getConnection() != null && Lists.getInstance().getConnection().isConnected()) {
                    registerForContextMenu(view);
                    getActivity().openContextMenu(view);
                }
            }
        });
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setImage();
        }
    };

    public void setImage() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int p = metrics.widthPixels;
        Picasso.with(view.getContext()).load(loadImageFromStorage()).resize(p, p).transform(new getCircleBitmap()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                image.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.grey500));
                image.setText("M");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private File loadImageFromStorage() {
        return new File(Environment.getExternalStorageDirectory() + "/Chats/Media/My Profile Images", "+919560083681");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.editimageoptions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accessgallery:
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, new image_chooser(), "image_chooser").addToBackStack(null).commit();
                //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //this.startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                return true;
            case R.id.accesscamera:
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                this.startActivityForResult(captureIntent, 1);
                return true;
            case R.id.removepic:
                editor.putInt("pic", 0).commit();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ff", "oac");
        switch (requestCode) {
            case 1:
                Log.e("ff", "picuri");
                picUri = data.getData();
                performCrop();
                break;
            case 2:
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] byteArray = stream.toByteArray();

                getActivity().startService(new Intent(getContext(), serviceChangeProfilePic.class).putExtra("profile_photo", byteArray));
                break;
        }

    }

    private void performCrop() {
        Log.e("ff", "pc");
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 2);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            getContext().unregisterReceiver(myReceiver);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
