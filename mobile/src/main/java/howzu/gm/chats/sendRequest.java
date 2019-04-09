package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class sendRequest extends IntentService{

    public sendRequest() {
        super("sendRequest");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        JSONObject object = new JSONObject();
        try {
            object.put("type", "request");
            object.put("what", "");
            object.put("of","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
