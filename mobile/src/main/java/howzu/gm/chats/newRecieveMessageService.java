package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class newRecieveMessageService extends IntentService {

    bRecieveMessage alarm;

    public newRecieveMessageService() {
        super("newrecieveMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        alarm = new bRecieveMessage();
        ByteBuffer buffer = ByteBuffer.allocate(256);

        try {
            meLists.getInstance().getConnection().read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message = new String(buffer.array()).trim();

        if(!message.isEmpty()){
            startService(new Intent(this, newHandleNewDataService.class).putExtra("data", message));
            Log.e("new message aaya hai","ok ji");
        }

        if(meLists.getInstance().checkTime())
            alarm.SetAlarm(this);
        else
            alarm.SetAlarm5(this);
    }
}
