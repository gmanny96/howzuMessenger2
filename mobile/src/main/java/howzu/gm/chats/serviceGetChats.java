package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class serviceGetChats extends IntentService {

    Cursor cursor;

    public serviceGetChats() {
        super("serviceGetChats");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber, Dbhelper.time, Dbhelper.pic};
        cursor = getContentResolver().query(uri, projection, Dbhelper.chat + " != 0 ", null, Dbhelper.time + " DESC");

        List<chatsData> data = new ArrayList<>();

        while(cursor != null && cursor.moveToNext()){
            chatsData current = new chatsData();
            current.setNumber(cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber)));
            String time = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
            current.setPic(cursor.getInt(cursor.getColumnIndex(Dbhelper.pic)));
            current.setTime(chatU.getDateTime(this, Long.valueOf(time), 1));
            data.add(current);
        }

        Lists.getInstance().setChatList(data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("Chats_updated").putExtra("msg", false));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
    }
}
