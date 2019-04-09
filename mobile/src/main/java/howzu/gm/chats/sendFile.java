package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;

public class sendFile extends IntentService{

    public sendFile() {
        super("sendfile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FileTransferManager manager = FileTransferManager.getInstanceFor(Lists.getInstance().getConnection());
        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer("romeo@montague.net");
        try {
            transfer.sendFile(new File(""), "");
        } catch (SmackException e) {
            e.printStackTrace();
        }

        byte[] profilepic = new byte[0];
        String s = Base64.encodeToString(profilepic);
    }
}
