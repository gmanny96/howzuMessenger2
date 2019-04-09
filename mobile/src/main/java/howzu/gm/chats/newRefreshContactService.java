package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class newRefreshContactService extends IntentService{

    String name, phoneNumber;

    public newRefreshContactService() {
        super("meRefreshContacts");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //SharedPreferences preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        //String USER = preferences.getString("user", null);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        JSONArray array = new JSONArray();
        array.put(2).put("+919560083681");
        while (phones != null && phones.moveToNext()) {
            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replace(" ", "");

            String[] args = {phoneNumber};
            String[] proj = {Dbhelper.phonenumber, Dbhelper.contact, Dbhelper.displayname};
            Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
            Cursor cursor = getContentResolver().query(uri, proj, Dbhelper.phonenumber+ " = ? ", args, null);
            if(cursor == null || !cursor.moveToNext())
            {
                Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert");
                ContentValues values = new ContentValues();
                values.put(Dbhelper.phonenumber, phoneNumber);
                values.put(Dbhelper.displayname, name);
                values.put(Dbhelper.contact, 1);
                //values.put(Dbhelper.onM, 0);
                getContentResolver().insert(iuri, values);
                Log.d("new",phoneNumber);
            }
            else {
                int contact = cursor.getInt(cursor.getColumnIndex(Dbhelper.contact));
                if (contact == 0) {
                    Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                    ContentValues values = new ContentValues();
                    values.put(Dbhelper.contact, 1);
                    getContentResolver().update(uuri, values, phoneNumber, null);
                }
                String displayname = cursor.getString(cursor.getColumnIndex(Dbhelper.displayname));
                if (!name.equals(displayname)) {
                    Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                    ContentValues values = new ContentValues();
                    values.put(Dbhelper.displayname, name);
                    getContentResolver().update(iuri, values, phoneNumber, null);
                }
            }
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            try {
                if(!array.getString(array.length()-1).equals(phoneNumber)) {
                    Log.e("phonenumber putting :", phoneNumber);
                    array.put(phoneNumber);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(phones != null){
            phones.close();
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("Service").putExtra("data",array.toString()));

        //startService(new Intent(this, newSendDataService.class).putExtra("data", array.toString()));

        /*byte[] data = new byte[0];

        try {
            data = array.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        try {
            meLists.getInstance().getConnection().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.clear();*/
    }
}
