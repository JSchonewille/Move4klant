package library;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4klant.LoginActivity;
import com.example.jeff.move4klant.OfferActivity;
import com.example.jeff.move4klant.ProductInfoActivity;
import com.example.jeff.move4klant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Objects.Offer;
import Objects.User;
import Objects.ibeacon;

public class Bluetoothscanner extends Service {
    // array used to decode BLE byte array
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    // the TX of when the phone is held to the beacon
    private Integer DISTANCE_CLOSE = -63;
    // active scan time
    private static int SCAN_TIME = 20 * 1000;
    private static int SCAN_STOP_TIME = 500;
    // string used for our broadcast listener
    private static final String ACTION_STRING_SERVICE = "ToService";
    // string used for our broadcast listener
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    // the time thats has passes after inactivity
    private static final int INACTIVITY_TIME = 5;
    // the standard bluetooth adapter
    BluetoothAdapter bta;
    // current user
    User user;
    Boolean checkedIn = false;
    Boolean checkinCallRunning = false;
    // List of offers we have already shown to the costumer
    private HashMap<Integer, Date> usedOffers;
    // stops the scanning when false
    private boolean shouldscan = true;
    // the scanmode , 0 means pasive , 1 means active , 99 is default value
    // the major of our beacons
    private int ACTIVEMAJOR = 31690;
    // counter used for passive mode
    private int passiveCounter = 0;
    // list with all beacons in the database
    private List<ibeacon> ibeaconList;
    // list with all the offers related to the users likes
    private List<Offer> offerList;
    // the implementation of our bluetooth scan
    private Smoothener smooth;
    // counter to check if our BLE gets results
    private int noBLECounter = 0;
    private Date productIntentTime;
    private Handler mHandler;

    //region bluetooth LE callback
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int Irssi,
                             final byte[] scanRecord) {
            if (ibeaconList != null && !ibeaconList.isEmpty()) {
                int startByte = 2;
                int major;
                int minor;
                int tx;
                boolean patternFound = false;

                while (startByte <= 5) {
                    if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                            ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                        patternFound = true;
                        break;
                    }
                    startByte++;
                }
                // als Het IBEACON patroon gevonden is kan er worden gezocht naar de MInor en MAjor
                if (patternFound) {
                    //Convert to hex String
                    byte[] uuidBytes = new byte[16];
                    System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                    String hexString = bytesToHex(uuidBytes);

                    //Here is your UUID
                    String uuid = hexString.substring(0, 8) + "-" +
                            hexString.substring(8, 12) + "-" +
                            hexString.substring(12, 16) + "-" +
                            hexString.substring(16, 20) + "-" +
                            hexString.substring(20, 32);

                    //Here is your Major value
                    major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
                    //Here is your Minor value
                    minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
                    tx = (scanRecord[startByte + 24]);

                    Integer adjrssi = smooth.smoothen(Irssi, major, minor);
                    // logica for beacons detected comes here

                    DISTANCE_CLOSE = (int) (tx * 0.83);

                    if (major != ACTIVEMAJOR) {
                        passiveCounter++;
                    }

                    if (major == ACTIVEMAJOR) {

                        noBLECounter = 0;
                        passiveCounter = 0;
                        // scan mode to active
                        setscanmode(0);
                        //we check the user in
                        if (!checkedIn && !checkinCallRunning) {
                            Checkinout(true);
                        }
                        // if somehow we dont have our configs
                        if (ibeaconList == null || offerList == null) {
                            Log.e("no config", "reloading configs");
                            getconfigs();
                        } else {
                            for (ibeacon i : ibeaconList) {
                                if (major == i.getMajor() && minor == i.getMinor()) {
                                    if (adjrssi > DISTANCE_CLOSE) {
                                        long diffInsec = Math.abs((new Date()).getTime() - productIntentTime.getTime()) / 1000;
                                        if (diffInsec > 2) {
                                            String autoLoginState = PrefUtils.getFromPrefs(getApplicationContext(), getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");
                                            // check if user is logged in
                                            if (autoLoginState.equals("true")) {
                                                Log.d("bluetooth result" + tx, "Holding phone to beacon");
                                                Intent j = new Intent(getApplicationContext(), ProductInfoActivity.class);
                                                j.putExtra("productID", i.getProductID());
                                                j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(j);
                                                productIntentTime = new Date();
                                            }
                                        }
                                    }
                                    // far away action
                                    else {
                                        // check if we already used this offer
                                        if (usedOffers.get(i.getOfferID()) == null) {
                                            for (Offer o : offerList) {
                                                if (o.getID() == i.getOfferID()) {
                                                    // check if user is logged in
                                                    String autoLoginState = PrefUtils.getFromPrefs(getApplicationContext(), getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");
                                                    if (autoLoginState.equals("true")) {
                                                        Log.d("bluetooth result", "far away action");
                                                        Intent j = new Intent(getApplicationContext(), OfferActivity.class);
                                                        j.putExtra("offerID", i.getOfferID());
                                                        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        usedOffers.put(i.getOfferID(), new Date());
                                                        startActivity(j);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    };
    //endregion
    //region broadcast reciever
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int SCANMODE = 99;
            try {
                shouldscan = intent.getBooleanExtra("RUN", true);
                SCANMODE = intent.getIntExtra("SCANMODE", 99);


                Log.d("onrecive", "scanmode " + SCANMODE + "  shouldscan  " + shouldscan);
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }

            if (intent.getBooleanExtra("UPDATELIKE", false)) {
                getconfigs();
            }
            // stops the bluetooth scanning when asked
            if (bta.isEnabled() && !shouldscan) {
                Log.d("onrecieve action", "should not scan");
                mStopRunnable.run();
            }
            // starts the scan when asked
            if (bta.isEnabled() && shouldscan) {
                Log.d("onrecieve action", "starting scan");
                mStartRunnable.run();
            }

            // a call is made to scan actively
            if (SCANMODE == 0) {
                setscanmode(0);
                Log.d("onrecieve action", "scanmode active ");
                passiveCounter = 0;
                // activate the thread when its in waiting mode
            }
        }
    };
//endregion

    // bytes to hex converter for analysing the byte array from the bluetooth scanner
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (serviceReceiver != null) {
//Create an intent filter to listen to the broadcast sent with the action "ACTION_STRING_SERVICE"
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_SERVICE);
//Map the intent filter to the receiver
            registerReceiver(serviceReceiver, intentFilter);
        }
        mHandler = new Handler();
        getconfigs();
        smooth = new Smoothener(8);
        productIntentTime = new Date();
        usedOffers = new HashMap<Integer, Date>();


        Context context = getApplicationContext();
        Calendar cal = Calendar.getInstance();

        Intent i = new Intent(context,Bluetoothscanner.class);
        PendingIntent P = PendingIntent.getService(context, 0, i, 0);
        AlarmManager ALS = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        ALS.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),60 * 1000 ,P);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "onDestroy");
//STEP3: Unregister the receiver
        unregisterReceiver(serviceReceiver);
    }

//    private void sendBroadcast(String extra) {
//
//        // if  logged in
//        String autoLoginState = PrefUtils.getFromPrefs(getApplicationContext(), getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");
//        if (autoLoginState != "false") {
//            Intent new_intent = new Intent();
//            new_intent.setAction(ACTION_STRING_ACTIVITY);
//            new_intent.putExtra("returnvalue", extra);
//            sendBroadcast(new_intent);
//        } else {
//            // login and redirect
//            //
//            Intent start = new Intent(this, LoginActivity.class);
//            start.setAction(Intent.ACTION_MAIN);
//            start.addCategory(Intent.CATEGORY_LAUNCHER);
//            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(start);
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("start command", "start command called");
        noBLECounter++;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bta = bluetoothManager.getAdapter();

        if(bta.isEnabled() && shouldscan ) {
            mStopRunnable.run();
            mHandler.removeCallbacks(mStopRunnable);
            mHandler.removeCallbacks(mStartRunnable);
            // starting our BLE SCAN
            mStartRunnable.run();
        }

        // if we didnt get any results for 5 minutes straight.. we check if we have to checkout the user
        if (noBLECounter >= INACTIVITY_TIME) {
            // reset the counter to avoid unnecessary database calls

            if (!checkinCallRunning) {
                noBLECounter = 0;
                Checkinout(false);
                Log.d("checkout", "user checked out from non bluetooth results");
            }
        }

        if(passiveCounter > 300 || noBLECounter > 5)
        {
            // passive scan mode after inactivity
            setscanmode(1);
        }
        return Service.START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getconfigs() {
        ibeaconList = DatabaseHandler.getInstance(getApplicationContext()).getAllBeacons();
        offerList = DatabaseHandler.getInstance(getApplicationContext()).getOfferByLikedCategories();
        //offerList = DatabaseHandler.getInstance(getApplicationContext()).getAllOffers();
        user = DatabaseHandler.getInstance(getApplicationContext()).getUser();
        if (usedOffers != null) {
            usedOffers.clear();
        }
    }

//    public boolean AppActive() {
//        // checks if the app is running, so we wont refresh it if its active
//        String class1 = "com.example.jeff.move4klant.ProductInfoActivity";
//        String class2 = "";
//        String class3 = "";
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);
//        for (ActivityManager.RunningTaskInfo task : tasks) {
//            if (task.baseActivity.getClassName().equals(class1) || task.baseActivity.getClassName().equals(class2)) {
//                return true;
//            }
//        }
//        return false;
//    }

    // the logic for checking in or out
    public void Checkinout(final Boolean action) {
        checkinCallRunning = true;
        ServerRequestHandler.checkinstatus(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonArray) {
                Log.d("CHECKIN STATUS", jsonArray.toString());
                try {
                    boolean checkinstatus = jsonArray.getBoolean("returnvalue");
                    // we want to check the user in
                    if (action) {
                        // if the user isnt checked in
                        if (!checkinstatus) {
                            // check in
                            dbCheckInOut(true);
                        } else {
                            Log.d("CHECK IN", "user was already checked in");
                            checkedIn = true;
                        }
                    }
                    // we want to check the user out
                    if (!action) {
                        // the user is checked in
                        if (checkinstatus) {
                            dbCheckInOut(false);
                        } else {
                            checkedIn = false;
                            Log.d("CHECK OUT", "user was already checked out");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                checkinCallRunning = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null) {
                    Log.e("NETWORKERROR", volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data));
                } else {
                    Log.e("NETWORKERROR", "no network");
                }
                checkinCallRunning = false;
            }
        }, user.getUserID());
    }

    // a function to check the user out. we defined a different function because we want to make sure there is a proper response
    // this function makes the database call
    public void dbCheckInOut(final Boolean action) {
        ServerRequestHandler.checkinout(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                // if we wanted to checkin
                if (action) {
                    checkedIn = true;
                    Log.d("CHECK IN", "user checked in");

                }

                // if we wanted to check out
                if (!action) {
                    checkedIn = false;
                    Log.d("CHECK OUT", "user checked out");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, user.getUserID());
    }

    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startscan();
        }
    };
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopscan();
        }
    };

    public void startscan()
    {
        if(bta.isEnabled()) {
            bta.startLeScan(mLeScanCallback);
            mHandler.postDelayed(mStopRunnable, SCAN_TIME);
        }
    }

    public void stopscan()
    {
        bta.stopLeScan(mLeScanCallback);
        mHandler.postDelayed(mStartRunnable,SCAN_STOP_TIME);
    }

    public void setscanmode(Integer mode)
    {
        // set scan mode te active
        if (mode == 0){
            SCAN_STOP_TIME = 500;
        }
        // set scan mode to pasive
        if(mode == 1)
        {
            SCAN_STOP_TIME = 40 * 1000;
        }

    }

}


