package howzu.gm.chats;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.io.File;

public class imageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    Uri[] itemList;
    private LayoutInflater inflater;
    private Clicklistener clicklistener;

    public imageAdapter(Context context, Uri[] itemList, int count, Clicklistener clicklistener){
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
        this.itemList = new Uri[count];
        this.itemList = itemList;
        this.clicklistener = clicklistener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new image(inflater.inflate(R.layout.image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        image i = (image) holder;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int p = metrics.widthPixels/3;
        i.image.setMaxHeight(p);
        i.image.setMaxWidth(p);
        File file = new File(itemList[position].getPath());
        Picasso.with(context).load(file).centerCrop().resize(p, p).into(i.image);
    }

    @Override
    public int getItemCount() {
        if(itemList!=null)
            return itemList.length;
        else
            return 0;
    }

    class image extends RecyclerView.ViewHolder {

        AppCompatImageView image;
        AppCompatCheckBox checkBox;

        public image(View arg0) {
            super(arg0);
            checkBox = (AppCompatCheckBox) arg0.findViewById(R.id.check);
            image = (AppCompatImageView) arg0.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clicklistener!=null)
                        clicklistener.rowClicked(getAdapterPosition(), checkBox);
                }
            });
        }
    }
    public interface Clicklistener {
        void rowClicked(int p, AppCompatCheckBox cb);
    }
}
