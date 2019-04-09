package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.File;
import java.io.FileOutputStream;

public class serviceChangeProfilePic extends IntentService{

    VCardManager manager;
    VCard vCard;
    SharedPreferences preferences;
    String username;

    public serviceChangeProfilePic() {
        super("serviceChangeProfilePic");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        username = preferences.getString("username", "");

        manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());
        byte[] profilepic = intent.getByteArrayExtra("profilephoto");

        vCard = new VCard();
        try {
            vCard = manager.loadVCard();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        vCard.setAvatar(profilepic);

        try {
            manager.saveVCard(vCard);
            Bitmap bitmap = BitmapFactory.decodeByteArray(profilepic , 0, profilepic.length);
            if(saveToInternalSorage(bitmap))
                preferences.edit().putInt("pic", 1).commit();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        sendBroadcast(new Intent("pic"));
    }

    private boolean saveToInternalSorage(Bitmap bitmapImage) {
        File mypath = new File(Environment.getExternalStorageDirectory()+"/Chats/Media/My Profile Images", "+919560083681");
        try {
            FileOutputStream fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
