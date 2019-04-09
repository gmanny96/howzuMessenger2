package howzu.gm.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class settingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    Context context;
    int width;

    public settingsAdapter(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == 2) {
            return 0;
        }
        else
            return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:return new headerbutton(inflater.inflate(R.layout.header_button, parent, false));
            default:return new header(inflater.inflate(R.layout.header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof headerbutton)
        {
            headerbutton h = (headerbutton) holder;
            switch (position){
                case 0:
                    h.text.setText("Notification");
                    break;
                case 2:
                    h.text.setText("Sound");
                    break;
            }
        }
        else {
            header h1 = (header) holder;
            switch (position){
                case 1:
                    break;
                case 3:
                    h1.text.setText("LED");
                    break;

            }
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class headerbutton extends RecyclerView.ViewHolder{

        AppCompatTextView text;
        SwitchCompat switchCompat;

        public headerbutton(View itemView) {
            super(itemView);
            text= (AppCompatTextView) itemView.findViewById(R.id.text);
            switchCompat= (SwitchCompat) itemView.findViewById(R.id.swit);
        }
    }

    class header extends RecyclerView.ViewHolder{

        AppCompatTextView text;
        public header(View itemView) {
            super(itemView);
            text= (AppCompatTextView) itemView.findViewById(R.id.text);
        }
    }
}
