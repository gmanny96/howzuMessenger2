package howzu.gm.chats;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;

import com.android.vending.billing.IInAppBillingService;

public class Subscription extends AppCompatActivity{

    IInAppBillingService mService;
    AppCompatButton button;
    SharedPreferences preferences;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    /*@Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.subscription);

        preferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        Bundle bundle = mService.getBuyIntent(3, "com.example.myapp", MY_SKU, "subs", developerPayload);

        PendingIntent pendingIntent = bundle.getParcelable(RESPONSE_BUY_INTENT);
        if (bundle.getInt(RESPONSE_CODE) == BILLING_RESPONSE_RESULT_OK) {
            // Start purchase flow (this brings up the Google Play UI).
            // Result will be delivered through onActivityResult().
            startIntentSenderForResult(pendingIntent, RC_BUY, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        }

        Bundle activeSubs = mService.getPurchases(3, "com.example.myapp", "subs", continueToken);

        preferences.edit().putInt("user", 2).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }*/
}
