package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

public class serviceSendMessage extends IntentService {

    public serviceSendMessage() {
        super("serviceSendMessage");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /*
        text = 0
        File = 1
        broadcast message = 2
        invites = 3
        acknowledgements = 4
        notifications = 5
        */

        if (Lists.getInstance().getConnection() != null){
            Message msgtosend = new Message();
            msgtosend.setBody(intent.getStringExtra("Message"));
            msgtosend.setThread(intent.getStringExtra("Id"));
            msgtosend.setTo(intent.getStringExtra("To") + "@aa-pc");
            msgtosend.setSubject("0,"+String.valueOf(System.currentTimeMillis() / 1000));

            try {
                Lists.getInstance().getConnection().sendStanza(msgtosend);
                Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/update");
                String[] args = {intent.getStringExtra("Id")};
                ContentValues valuesc = new ContentValues();
                valuesc.put(Dbhelper.mode,  1);
                getContentResolver().update(uuri, valuesc, null, args);
            } catch (SmackException.NotConnectedException e) {}
        } else {
            stopSelf();
        }
    }
}