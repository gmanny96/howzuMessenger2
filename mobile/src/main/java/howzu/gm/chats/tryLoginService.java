package howzu.gm.chats;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class tryLoginService extends Service {

    Socket socket = null;
    String newMsgToSend = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("firing server", "pp");
        if (!meLists.getInstance().getConnectionStatus()) {
            Log.e("socket null", "new thread");
            new Thread(new tryLoginService.serverConnection()).start();
        }
        return START_STICKY;
    }

    private class serverConnection implements Runnable {
        @Override
        public void run() {
            //SharedPreferences preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            //String USER = preferences.getString("user", null);

            if(socket == null){
                try {
                    socket = new Socket("192.168.0.103", 5222);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(socket!=null){
                    JSONArray array = new JSONArray();
                    array.put((Integer) 1).put("+919560083681");

                    try {
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF(array.toString());
                        dout.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        socket.setSoTimeout(3*60*1000);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

                    try {
                        DataInputStream dis=new DataInputStream(socket.getInputStream());
                        String result = (String)dis.readUTF();

                        if (!result.isEmpty()) {
                            Log.e("result:", result);
                            Log.e("result cgeck ho rha h", "ble ble");

                            try {
                                Log.e("shuru hogya try h", "ble");
                                array = new JSONArray(result);
                                if (array.getInt(0) == 1 && array.getString(1).equals("+919560083681")) {
                                    Log.e("record bngya h paas h", "ble ble");
                                    socket.setSoTimeout(0);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(myReceiver, new IntentFilter("Service"));

                                    //meLists.getInstance().setConnection(socket);
                                    //startService(new Intent(getApplicationContext(), newRecieveMessageService.class));
                                    //done = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            while(true){
                try {
                    DataInputStream dis=new DataInputStream(socket.getInputStream());
                    String newMsgRec = (String)dis.readUTF();
                    startService(new Intent(getApplicationContext(), newHandleNewDataService.class).putExtra("data", newMsgRec));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /*if (meLists.getInstance().getConnection() == null) {
                Log.e("socket null", "new socket");
                try {
                    socket = SocketChannel.open(new InetSocketAddress("192.168.0.103", 5222));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (socket != null) {
                    //TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    JSONArray array = new JSONArray();
                    array.put((Integer) 1).put("+919560083681");

                    byte[] bytes = new byte[0];
                    try {
                        bytes = array.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    ByteBuffer buffer2 = null;
                    try {
                        buffer2 = ByteBuffer.wrap("this is 2nd msg".getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        socket.write(buffer2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    buffer.clear();

                    ByteBuffer resultBuffer = ByteBuffer.allocate(256);
                    Boolean done = false;
                    final long NANOSEC_PER_SEC = 1000l * 1000 * 1000;
                    long startTime = System.nanoTime();
                    while (!done && (System.nanoTime() - startTime) < 20 * 60 * NANOSEC_PER_SEC) {
                        try {
                            socket.read(resultBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String result = new String(resultBuffer.array()).trim();

                        if (!result.isEmpty()) {
                            Log.e("result:", result);
                            Log.e("result cgeck ho rha h", "ble ble");

                            try {
                                Log.e("shuru hogya try h", "ble");
                                array = new JSONArray(result);
                                if (array.getInt(0) == 1 && array.getString(1).equals("+919560083681")) {
                                    Log.e("record bngya h paas h", "ble ble");
                                    meLists.getInstance().setConnection(socket);
                                    startService(new Intent(getApplicationContext(), newRecieveMessageService.class));
                                    done = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else
                    meLists.getInstance().setConnection(null);
            }*/
        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            while(newMsgToSend!=null);
            newMsgToSend = intent.getStringExtra("data");
            new Thread(new tryLoginService.sendData()).start();
        }
    };

    private class sendData implements Runnable {
        @Override
        public void run() {
            try {
                DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                dout.writeUTF(newMsgToSend);
                dout.flush();
                newMsgToSend = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }
}
