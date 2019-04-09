package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class newSendDataService extends IntentService {

    public newSendDataService() {
        super("newSendDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("ff", "message send Data started");

        byte[] data = new byte[0];

        String dataText = intent.getStringExtra("data");

        try {
            data = dataText.getBytes("UTF-8");
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

        buffer.clear();
    }
}
