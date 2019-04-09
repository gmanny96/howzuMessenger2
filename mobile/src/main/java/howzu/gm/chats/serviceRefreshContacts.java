package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.util.List;

public class serviceRefreshContacts extends IntentService {
    String str, name, phoneNumber;

    public serviceRefreshContacts() {
        super("serviceRefreshContacts");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            refresh();
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    public void refresh() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones != null && phones.moveToNext()) {
            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replace(" ", "");
            if(Lists.getInstance().getConnection() != null && Lists.getInstance().getConnection().isConnected()) {
                UserSearchManager userSearchManager = new UserSearchManager(Lists.getInstance().getConnection());
                Form searchForm = userSearchManager.getSearchForm("search." + Lists.getInstance().getConnection().getServiceName());
                Form answerForm = searchForm.createAnswerForm();
                answerForm.setAnswer("Username", true);
                answerForm.setAnswer("search", phoneNumber);
                ReportedData resultData = userSearchManager.getSearchResults(answerForm, "search." + Lists.getInstance().getConnection().getServiceName());
                List<ReportedData.Row> data = resultData.getRows();
                for (int i = 0; i < data.size(); i++) {
                    str = data.get(i).getValues("Username").toString();
                    str = str.replace("[", "");
                    str = str.replace("]", "");
                    String[] args = {str};
                    String[] proj = {Dbhelper.phonenumber, Dbhelper.contact, Dbhelper.displayname};
                    Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
                    Cursor cursor = getContentResolver().query(uri, proj, Dbhelper.phonenumber+ " = ? ", args, null);
                    if(cursor == null || !cursor.moveToNext())
                    {
                        Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert");
                        ContentValues values = new ContentValues();
                        values.put(Dbhelper.phonenumber, str);
                        values.put(Dbhelper.displayname, name);
                        values.put(Dbhelper.contact, 1);
                        getContentResolver().insert(iuri, values);
                        Log.d("new",str);
                    }
                    else {
                        String contact = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.contact));
                        if (!contact.equals("y")) {
                            Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                            ContentValues values = new ContentValues();
                            values.put(Dbhelper.contact, 1);
                            getContentResolver().update(uuri, values, str, null);
                        }
                        String displayname = cursor.getColumnName(cursor.getColumnIndex(Dbhelper.displayname));
                        if (!name.equals(displayname)) {
                            Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                            ContentValues values = new ContentValues();
                            values.put(Dbhelper.displayname, name);
                            getContentResolver().update(iuri, values, phoneNumber, null);
                        }
                    }
                    if(cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }
        if(phones != null){
            phones.close();
        }
        startService(new Intent(this, serviceCheckContacts.class));
    }
}