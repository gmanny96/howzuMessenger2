package howzu.gm.chats;

import android.app.IntentService;
import android.content.Intent;

import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

public class recieveFile extends IntentService {

    public recieveFile() {
        super("recievefile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FileTransferManager manager = FileTransferManager.getInstanceFor(Lists.getInstance().getConnection());

        manager.addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                request.getFileName();
                request.getMimeType();
                request.getRequestor();
            }
        });
    }
}
