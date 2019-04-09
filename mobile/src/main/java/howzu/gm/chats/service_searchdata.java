package howzu.gm.chats;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;

import java.util.ArrayList;
import java.util.List;

public class service_searchdata extends IntentService {

    Cursor cursor;
    SharedPreferences preferences;

    public service_searchdata() {
        super("service_searchdata");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String searchword = intent.getStringExtra("searchword");
        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
        String[] projections = {Dbhelper.phonenumber};
        cursor = getContentResolver().query(uri, projections, Dbhelper.phonenumber + " LIKE '%" + searchword + "%' OR " + Dbhelper.displayname + " LIKE '%" + searchword + "%'", null, null);

        List<searchData> data = new ArrayList<>();

        while (cursor != null && cursor.moveToNext()) {
            searchData current = new searchData();
            current.number = cursor.getString(cursor.getColumnIndex(Dbhelper.phonenumber));
            current.name = chatU.getName(this, current.number);
            current.from = 0;
            data.add(current);
        }
        preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        Boolean global_search = preferences.getBoolean("global_search", false);
        if (global_search && Lists.getInstance().getConnection()!=null && Lists.getInstance().getConnection().isConnected()) {
            UserSearchManager userSearchManager = new UserSearchManager(Lists.getInstance().getConnection());
            Form searchForm = null;
            try {
                searchForm = userSearchManager.getSearchForm("search." + Lists.getInstance().getConnection().getServiceName());
            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Name", true);
            answerForm.setAnswer("search", searchword);
            ReportedData resultData = null;
            try {
                resultData = userSearchManager.getSearchResults(answerForm, "search." + Lists.getInstance().getConnection().getServiceName());
            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
            List<ReportedData.Row> names = resultData.getRows();
            for (int i = 0; i < names.size(); i++) {
                String str = names.get(i).getValues("Username").toString();
                str = str.replace("[", "");
                str = str.replace("]", "");
                VCardManager manager = VCardManager.getInstanceFor(Lists.getInstance().getConnection());
                try {
                    VCard vCard = manager.loadVCard(str + "@aa-pc");
                    String name = vCard.getFirstName();
                    byte[] profilepic = vCard.getAvatar();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(profilepic, 100, profilepic.length);
                    searchData current = new searchData();
                    current.number = str;
                    current.name = name;
                    current.bitmap = bitmap;
                    current.from = 1;
                    data.add(current);
                } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
        Lists.getInstance().setSearchList(data);
        sendBroadcast(new Intent("search_updated"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed())
            cursor.close();
    }
}
