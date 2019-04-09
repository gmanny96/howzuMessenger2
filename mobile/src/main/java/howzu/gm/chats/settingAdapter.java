package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

class settingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    Context context;
    private Clicklistener clicklistener;
    //int width;

    settingAdapter(Context context, Clicklistener clicklistener, int width) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.clicklistener = clicklistener;
        //this.width = width;
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    /*@Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else
            return 1;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if(viewType == 0)
            return new account(inflater.inflate(R.layout.account, parent, false));
        else*/
            return new other(inflater.inflate(R.layout.header, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*if(position == 0){
            account h = (account) holder;
            //h.dp.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.on_m));
            //h.dp.setText("M");
            h.dp.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.grey500), PorterDuff.Mode.MULTIPLY);
        }
        else {*/
            other h = (other) holder;
            switch (position) {
                case 0:
                    h.text.setText("Status");
                    break;
                case 1:
                    h.text.setText("Notification");
                    break;
                case 2:
                    h.text.setText("Change theme");
                    break;
                case 3:
                    h.text.setText("Invite Contacts");
            //}
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    /*class account extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        AppCompatTextView text, dp;

        public account(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            text = (AppCompatTextView) itemView.findViewById(R.id.name);
            dp = (AppCompatTextView) itemView.findViewById(R.id.image);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clicklistener != null)
                        clicklistener.rowClicked(v, getAdapterPosition());
                }
            });
        }
    }*/

    class other extends RecyclerView.ViewHolder {

        AppCompatTextView text;

        public other(View itemView) {
            super(itemView);
            text = (AppCompatTextView) itemView.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clicklistener != null)
                        clicklistener.rowClicked(v, getAdapterPosition());
                }
            });
        }
    }

    public interface Clicklistener {
        void rowClicked(View view, int p);
    }
}
