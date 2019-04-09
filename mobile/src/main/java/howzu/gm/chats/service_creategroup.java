package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class service_creategroup extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public service_creategroup(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(Lists.getInstance().getConnection());

        MultiUserChat muc = manager.getMultiUserChat(Lists.getInstance().getConnection().getUser());

        try {
            muc.create("");
        } catch (XMPPException.XMPPErrorException | SmackException e) {
            e.printStackTrace();
        }

        try {
            muc.sendConfigurationForm(new Form(DataForm.Type.submit));
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
