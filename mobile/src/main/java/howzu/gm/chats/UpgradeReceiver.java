package howzu.gm.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class UpgradeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        /*int user = preferences.getInt("user", 0);

        if(user == 4){
            context.startService(new Intent().setClass(context, serviceLogin.class));
        }*/
    }
}
