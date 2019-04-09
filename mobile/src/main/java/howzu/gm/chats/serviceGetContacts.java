package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class serviceGetContacts extends IntentService {

    Cursor cursor;

    public serviceGetContacts() {
        super("serviceGetContacts");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber, Dbhelper.status, Dbhelper.pic};
        cursor = getContentResolver().query(uri, projection, Dbhelper.contact + "= 1", null, Dbhelper.displayname + " ASC");

        List<contactsData> data = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            contactsData current = new contactsData();
            current.setNumber(cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber)));
            current.setStatus(cursor.getString(cursor.getColumnIndex(Dbhelper.status)));
            current.setPic(cursor.getInt(cursor.getColumnIndex(Dbhelper.pic)));
            current.setType(0);
            data.add(current);
        }

        Lists.getInstance().setContactList(data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("Contacts_updated"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed())
            cursor.close();
    }
}
