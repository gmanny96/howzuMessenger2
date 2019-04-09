package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smackx.filetransfer.FileTransferManager;

public class getFile extends IntentService{

    public getFile() {
        super("getFile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FileTransferManager manager = FileTransferManager.getInstanceFor(Lists.getInstance().getConnection());
        
    }
}
