package howzu.gm.chats;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class imageCropper extends Fragment{

    View view;
    Toolbar toolbar;
    CropImageView imageView;
    String path;

    public imageCropper(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.imagecropper, container, false);
        path = getArguments().getString("path");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Crop image");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue500), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        imageView = (CropImageView) view.findViewById(R.id.cropimage);
        Picasso.with(getContext()).load(new File(path)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(getContext(), "Some problem occured while loading file", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        imageView.setFixedAspectRatio(true);
        imageView.setAspectRatio(1, 1);
    }
}
