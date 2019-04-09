package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.File;
import java.io.FileOutputStream;

public class serviceGetProfilephoto extends IntentService {

    VCardManager manager;
    VCard vCard;

    public serviceGetProfilephoto() {
        super("serviceGetProfilephoto");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projection = {Dbhelper.phonenumber};
        Cursor cursor = getContentResolver().query(uri, projection, Dbhelper.contact+ "= 1", null, null);

        manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());

        while(cursor!=null && cursor.moveToNext()) {
            String user = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.phonenumber));
            try {
                vCard = manager.loadVCard(user + "@aa-pc");
            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

            byte[] profilepic = vCard.getAvatar();
            Uri duri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
            ContentValues values = new ContentValues();

            if (profilepic != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(profilepic, 0, profilepic.length);
                if (saveToInternalSorage(bitmap, user))
                    values.put(Dbhelper.pic, 1);
            } else {
                values.put(Dbhelper.pic, 0);
            }

            getContentResolver().update(duri, values, user, null);
        }

        startService(new Intent(this, serviceGetContacts.class));

        if(cursor!=null && !cursor.isClosed())
            cursor.close();
    }

    private boolean saveToInternalSorage(Bitmap bitmapImage, String num) {
        File mypath = new File(Environment.getExternalStorageDirectory()+"/Chats/Media/Profile Images", num);

        try {
            FileOutputStream fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
