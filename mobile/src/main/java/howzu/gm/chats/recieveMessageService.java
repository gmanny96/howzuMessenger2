package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class recieveMessageService extends IntentService{

    BufferedReader br;
    String newMsg = null;
    bRecieveMessage alarm;

    public recieveMessageService() {
        super("recieveMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /*alarm = new bRecieveMessage();

        try {
            br = new BufferedReader(new InputStreamReader(meLists.getInstance().getConnection().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            newMsg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(newMsg != null){
            startService(new Intent(this, meHandleNewData.class).putExtra("newMsg", newMsg));
        }

        alarm.SetAlarm(this);*/
    }
}
