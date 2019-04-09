package howzu.gm.chats;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.List;

class chatPageDataFetch extends AsyncTaskLoader<List<chatPageData>> {

    Cursor cursor;
    String number;
    Context context;

    chatPageDataFetch(Context context, String number) {
        super(context);
        this.context = context;
        this.number = number;
    }

    @Override
    public List<chatPageData> loadInBackground() {
        String[] arg = {number};
        String[] projection = {Dbhelper.message, Dbhelper.time, Dbhelper.mode, Dbhelper.msgId};
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg");
        cursor = context.getContentResolver().query(uri, projection, Dbhelper.phonenumber + "= ? ", arg, null);

        List<chatPageData> data = new ArrayList<>();
        while (cursor!=null && cursor.moveToNext()) {
            chatPageData current = new chatPageData();
            current.setId(cursor.getString(cursor.getColumnIndex(Dbhelper.msgId)));
            current.setMessage(cursor.getString(cursor.getColumnIndex(Dbhelper.message)));
            current.setMode(cursor.getInt(cursor.getColumnIndex(Dbhelper.mode)));
            current.setTordtext(cursor.getString(cursor.getColumnIndex(Dbhelper.time)));
            String date = chatU.getDateTime(context, Long.valueOf(current.getTordtext()), 0);
            current.setTord(chatU.getTime(context, Long.valueOf(current.getTordtext())));
            if(cursor.isFirst()){
                chatPageData current1 = new chatPageData();
                current1.setTord(date);
                current1.setMode(7);
                data.add(current1);
            }
            else{
                cursor.moveToPrevious();
                String time2 = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
                if(!date.equals(chatU.getDateTime(context, Long.valueOf(time2),0))) {
                    chatPageData current2 = new chatPageData();
                    current2.setTord(date);
                    current2.setMode(7);
                    data.add(current2);
                }
                cursor.moveToNext();
            }
            data.add(current);
            /*if(cursor.isLast()){
                chatPageData current3 = new chatPageData();
                current3.message = chatU.getMsgtime(this, System.currentTimeMillis() / 1000);
                current3.mode = 7;
                data.add(current3);
            }*/
        }
        return data;
    }
}
