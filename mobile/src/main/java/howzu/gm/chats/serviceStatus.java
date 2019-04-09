package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public class serviceStatus extends IntentService{

    VCard vCard;
    VCardManager manager;
    String status;

    public serviceStatus() {
        super("serviceStatus");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        status = intent.getStringExtra("status");
        manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());

        try {
            vCard = manager.loadVCard();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        vCard.setField("Status", status);

        try {
            manager.saveVCard(vCard);
            getSharedPreferences("data", MODE_PRIVATE).edit().putString("status", status).commit();
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
