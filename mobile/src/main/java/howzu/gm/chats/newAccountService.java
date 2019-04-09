package howzu.gm.chats;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class newAccountService extends IntentService {

    Socket socket;
    BufferedReader br;

    public newAccountService() {
        super("meAccount");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String USER = preferences.getString("user", null);
        String NAME = preferences.getString("name", null);

        try {
            socket = new Socket("192.168.0.101", 5222);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket != null) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            JSONArray array = new JSONArray();
            array.put(0).put("+919560083681").put("manny").put("main");

            /*JSONObject obj = new JSONObject();
            try {
                obj.put("t", 0).put("u", "+919560083681").put("n", "").put("r", tm.getDeviceId());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(array.toString());
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean done = false;
            final long NANOSEC_PER_SEC = 1000l*1000*1000;
            long startTime = System.nanoTime();
            while(!done && (System.nanoTime()-startTime)< 5*60*NANOSEC_PER_SEC){
                try {
                    String result = br.readLine();
                    try {
                        if(result!=null) {
                            JSONArray array1 = new JSONArray(result);
                            if (array1.length() > 0) {
                                if((Integer) array1.get(0)==1 && array1.get(1).equals(USER)){
                                    preferences.edit().putInt("user", 3).apply();
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("account_creation"));
                                    done = true;
                                }
                                if(!done && (Integer)array1.get(0)==0 && array1.get(1).equals(USER)){
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("account_creation"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
