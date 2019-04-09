package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class statusAdapter extends RecyclerView.Adapter<statusAdapter.Myviewholder> {

    private LayoutInflater inflater;
    List<statusData> data= Collections.emptyList();
    int theme;
    Context context;

    public statusAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }
    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(inflater.inflate(R.layout.status_onerow, parent, false));
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {
        statusData current=data.get(position);

        holder.text.setText(current.text);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Myviewholder extends RecyclerView.ViewHolder{

        AppCompatTextView text;
        AppCompatImageButton button;

        public Myviewholder(View itemView) {
            super(itemView);
            text = (AppCompatTextView) itemView.findViewById(R.id.text);
            button = (AppCompatImageButton) itemView.findViewById(R.id.button);
        }
    }
}
