package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;

public class serviceGetLastSeen extends IntentService{

    LastActivity activity;
    public serviceGetLastSeen() {
        super("service_getLastSeen");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        LastActivityManager activityManager = LastActivityManager.getInstanceFor(Lists.getInstance().getConnection());
        try {
            activity =  activityManager.getLastActivity("");
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        long ff = activity.getIdleTime();
    }
}
