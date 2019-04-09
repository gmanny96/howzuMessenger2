package howzu.gm.chats;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.*;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class chatU {

    static String getName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        String contactName = null;

        if (cursor != null && cursor.moveToFirst())
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        if (cursor != null && !cursor.isClosed()) cursor.close();

        return contactName;
    }

    static String getDateTime(Context context, Long timestamp, int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        SimpleDateFormat syear = new SimpleDateFormat("yyyy", Locale.getDefault());

        if (Integer.parseInt(syear.format(System.currentTimeMillis())) > Integer.parseInt(syear.format(new Date(timestamp * 1000)))) {
            return sdf.format(new Date(timestamp * 1000));
        }

        SimpleDateFormat sday = new SimpleDateFormat("dd", Locale.getDefault());

        int difference = Integer.parseInt(sday.format(System.currentTimeMillis())) - Integer.parseInt(sday.format(new Date(timestamp * 1000)));
        switch(difference){
            case 8:return "1 week ago";

            case 7:return "7 days ago";

            case 6:return "6 days ago";

            case 5:return "5 days ago";

            case 4:return "4 days ago";

            case 3:return "3 days ago";

            case 2:return "2 days ago";

            case 1:return "Yesterday";

            case 0:if(type == 0)
                return "Today";
            else
                return getTime(context, timestamp);

            default:
                SimpleDateFormat sdfmonth = new SimpleDateFormat("dd MMM", Locale.getDefault());
                return sdfmonth.format(new Date(timestamp * 1000));

        }
    }

    static String getTime(Context context, long timestamp) {
        SimpleDateFormat sdf;

        if (DateFormat.is24HourFormat(context)) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }

        return sdf.format(new Date(timestamp * 1000));
    }

    static String getChatTime(Context context, long timestamp){
        return getDateTime(context, timestamp, 0)+ " at " +getTime(context, timestamp);
    }
}
