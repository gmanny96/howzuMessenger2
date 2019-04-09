package howzu.gm.chats;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class get_musicadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    Cursor cursor;
    private LayoutInflater inflater;

    public get_musicadapter(Context context, Cursor cursor){
        this.cursor = cursor;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cursor.moveToNext();
    }

    @Override
    public int getItemCount() {
        if(cursor!=null)
            return cursor.getCount();
        else
            return 0;
    }

    public void closeCursor() {
        if(cursor!=null && !cursor.isClosed())
            cursor.close();
    }
}
