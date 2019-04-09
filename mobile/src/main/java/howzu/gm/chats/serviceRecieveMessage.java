package howzu.gm.chats;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class serviceRecieveMessage extends IntentService {

    int i;
    SharedPreferences preferences;
    ContentValues values;
    Uri uri;
    bRecieveMessage alarm;

    public serviceRecieveMessage() {
        super("serviceRecieveMessage");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        alarm = new bRecieveMessage();
        values = new ContentValues();
        uri = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/insert");
        preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        final StanzaFilter filter = new AndFilter(new StanzaTypeFilter(org.jivesoftware.smack.packet.Message.class));
        final StanzaListener packetListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("timer", 0).apply();

                Message message = (Message) packet;
                for (i = 0; i < packet.getFrom().length(); i++) {
                    char check = packet.getFrom().charAt(i);
                    if (check == '@') {
                        String username = packet.getFrom().substring(0, i);
                        String[] args={username};
                        Uri uri = Uri.parse(dataContentProvider.CONTENT_URI + "/data");
                        String[] projection = {Dbhelper.pic};
                        Cursor cursor = getContentResolver().query(uri, projection, Dbhelper.phonenumber + " = ? ", args, null);
                        if(cursor != null && cursor.moveToNext()){
                            Uri uuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/update");
                            ContentValues valuesc = new ContentValues();
                            valuesc.put(Dbhelper.time,  message.getSubject().substring(2));
                            valuesc.put(Dbhelper.chat, 1);
                            getContentResolver().update(uuri, valuesc, null, args);
                        }
                        else{
                            Uri iuri = Uri.parse(dataContentProvider.CONTENT_URI + "/data/insert");
                            ContentValues valuesm = new ContentValues();
                            valuesm.put(Dbhelper.phonenumber, username);
                            valuesm.put(Dbhelper.chat, 1);
                            valuesm.put(Dbhelper.contact, 0);
                            valuesm.put(Dbhelper.message, message.getBody());
                            valuesm.put(Dbhelper.time,  message.getSubject().substring(2));
                            getContentResolver().insert(iuri, valuesm);
                        }
                        if(cursor != null && !cursor.isClosed())
                            cursor.close();

                        Uri curi = Uri.parse(dataContentProvider.CONTENT_URI + "/chatmsg/insert");
                        values.put(Dbhelper.msgId, message.getThread());
                        values.put(Dbhelper.phonenumber, username);
                        values.put(Dbhelper.message, message.getBody());
                        values.put(Dbhelper.mode, 4);
                        if(message.getSubject().charAt(0) == '0')
                            values.put(Dbhelper.type, 0);
                        getContentResolver().insert(curi, values);

                        sendBroadcast(new Intent("Chatmsgs_updated").putExtra("r", 1).putExtra("from", username).putExtra("msg", message.getBody()).putExtra("time", message.getSubject().substring(2)));
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
                        }
                        break;
                    }
                }
            }
        };

        if (Lists.getInstance().getConnection() != null && Lists.getInstance().getConnection().isConnected()) {
            Lists.getInstance().getConnection().addAsyncStanzaListener(packetListener, filter);
            SharedPreferences.Editor editor = preferences.edit();
            int timer = preferences.getInt("timer", 0);
            switch (timer) {
                case 0:
                    editor.putInt("timer", 1);
                    break;
                case 1:
                    editor.putInt("timer", 2);
                    break;
                case 2:
                    editor.putInt("timer", 3);
                    break;
                case 3:
                    editor.putInt("timer", 4);
                    break;
                case 4:
                    editor.putInt("timer", 5);
                    break;
            }
            editor.apply();
        }
        int timer = preferences.getInt("timer", 0);
        switch (timer){
            case 5:alarm.SetAlarm5(this);
                break;
            default:alarm.SetAlarm(this);
                break;
        }
    }
}