package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public class service_setName extends IntentService {

    SharedPreferences preferences;
    String username;
    VCardManager manager;
    VCard vCard;
    String name;
    Boolean done = false;

    public service_setName() {
        super("service_setName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        username = preferences.getString("username", "");

        manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());
        name = intent.getStringExtra("name");

        vCard = new VCard();
        try {
            vCard = manager.loadVCard();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        vCard.setField("name", name);

        try {
            manager.saveVCard(vCard);
            done = true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        if (done) {
            preferences.edit().putString("name", name).apply();
            Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show();
            this.startActivity(new Intent(this, Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_LONG).show();
        }
    }
}
