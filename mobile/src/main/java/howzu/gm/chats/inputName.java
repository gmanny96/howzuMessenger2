package howzu.gm.chats;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class inputName extends Fragment{

    View view;
    AppCompatEditText editText;
    AppCompatTextView image;
    Cursor c;
    Uri picUri;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AppCompatButton button;
    byte[] byteArray;
    Boolean pic;
    int user;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inputname, container, false);
        view.getContext().registerReceiver(myReceiver, new IntentFilter("pic"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        pic = preferences.getBoolean("pic", false);

        image = (AppCompatTextView) view.findViewById(R.id.image);
        editText = (AppCompatEditText) view.findViewById(R.id.name);
        button = (AppCompatButton) view.findViewById(R.id.button);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        user = preferences.getInt("user", 0);

        if(user == 4){
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().trim().equals(""))
                    getActivity().startService(new Intent(getContext(), service_setName.class).putExtra("name", editText.getText()));
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int p = metrics.widthPixels/2;

        image.setHeight(p);
        image.setWidth(p);

        editText.requestFocus();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            c = getContext().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (c != null && c.moveToNext()) {
                String columnValue = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                editText.setText(columnValue);
            }
            if (c != null)
                c.close();
        }

        setImage();

        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lists.getInstance().getConnection() != null && Lists.getInstance().getConnection().isConnected()) {
                    registerForContextMenu(view);
                    getActivity().openContextMenu(view);
                }
            }
        });*/
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //setImage();
            if(intent.getBooleanExtra("done", false)){
                preferences.edit().putString("name", editText.getText().toString()).apply();
                Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_LONG).show();
                if(user == 3) {
                    preferences.edit().putInt("user", 4).apply();
                    getContext().startActivity(new Intent(getContext(), Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
            else{
                Toast.makeText(getContext(), "Some problem occured", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void setImage() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int p = metrics.widthPixels/2;

        if(pic) {
            Picasso.with(getContext()).load(loadImageFromStorage()).resize(p, p).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    image.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thumb));
                    image.setText("M");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        else{
            image.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thumb));
            image.setText("M");
        }
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
                Bundle b = new Bundle();
                b.putBoolean("single", true);
                image_chooser ic = new image_chooser();
                ic.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity, ic, "image_chooser").addToBackStack(null).commit();
                return true;
            case R.id.accesscamera:
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                this.startActivityForResult(captureIntent, 1);
                return true;
            case R.id.removepic:
                image.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.thumb));
                image.setText("M");
                editor.putBoolean("pic", false).commit();
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
                byteArray = stream.toByteArray();
                break;
        }
    }

    private void performCrop() {
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
}
