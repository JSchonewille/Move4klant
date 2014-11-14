package com.example.jeff.move4klant;

import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import library.DatabaseHandler;
import library.Offer;
import library.PrefUtils;
import library.ibeacon;

public class Bluetoothscanner extends Service {
    // array used to decode BLE byte array
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    // the TX of when the phone is held to the beacon
    private static final Integer DISTANCE_CLOSE = -70;
    // time passive scanning goes into sleeping mode
    private static final Integer TIMEOUTTIME = 30 * 1000;
    // string used for our broadcast listener
    private static final String ACTION_STRING_SERVICE = "ToService";
    // string used for our broadcast listener
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    // the standard bluetooth adapter
    BluetoothAdapter bta;
    // List of offers we have already shown to the costumer
    private HashMap<Integer, Date> Usedoffers;
    // stops the scanning when false
    private boolean shouldscan = true;
    // the scanmode , 0 means pasive , 1 means active , 99 is default value
    private int SCANMODE = 99;
    // the major of our beacons
    private int ACTIVEMAJOR = 31691;
    // counter used for passive mode
    private int counter = 0;
    // counter used for passive mode
    private int beaconcounter = 0;
    // list with all beacons in the database
    private List<ibeacon> ibeaconList;
    // list with all the offers related to the users likes
    private List<Offer> offerList;
    // the implementation of our bluetooth scan

    //region bluetooth LE callback
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override

        public void onLeScan(final BluetoothDevice device, final int Irssi,
                             final byte[] scanRecord) {
            if (ibeaconList != null && !ibeaconList.isEmpty()) {
                int startByte = 2;
                int major = 0;
                int minor = 0;
                int tx = 0;
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
                }
                // logica for beacons detected comes here
                // if major and minor in beacon list

                if (major != ACTIVEMAJOR) {
                    counter++;
                    Log.d("bluetooth action", "major not found , counter = " + counter);
                }

                if (major == ACTIVEMAJOR) {
                    SCANMODE = 1;
                    counter = 0;
                    Log.d("bluetooth action", "major found, set scan to active");
                    for (ibeacon i : ibeaconList) {
                        if (major == i.getMajor() && minor == i.getMinor()) {
                            if (tx > DISTANCE_CLOSE) {
                                if (!AppActive())

                                    Log.d("bluetooth result", "Holding phone to beacon");
                                Intent j = new Intent(getApplicationContext(), OfferActivity.class);
                                j.putExtra("productID", i.getProductID());
                                startActivity(j);
                            }
                            // far away action
                            else if (tx < DISTANCE_CLOSE) {
                                // check if we already used this offer
                                if (Usedoffers.get(i.getOfferID()) == null) {
                                    for (Offer o : offerList) {
                                        if (o.getID() == i.getOfferID()) {
                                            Log.d("bluetooth result", "far away action");
                                            Intent j = new Intent(getApplicationContext(), OfferActivity.class);
                                            j.putExtra("offerID", i.getOfferID());
                                            Usedoffers.put(i.getOfferID(), new Date());
                                            startActivity(j);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            // if we find our major, we set our mode to active


            if (counter >= 300) {
                SCANMODE = 0;
                Log.d("bluetooth action", "counter > 300 , setting scanmode to passive");
            }

            beaconcounter++;
            // slow down scanning if mode = passive, tp conserve battery
            if (SCANMODE == 0 && beaconcounter > 10) {
                Log.d("bluetooth action", "Passive mode entered");
                beaconcounter = 0;
                try {
                    float sec = 0;
                    while (SCANMODE == 0 && sec < TIMEOUTTIME) {
                        Thread.sleep(1000);
                        sec += 1000;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    //endregion
       //region broadcast reciever
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
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
                bta.stopLeScan(mLeScanCallback);
            }
            // starts the scan when asked
            if (bta.isEnabled() && shouldscan) {
                Log.d("onrecieve action", "starting scan");
                bta.startLeScan(mLeScanCallback);
            }

            // a call is made to scan actively
            if (SCANMODE == 1) {
                Log.d("onrecieve action", "scanmode active ");
                counter = 0;
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
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bta = bluetoothManager.getAdapter();
        if (serviceReceiver != null) {
//Create an intent filter to listen to the broadcast sent with the action "ACTION_STRING_SERVICE"
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_SERVICE);
//Map the intent filter to the receiver
            registerReceiver(serviceReceiver, intentFilter);
        }
        getconfigs();
        Usedoffers = new HashMap<Integer, Date>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "onDestroy");
//STEP3: Unregister the receiver
        unregisterReceiver(serviceReceiver);
    }

    private void sendBroadcast(String extra) {

        // if  logged in
        String autoLoginState = PrefUtils.getFromPrefs(getApplicationContext(), getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");
        if (autoLoginState != "false") {
            Intent new_intent = new Intent();
            new_intent.setAction(ACTION_STRING_ACTIVITY);
            new_intent.putExtra("returnvalue", extra);
            sendBroadcast(new_intent);
        } else {
            // login and redirect
            // TODO ADD PARAMAMETERS FOR LOGIN ACTIVITY
            Intent start = new Intent(this, LoginActivity.class);
            start.setAction(Intent.ACTION_MAIN);
            start.addCategory(Intent.CATEGORY_LAUNCHER);
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(start);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("start command", "start command called");
        if (bta.isEnabled() && !bta.isDiscovering() && shouldscan) {
            Log.d("start command", "restarting scan");
            // if for some reason, the scan is aborted, we restart the scan and set it to pasive mode
            bta.startLeScan(mLeScanCallback);
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

    }

    public boolean AppActive() {
        // checks if the app is running, so we wont refresh it if its active
        String class1 = "";
        String class2 = "";
        String class3 = "";
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (task.baseActivity.getClassName().equals(class1) || task.baseActivity.getClassName().equals(class2)) {
                return true;
            }
        }
        return false;
    }
}



