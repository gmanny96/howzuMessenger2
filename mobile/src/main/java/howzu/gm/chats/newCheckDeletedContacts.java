package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class newCheckDeletedContacts extends IntentService{

    public newCheckDeletedContacts() {
        super("newCheckDeletedContacts");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        Uri duri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
        String[] projection = {Dbhelper.phonenumber, Dbhelper.displayname};
        Cursor cursor = getContentResolver().query(uri, projection, Dbhelper.contact+ "= 1", null, null);

        while(cursor!=null && cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            String phoneNumber = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.phonenumber));
            Uri curi = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            ContentResolver contentResolver = getContentResolver();
            Cursor contactLookup = contentResolver.query(curi, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            if(contactLookup != null && contactLookup.moveToNext()) {
                String nickname = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.displayname));
                String name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                if(!nickname.equals(name))
                {
                    values.put(Dbhelper.displayname, nickname);
                    getContentResolver().update(duri, values, phoneNumber, null);
                }
            }
            else{
                values.put(Dbhelper.contact, 0);
                getContentResolver().update(duri, values, phoneNumber, null);
            }
            if (contactLookup != null)
                contactLookup.close();
        }
        if(cursor!=null && !cursor.isClosed())
            cursor.close();
    }
}
