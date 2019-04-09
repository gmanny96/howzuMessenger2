package howzu.gm.chats;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class serviceLogin extends Service {

    String PASSWORD;
    String USERNAME;
    static final String DEFAULT = "n/a";
    AbstractXMPPConnection connection;
    SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new serverconnection()).start();
        return START_STICKY;
    }

    class serverconnection implements Runnable {
        @Override
        public void run() {
            preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            USERNAME = preferences.getString("username", DEFAULT);

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            PASSWORD = tm.getDeviceId();

            //PASSWORD = "p" +USERNAME;

            KeyStore trustStore = null;
            try {
                trustStore = KeyStore.getInstance("BKS");
            } catch (KeyStoreException e) {
                Log.e("SE", e.toString());
            }
            InputStream trustStoreStream = getApplicationContext().getResources().openRawResource(R.raw.howzuserver);
            try {
                trustStore.load(trustStoreStream, "keypass".toCharArray());
            } catch (IOException e) {
                Log.e("IOE", e.toString());
            } catch (NoSuchAlgorithmException e) {
                Log.e("NSAE", e.toString());
            } catch (CertificateException e) {
                Log.e("CE", e.toString());
            }

            TrustManagerFactory trustManagerFactory = null;
            try {
                trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            } catch (NoSuchAlgorithmException e) {
                Log.e("NSAE", e.toString());
            }
            try {
                trustManagerFactory.init(trustStore);
            } catch (KeyStoreException e) {
                Log.e("KSE", e.toString());
            }

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                Log.e("NSAE", e.toString());
            }
            try {
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            } catch (KeyManagementException e) {
                Log.e("KME", e.toString());
            }

            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setResource(PASSWORD);
            configBuilder.setServiceName("aa-pc");
            configBuilder.setHost("192.168.0.104");
            configBuilder.setCustomSSLContext(sslContext);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            connection = new XMPPTCPConnection(configBuilder.build());

            try {
                connection.connect();
            } catch (SmackException | XMPPException | IOException e) {
                e.printStackTrace();
            }

            try {
                connection.login("+919910098611", PASSWORD);
            } catch (XMPPException | SmackException | IOException e) {
                e.printStackTrace();
            }

            if (connection != null && connection.isConnected()) {
                Lists.getInstance().setConnection(connection);
                startService(new Intent(getApplicationContext(), serviceRecieveMessage.class));
            } else {
                Lists.getInstance().setConnection(null);
                stopSelf();
            }
        }
    }
}