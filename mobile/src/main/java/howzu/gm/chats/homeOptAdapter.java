package howzu.gm.chats;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

class homeOptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    workToDo callback;

    homeOptAdapter(Context context, workToDo callBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.callback = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new homeOptAdapter.opt(inflater.inflate(R.layout.options, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class opt extends RecyclerView.ViewHolder {

        FloatingActionButton optf;

        opt(View view) {
            super(view);
            //layout = (RelativeLayout) view.findViewById(R.id.layout);
            optf = (FloatingActionButton) view.findViewById(R.id.opt);
        }
    }
}
