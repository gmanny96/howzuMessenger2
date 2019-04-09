package howzu.gm.chats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class mypagelist extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Clicklistener clicklistener;
    Context context;
    private LayoutInflater inflater;

    public mypagelist(Context context, Clicklistener clicklistener) {
        this.clicklistener = clicklistener;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface Clicklistener {
        public void rowClicked(View view, String number);
    }
}
