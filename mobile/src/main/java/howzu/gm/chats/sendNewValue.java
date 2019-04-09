package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class sendNewValue extends IntentService{

    public sendNewValue() {
        super("sendNewValue");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        JSONObject object = new JSONObject();
        try {
            object.put("type","update");
            object.put("what","");
            object.put("of","");
            object.put("value","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
