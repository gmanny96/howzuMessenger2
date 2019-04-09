package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class newContactRefreshResultService extends IntentService{

    JSONArray array;
    String number;

    public newContactRefreshResultService() {
        super("contactRefreshResult");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        //String USER = preferences.getString("user", null);

        String json = intent.getStringExtra("data");

        try {
            array = new JSONArray(json);
            number = array.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("new message checking","2 number hai ji");

        if(number.equals("+919560083681")){
            for(int i = 2;i<array.length();i++) {

                String[] args;
                try {
                    Log.e("ye contact h",array.getString(i));
                    args = new String[]{array.getString(i)};
                    String[] proj = {Dbhelper.phonenumber};
                    Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
                    Cursor cursor = getContentResolver().query(uri, proj, Dbhelper.phonenumber + " = ? ", args, null);
                    if (cursor == null || !cursor.moveToNext()) {
                        Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert");
                        ContentValues values = new ContentValues();
                        values.put(Dbhelper.phonenumber, array.getString(i));
                        //values.put(Dbhelper.onM, 1);
                        //values.put(Dbhelper.displayname, name);
                        values.put(Dbhelper.contact, 2);

                        getContentResolver().insert(iuri, values);
                        Log.d("new", array.getString(i));
                    }
                    else{
                        Log.e("ye database me h",array.getString(i));
                        Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                        ContentValues values = new ContentValues();
                        //values.put(Dbhelper.onM, 1);
                        values.put(Dbhelper.contact, 2);
                        getContentResolver().update(uuri, values, null, args);
                    }

                    if(cursor!=null && !cursor.isClosed())
                        cursor.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("Contacts_updated"));
    }
}
