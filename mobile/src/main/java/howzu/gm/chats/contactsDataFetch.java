package howzu.gm.chats;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class contactsDataFetch extends AsyncTaskLoader<List<contactsData>> {

    Cursor cursor;
    Context context;

    contactsDataFetch(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<contactsData> loadInBackground() {
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber, Dbhelper.status, Dbhelper.pic};
        cursor = context.getContentResolver().query(uri, projection, Dbhelper.contact + " = 2 ", null, Dbhelper.displayname + " ASC");

        Log.e("ye chl raha hai","ok");
        List<contactsData> data = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            contactsData current = new contactsData();
            current.setNumber(cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber)));
            Log.e("ye contact list me", current.getNumber());
            current.setStatus(cursor.getString(cursor.getColumnIndex(Dbhelper.status)));
            current.setPic(cursor.getInt(cursor.getColumnIndex(Dbhelper.pic)));
            current.setType(0);
            data.add(current);
        }
        /*for(int i = 0;i<2;i++){
            contactsData current = new contactsData();
            current.type = 2;
            switch (i) {
                case 0:
                    current.number = "Refresh Contacts";
                    break;
                default:
                    current.number = "Invite Contacts";
                    break;
            }
            data.add(current);
        }*/
        return data;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
