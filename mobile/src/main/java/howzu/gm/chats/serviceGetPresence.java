package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public class serviceGetPresence extends IntentService {

    VCard vCard;
    VCardManager manager;

    public serviceGetPresence() {
        super("serviceGetPresence");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber};

        Cursor cursor = getContentResolver().query(uri, projection, Dbhelper.contact+ "= 1", null, null);

        while(cursor!=null && cursor.moveToNext()) {
            String user = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.phonenumber));
            manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());
            try {
                vCard = manager.loadVCard(user);
            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
            //String status = vCard.getField("Status");
            Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
            ContentValues values = new ContentValues();
            values.put(Dbhelper.status, "");
            getContentResolver().update(uuri, values, user, null);
        }
        startService(new Intent(this, serviceGetProfilephoto.class));

        if(cursor!=null && !cursor.isClosed())
            cursor.close();
    }
}

