package howzu.gm.chats;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

public class newMessageHandlerService extends IntentService {

    JSONArray array;
    //String number;
    //ContentValues values;
    //Uri uri;

    public newMessageHandlerService() {
        super("newMessageHandlerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String json = intent.getStringExtra("data");

        //uri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/insert");

        try {
            array = new JSONArray(json);
            //number = array.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (array != null) {
            try {
                String from = array.getString(2); //assume sender number

                String[] args = {from};
                Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
                String[] projection = {Dbhelper.pic};
                Cursor cursor = getContentResolver().query(uri, projection, Dbhelper.phonenumber + " = ? ", args, null);
                if (cursor != null && cursor.moveToNext()) {
                    Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                    ContentValues valuesc = new ContentValues();
                    //add recieved time
                    valuesc.put(Dbhelper.time, System.currentTimeMillis() / 1000);
                    valuesc.put(Dbhelper.chat, 1);
                    getContentResolver().update(uuri, valuesc, null, args);
                } else {
                    Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert");
                    ContentValues valuesm = new ContentValues();
                    valuesm.put(Dbhelper.phonenumber, from);
                    valuesm.put(Dbhelper.name, "");
                    valuesm.put(Dbhelper.chat, 1);
                    valuesm.put(Dbhelper.contact, 0);
                    //valuesm.put(Dbhelper.message, array.getString(3));
                    valuesm.put(Dbhelper.time, System.currentTimeMillis() / 1000);
                    getContentResolver().insert(iuri, valuesm);
                }
                if (cursor != null && !cursor.isClosed())
                    cursor.close();

                Uri curi = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/insert");
                ContentValues values = new ContentValues();
                values.put(Dbhelper.msgId, array.getString(1));
                values.put(Dbhelper.phonenumber, from);
                values.put(Dbhelper.message, array.getString(4));
                values.put(Dbhelper.mode, 4);
                //values.put(Dbhelper.type, 0);
                getContentResolver().insert(curi, values);

                /*sendBroadcast(new Intent("Chatmsgs_updated").putExtra("r", 1).putExtra("from", username).putExtra("msg", message.getBody()).putExtra("time", message.getSubject().substring(2)));
                sendBroadcast(new Intent("Chats_updated").putExtra("msg", true).putExtra("from", username).putExtra("msg", message.getBody()).putExtra("time", message.getSubject().substring(2)));
                String Page = preferences.getString("Page", "none");
                if(!Page.equals("chats")||!Page.equals(username)){
                    NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext()).setContentTitle(chatU.getName(getApplicationContext(), username)).setContentText(message.getBody()).setSmallIcon(R.drawable.message);
                    notification.setVibrate(new long[] { 1000});
                    notification.setLights(Color.RED, 3000, 3000);
                    notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.FLAG_ONLY_ALERT_ONCE);
                    notification.setAutoCancel(true);
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle().setBigContentTitle("New Messages").addLine(username+":"+message.getBody());
                    notification.setStyle(inboxStyle);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(Activity.class);
                    stackBuilder.addNextIntent(new Intent(getApplicationContext(), Activity.class));
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, notification.build());
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
