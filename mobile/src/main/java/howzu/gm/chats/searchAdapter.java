package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.myviewholder> {

    private LayoutInflater inflater;
    List<searchData> data = Collections.emptyList();
    int theme;
    Context context;
    private Clicklistener clicklistener;

    public searchAdapter(Context context, List<searchData> data, Clicklistener clicklistener) {
        this.clicklistener = clicklistener;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        theme = preferences.getInt("theme", 0);
    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int arg1) {
        return new myviewholder(inflater.inflate(R.layout.contact_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, final int arg1) {
        searchData current = data.get(arg1);
        holder.name.setText(current.name);

        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

        if(current.from == 1){
            if(current.bitmap == null){

            }
            else{

            }
        }
        else{
            if(current.bitmap == null){

            }
            else{

            }
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    class myviewholder extends RecyclerView.ViewHolder {

        AppCompatTextView name;
        RelativeLayout layout;

        public myviewholder(View arg0) {
            super(arg0);
            layout = (RelativeLayout) arg0.findViewById(R.id.layout);
            name = (AppCompatTextView) arg0.findViewById(R.id.name);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicklistener.rowClicked(v, data.get(getAdapterPosition()).number);
                }
            });
        }
    }

    public interface Clicklistener {
        public void rowClicked(View view, String number);
    }

    private File loadImageFromStorage(String num) {
        return new File(Environment.getExternalStorageDirectory() + "/Chats/Media/Profile Images", num);
    }
}
