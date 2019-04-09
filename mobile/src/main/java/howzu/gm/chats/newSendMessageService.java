package howzu.gm.chats;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class newSendMessageService extends IntentService{

    //message
    //broadcast
    //call
    //video call
    //call/video call data
    //update
    //search
    //new group
    //grp message

    public newSendMessageService() {
        super("newSendMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String USER = preferences.getString("user", null);

        Log.e("ff", "message send service started");

        JSONArray array = new JSONArray();
        array.put(3);//type in int
        array.put("Id");
        array.put(USER);
        array.put(intent.getStringExtra("To"));
        array.put(intent.getStringExtra("Message"));
        array.put(intent.getStringExtra("Time_written"));
        Long send = System.currentTimeMillis() / 1000;
        array.put(send);

        startService(new Intent(this, newSendDataService.class).putExtra("data", array.toString()));

        /*byte[] data = new byte[0];

        try {
            data = array.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        try {
            meLists.getInstance().getConnection().write(buffer);
            Log.e("ff", "message send done");
            //update sendtime
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.clear();*/
    }
}
