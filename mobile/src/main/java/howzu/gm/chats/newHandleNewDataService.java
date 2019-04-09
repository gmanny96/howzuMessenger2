package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class newHandleNewDataService extends IntentService{

    public newHandleNewDataService() {
        super("meHandleNewData");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String json = intent.getStringExtra("data");

        try {
            JSONArray array = new JSONArray(json);
            int type = array.getInt(0);

            switch (type){
                case 2:
                    startService(new Intent(this, newContactRefreshResultService.class).putExtra("data", json));
                    Log.e("new message aaya hai","2 number hai ji");
                    break;
                case 1:
                    break;
                case 3:
                    startService(new Intent(this, newMessageHandlerService.class).putExtra("data", json));
                    Log.e("new message aaya hai","3 number hai ji");
                    break;
                default:
                    //jst for testing purpose
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
