package howzu.gm.chats;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dataBaseAdapter {

    Dbhelper helper;

    public dataBaseAdapter(Context context){
        helper = new Dbhelper(context);
    }

    public void insertChat(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //add recieved time
        values.put(Dbhelper.time, System.currentTimeMillis() / 1000);
        values.put(Dbhelper.chat, 1);
        db.insert(Dbhelper.TABLE_NAME, null, values);
    }

    public void insertMsg(String from, String msg){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Dbhelper.phonenumber, from);
        values.put(Dbhelper.name, "");
        values.put(Dbhelper.chat, 1);
        values.put(Dbhelper.contact, 0);
        //values.put(Dbhelper.message, array.getString(3));
        values.put(Dbhelper.time, System.currentTimeMillis() / 1000);
        db.insert(Dbhelper.TABLE_NAME1, null, values);
    }

    public void getChats(){

    }

    public void getMsgs(){

    }

    public void updateChat(){

    }

    public void updateMsg(){

    }

    public void deleteChat(){

    }

    public void deleteMsg(){

    }

    public class Dbhelper extends SQLiteOpenHelper {

        //CHATMSGS
    /*Msgs mode
    * want to send = 0
    * send = 1
    * send and recieived = 2
    * send and seen = 3
    *
    * recived = 4
    * recived and seen = 5
    * ack send = 6*/

    /*Type of msg
    * text = 0
    * image
    * sound
    * contact
    * file
    * map/location
    * broadcast*/

        //CHATS
    /*Chat type
    deleted or not chatted with = 0
    * chat = 1
    * group = 2
    * hidden = 3
    * request = 4*/

        //CONTACTS not required
    /*Contacts type
    yes = 1
    no = 0
    */

        //CONTACTS
    /*
    contacts on messenger = 1
    not on messenger = 0

    contacts on fav
    searched on fav

    searched added

    hidden contact
    hidden contact on fav
*/

        //not required
    /*on Messenger
    *yes = 1
    *no = 0*/

        //not required
    /* fav
    yes = 1
    no = 0
     */

        //not required
    /*hidden
    yes = 1
    no = 0
     */

        private static final String DATABASE_NAME = "chatdatabase";

        public static final String TABLE_NAME = "CONTACTS";
        public static final String name = "username";
        public static final String displayname = "displayname";
        public static final String pic = "pic";
        public static final String contact = "contact";
        public static final String chat = "chat";
        //public static final String fav = "fav";
        //public static final String hidden = "hide";
        public static final String onM = "messenger";

        public static final String TABLE_NAME1 = "CHATMSGS";
        public static final String message = "msg";
        public static final String msgId = "msgId";
        public static final String mode = "mode";
        public static final String type = "type";

        public static final String phonenumber = "number";

        public static final String TABLE_NAME2 = "STATUSES";
        public static final String status = "status";

        public static final String time = "time";

        private static final int DATABASE_VERSION = 15;

        /*private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + phonenumber + " VARCHAR(20) PRIMARY KEY,"
                + name + " TEXT,"
                + displayname + " TEXT,"
                + status + " TEXT,"
                + contact + " INTEGER,"
                + chat + " INTEGER,"
                + pic + " INTEGER,"
                + time + " LONG,"
                + onM + " INTEGER,"
                + fav + " INTEGER,"
                + hidden + "INTEGER);";
    */
        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + phonenumber + " VARCHAR(20) PRIMARY KEY,"
                + name + " TEXT,"
                + displayname + " TEXT,"
                + status + " TEXT,"
                + contact + " INTEGER,"
                + chat + " INTEGER,"
                + pic + " INTEGER,"
                + time + " LONG)";
        /*
            private static final String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAME1 + "("
                    + msgId + " TEXT,"
                    + phonenumber + " VARCHAR(20),"
                    + time + " LONG,"
                    + message + " TEXT,"
                    + mode + " INTEGER,"
                    + type + " INTEGER);";
        */
        private static final String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME1 + "("
                + msgId + " TEXT,"
                + phonenumber + " VARCHAR(20),"
                + time + " LONG,"
                + message + " TEXT,"
                + mode + " INTEGER);";

        private static final String CREATE_TABLE2 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME2 + " ("
                + status + " TEXT,"
                + time + " LONG);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS '"
                + TABLE_NAME + "';";

        private static final String DROP_TABLE1 = "DROP TABLE IF EXISTS '"
                + TABLE_NAME1 + "';";

        private static final String DROP_TABLE2 = "DROP TABLE IF EXISTS '"
                + TABLE_NAME2 + "';";

        public Dbhelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE1);
            db.execSQL(CREATE_TABLE2);
        }

        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
            Log.e("onUpgrade:", " called");
            switch (oldversion) {
                case 14:
                    db.execSQL("ALTER TABLE " + TABLE_NAME + " RENAME TO " + TABLE_NAME + "_old;");
                    db.execSQL(CREATE_TABLE);
                    db.execSQL("INSERT INTO " + TABLE_NAME + " (" + phonenumber + " ,"
                            + name + " ,"
                            + displayname + " ,"
                            + status + " ,"
                            + contact + " ,"
                            + chat + " ,"
                            + pic + " ,"
                            + time + ") SELECT " + phonenumber + " ,"
                            + name + " ,"
                            + displayname + " ,"
                            + status + " ,"
                            + contact + " ,"
                            + chat + " ,"
                            + pic + " ,"
                            + time + " FROM " + TABLE_NAME + "_old;");
                    db.execSQL("DROP TABLE " + TABLE_NAME + "_old;");
                    db.execSQL("ALTER TABLE " + TABLE_NAME1 + " RENAME TO " + TABLE_NAME1 + "_old;");
                    db.execSQL(CREATE_TABLE1);
                    db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + msgId + " ,"
                            + phonenumber + " ,"
                            + time + " ,"
                            + message + " ,"
                            + mode + ") SELECT " + msgId + " ,"
                            + phonenumber + " ,"
                            + time + " ,"
                            + message + " ,"
                            + mode + " FROM " + TABLE_NAME1 + "_old;");
                    db.execSQL("DROP TABLE " + TABLE_NAME1 + "_old;");
            }
            //db.execSQL(DROP_TABLE);
            //db.execSQL(DROP_TABLE1);
            //db.execSQL(DROP_TABLE2);
            //onCreate(db);
        }
    }
}