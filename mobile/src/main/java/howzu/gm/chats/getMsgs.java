package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class getMsgs extends IntentService {

    Cursor cursor;

    public getMsgs() {
        super("getMsgs");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String username = intent.getStringExtra("username");
        String[] arg = {username};
        String[] projection = {Dbhelper.message, Dbhelper.time, Dbhelper.mode, Dbhelper.msgId};
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg");
        cursor = getContentResolver().query(uri, projection, Dbhelper.phonenumber + "= ? ", arg, null);

        List<chatPageData> data = new ArrayList<>();
        while (cursor!=null && cursor.moveToNext()) {
            chatPageData current = new chatPageData();
            current.setId(cursor.getString(cursor.getColumnIndex(Dbhelper.msgId)));
            current.setMessage(cursor.getString(cursor.getColumnIndex(Dbhelper.message)));
            current.setMode(cursor.getInt(cursor.getColumnIndex(Dbhelper.mode)));
            current.setTordtext(cursor.getString(cursor.getColumnIndex(Dbhelper.time)));
            String date = chatU.getChatTime(this, Long.valueOf(current.getTordtext()));
            if(cursor.isFirst()){
                chatPageData current1 = new chatPageData();
                current1.setTord(date);
                current1.setMode(7);
                data.add(current1);
            }
            else{
                cursor.moveToPrevious();
                String time2 = cursor.getString(cursor.getColumnIndex(Dbhelper.time));
                if(!date.equals(chatU.getChatTime(this, Long.valueOf(time2)))) {
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

        Lists.getInstance().setMsgList(data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("Chatmsgs_updated").putExtra("r", 0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursor!=null && !cursor.isClosed())
            cursor.close();
    }
}
