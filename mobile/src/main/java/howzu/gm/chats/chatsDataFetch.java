package howzu.gm.chats;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class chatsDataFetch extends AsyncTaskLoader<List<chatsData>>{

    Cursor cursor;
    Context context;

    chatsDataFetch(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<chatsData> loadInBackground() {
        Log.d("ff", "chatsdatafetch lod");
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber, Dbhelper.time, Dbhelper.pic};
        cursor = context.getContentResolver().query(uri, projection, Dbhelper.chat + " != 0 ", null, Dbhelper.time + " DESC");

        List<chatsData> data = new ArrayList<>();

        /*while(cursor != null && cursor.moveToNext()){
            chatsData current = new chatsData();
            current.number = cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber));
            Log.e("chat:", current.number);
            String time = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
            current.pic = cursor.getInt(cursor.getColumnIndex(Dbhelper.pic));
            current.time = chatU.getDateTime(context, Long.valueOf(time), 1);
            current.type = 1;
            if(cursor.isFirst()){
                chatsData current1 = new chatsData();
                current1.type = 0;
                current1.time = chatU.getDateTime(context, Long.valueOf(time), 0);
                data.add(current1);
            }
            else{
                cursor.moveToPrevious();
                String time2 = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
                String timet = chatU.getDateTime(context, Long.valueOf(time2),0);
                if(!chatU.getDateTime(context, Long.valueOf(time), 0).equals(timet)) {
                    chatsData current2 = new chatsData();
                    current2.time = timet;
                    current2.type = 0;
                    data.add(current2);
                }
                cursor.moveToNext();
            }
            data.add(current);
        }*/

        while(cursor != null && cursor.moveToNext()){
            chatsData current = new chatsData();
            current.setNumber(cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber)));
            Log.e("chat:", current.getNumber());
            String time = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
            current.setPic(cursor.getInt(cursor.getColumnIndex(Dbhelper.pic)));
            current.setTime(chatU.getDateTime(context, Long.valueOf(time), 1));
            current.setType(1);
            /*if(cursor.isFirst()){
                chatsData current1 = new chatsData();
                current1.type = 0;
                current1.time = chatU.getDateTime(context, Long.valueOf(time), 0);
                data.add(current1);
            }
            else{
                cursor.moveToPrevious();
                String time2 = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
                String timet = chatU.getDateTime(context, Long.valueOf(time2),0);
                if(!chatU.getDateTime(context, Long.valueOf(time), 0).equals(timet)) {
                    chatsData current2 = new chatsData();
                    current2.time = timet;
                    current2.type = 0;
                    data.add(current2);
                }
                cursor.moveToNext();
            }*/
            data.add(current);
        }

        if(data.size()>0)
            return data;
        else
            return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
