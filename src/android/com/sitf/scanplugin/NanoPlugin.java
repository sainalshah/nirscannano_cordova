package com.sitf.scanplugin;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kstechnologies.nirscannanolibrary.KSTNanoSDK;
import com.kstechnologies.nirscannanolibrary.SettingsManager;
import com.kstechnologies.nanoscan.NanoBLEService;

public class NanoPlugin extends CordovaPlugin /*implements ServiceConnection*/ {

    private static CallbackContext mcallbackContext;
    public static Context mContext;
    public static Activity mActivity;
    private ViewPager mViewPager;
    private String fileName;
    private ArrayList<String> mXValues;

//    private ArrayList<Float> mIntensityFloat;
    private ArrayList<Float> mAbsorbanceFloat;
//    private ArrayList<Float> mReflectanceFloat;
    private ArrayList<Float> mWavelengthFloat;

    private BluetoothAdapter btAdapter;
    private final BroadcastReceiver scanDataReadyReceiver = new scanDataReadyReceiver();
    private final BroadcastReceiver refReadyReceiver = new refReadyReceiver();
    private final BroadcastReceiver notifyCompleteReceiver = new notifyCompleteReceiver();
    private final BroadcastReceiver scanStartedReceiver = new ScanStartedReceiver();
    private final BroadcastReceiver requestCalCoeffReceiver = new requestCalCoeffReceiver();
    private final BroadcastReceiver requestCalMatrixReceiver = new requestCalMatrixReceiver();
    private final BroadcastReceiver disconnReceiver = new DisconnReceiver();

    private final IntentFilter scanDataReadyFilter = new IntentFilter(KSTNanoSDK.SCAN_DATA);
    private final IntentFilter refReadyFilter = new IntentFilter(KSTNanoSDK.REF_CONF_DATA);
    private final IntentFilter notifyCompleteFilter = new IntentFilter(KSTNanoSDK.ACTION_NOTIFY_DONE);
    private final IntentFilter requestCalCoeffFilter = new IntentFilter(KSTNanoSDK.ACTION_REQ_CAL_COEFF);
    private final IntentFilter requestCalMatrixFilter = new IntentFilter(KSTNanoSDK.ACTION_REQ_CAL_MATRIX);
    private final IntentFilter disconnFilter = new IntentFilter(KSTNanoSDK.ACTION_GATT_DISCONNECTED);
    private final IntentFilter scanStartedFilter = new IntentFilter(NanoBLEService.ACTION_SCAN_STARTED);

    private final BroadcastReceiver scanConfReceiver = new ScanConfReceiver();
    private final IntentFilter scanConfFilter = new IntentFilter(KSTNanoSDK.SCAN_CONF_DATA);
    private int calMatRecieveProgress;
    private int calMatSize;
    private KSTNanoSDK.ScanResults results;

    private NanoBLEService mNanoBLEService;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler;
    private static final String DEVICE_NAME = "NIRScanNano";
    private boolean connected;
    private KSTNanoSDK.ScanConfiguration activeConf;
    private String preferredDevice;

    public static String TAG = "NanoPlugin";
    private boolean readyToScan;

    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String ACCESS_BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;
    private static final String ACCESS_BLUETOOTH = Manifest.permission.BLUETOOTH;

    private boolean isBleServiceRunning = false;
    private static final int requestCode = 0;
    private boolean userRequestedBT =false;
    private LocationManager lm;
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity().getApplicationContext();
        mActivity = cordova.getActivity();
        readyToScan = false;
        // Register broadcasts receiver for bluetooth state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mbluetoothStateReceiver, filter);
//        Intent intent = new Intent(mContext, BoundService.class);
//        mContext.startService(intent);
//        mContext.bindService(intent, mServiceConnectionExample, Context.BIND_AUTO_CREATE);
//        mContext.bindService(
//                new Intent(cordova.getActivity(),
//                        NanoBLEService.class),
//                this, Context.BIND_AUTO_CREATE
//        );
        Log.v(TAG, "Init Device");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mbluetoothStateReceiver);
        shutDownBLE();
    }
    private void shutDownBLE(){
        mContext.unbindService(mServiceConnection);

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(scanDataReadyReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(refReadyReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(notifyCompleteReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(requestCalCoeffReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(requestCalMatrixReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(disconnReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(scanConfReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }

    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        mcallbackContext = callbackContext;
        Log.d(TAG, "action received" + action);
        if (action.equals("startSearch")) {

            if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                //has BLE

                startSearch();

                return true;
            }
            else{
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR,"This device doesn't support Bluetooth V4");
                pluginResult.setKeepCallback(false);
                mcallbackContext.sendPluginResult(pluginResult);
                return false;
            }

        } else if (action.equals("scan")) {
            Log.d(TAG, ":scan action, sending start scan broadcast");
            String filePrefix = "sitf";//test file prefix
            SettingsManager.storeStringPref(mContext, SettingsManager.SharedPreferencesKeys.prefix, filePrefix);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(KSTNanoSDK.START_SCAN));
            return true;
        }
        else if(action.equals("turnOnBT")){

            readyToScan = false;
            shutDownBLE();
            return true;
        } else {

            callbackContext.error(action);
            return false;
        }
    }


    private void startSearch() {

        checkForPermissions();

        if (Build.VERSION.SDK_INT >= 23 && !isLocationOn()) {
            // write api specific code here?
            displayLocationSettingsRequest(mContext);
        }
        if(isBluetoothOn()) {
            Intent gattServiceIntent = new Intent(mContext, NanoBLEService.class);
            mContext.startService(gattServiceIntent);
            mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            isBleServiceRunning = true;
            //Register all needed broadcast receivers
            LocalBroadcastManager.getInstance(mContext).registerReceiver(scanDataReadyReceiver, scanDataReadyFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(refReadyReceiver, refReadyFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(notifyCompleteReceiver, notifyCompleteFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(requestCalCoeffReceiver, requestCalCoeffFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(requestCalMatrixReceiver, requestCalMatrixFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(disconnReceiver, disconnFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(scanConfReceiver, scanConfFilter);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(scanStartedReceiver, scanStartedFilter);
        }
        else{
//            delaySearch=true;
//            setBluetooth(true);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR,"{\"msg\":\"btoff\"}");
            pluginResult.setKeepCallback(false);
            mcallbackContext.sendPluginResult(pluginResult);
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            //Get a reference to the service from the service connection
            mNanoBLEService = ((NanoBLEService.LocalBinder) service).getService();
            Log.d(TAG, "hi from onServiceConnected~~!!~~");
            //initialize bluetooth, if BLE is not available, then finish
            if (!mNanoBLEService.initialize()) {
                //finish();
                Log.d(TAG, "error ble not initialized");
            }

            //Start scanning for devices that match DEVICE_NAME
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (mBluetoothLeScanner == null) {
                //finish();
                Toast.makeText(mContext, "Please ensure Bluetooth is enabled and try again", Toast.LENGTH_SHORT).show();
            }
            mHandler = new Handler();
            if (SettingsManager.getStringPref(mContext, SettingsManager.SharedPreferencesKeys.preferredDevice, null) != null) {
                preferredDevice = SettingsManager.getStringPref(mContext, SettingsManager.SharedPreferencesKeys.preferredDevice, null);
                scanPreferredLeDevice(true);
            } else {
                scanLeDevice(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mNanoBLEService = null;
        }
    };

    private final BroadcastReceiver mbluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(isBleServiceRunning){
                            Log.d(TAG,"BT is off, shutting down ble service");
                            shutDownBLE();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        if(userRequestedBT) {
                            userRequestedBT = false;
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "{\"msg\":\"btStatusOn\"}");
                            pluginResult.setKeepCallback(false);
                            mcallbackContext.sendPluginResult(pluginResult);
                        }

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    private void registerLocationUpdate(){
        // Acquire a reference to the system Location Manager
        android.location.LocationManager locationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    private boolean isBluetoothOn(){
        if(btAdapter==null)
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        return btAdapter.isEnabled();
    }
    private void setBluetooth(boolean enable){
        if(btAdapter==null)
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(enable && !isBluetoothOn()){
            btAdapter.enable();
        }
        else if(!enable && isBluetoothOn()){
            btAdapter.disable();
        }
    }
    private boolean isLocationOn(){
        if(lm == null) {
            lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d(TAG,"gps enabled: "+gps_enabled);
        } catch(Exception ex) {
            Log.d(TAG,"exception thrown while checking for location");
        }
        return gps_enabled;
    }
    private void checkForPermissions(){
        ArrayList<String>  permissionsNeeded = new ArrayList<String>();
        String[] permissions = new String[1];
        if (Build.VERSION.SDK_INT >= 23) {
            // write api specific code here?
            if (!PermissionHelper.hasPermission(this, ACCESS_COARSE_LOCATION)) {
                // save info so we can call this method again after permissions are granted
                permissionsNeeded.add(ACCESS_COARSE_LOCATION);
            }
        }
        if (!PermissionHelper.hasPermission(this, ACCESS_BLUETOOTH)) {
            // save info so we can call this method again after permissions are granted
            permissionsNeeded.add(ACCESS_BLUETOOTH);
        }
        if (!PermissionHelper.hasPermission(this, ACCESS_BLUETOOTH_ADMIN)) {
            // save info so we can call this method again after permissions are granted
            permissionsNeeded.add(ACCESS_BLUETOOTH_ADMIN);
        }
        if(permissionsNeeded.size() > 0){

            Log.d(TAG,"requesting needed permissions: "+permissionsNeeded.size());
            //PermissionHelper.requestPermissions(this, requestCode, permissionsNeeded);
            PermissionHelper.requestPermissions(this, requestCode, permissionsNeeded.toArray(permissions));
            //return;
        }
    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        int REQUEST_CHECK_SETTINGS = 0x01;
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    /**
     * Scans for Bluetooth devices on the specified interval {@link NanoBLEService#SCAN_PERIOD}.
     * This function uses the handler {@link //NewScanActivity#mHandler} to delay call to stop
     * scanning until after the interval has expired. The start and stop functions take an
     * LeScanCallback parameter that specifies the callback function when a Bluetooth device
     * has been found {@link //NewScanActivity#mLeScanCallback}
     *
     * @param enable Tells the Bluetooth adapter {@link KSTNanoSDK#mBluetoothAdapter} if
     *               it should start or stop scanning
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            Log.d(TAG, "Hi from scanLeDevice");
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBluetoothLeScanner != null) {
                        mBluetoothLeScanner.stopScan(mLeScanCallback);
                        if (!connected) {
                            //notConnectedDialog();
                            Log.d(TAG, "not connected");
                        }
                    }
                }
            }, NanoBLEService.SCAN_PERIOD);
            Log.d(TAG, "mBluetothLeScanner is not null: " + (mBluetoothLeScanner != null));
            if (mBluetoothLeScanner != null) {
                mBluetoothLeScanner.startScan(mLeScanCallback);
            } else {
                //finish();
                Toast.makeText(mContext, "Please ensure Bluetooth is enabled and try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            mBluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }

    /**
     * Scans for preferred Nano devices on the specified interval {@link NanoBLEService#SCAN_PERIOD}.
     * This function uses the handler {@link //NewScanActivity#mHandler} to delay call to stop
     * scanning until after the interval has expired. The start and stop functions take an
     * LeScanCallback parameter that specifies the callback function when a Bluetooth device
     * has been found {@link //NewScanActivity#mPreferredLeScanCallback}
     *
     * @param enable Tells the Bluetooth adapter {@link KSTNanoSDK#mBluetoothAdapter} if
     *               it should start or stop scanning
     */
    private void scanPreferredLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothLeScanner.stopScan(mPreferredLeScanCallback);
                    if (!connected) {

                        scanLeDevice(true);
                    }
                }
            }, NanoBLEService.SCAN_PERIOD);
            mBluetoothLeScanner.startScan(mPreferredLeScanCallback);
        } else {
            mBluetoothLeScanner.stopScan(mPreferredLeScanCallback);
        }
    }

    /**
     * Callback function for Bluetooth scanning. This function provides the instance of the
     * Bluetooth device {@link BluetoothDevice} that was found, it's rssi, and advertisement
     * data (scanRecord).
     * <p>
     * When a Bluetooth device with the advertised name matching the
     * string DEVICE_NAME {@link //NewScanActivity#DEVICE_NAME} is found, a call is made to connect
     * to the device. Also, the Bluetooth should stop scanning, even if
     * the {@link NanoBLEService#SCAN_PERIOD} has not expired
     */
    private final ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            String name = device.getName();
            Log.d(TAG, "found device after scan: " + name);
            if (name != null) {
                if (device.getName().equals(DEVICE_NAME)) {
                    mNanoBLEService.connect(device.getAddress());
                    Log.d(TAG, "connected to " + name + "!!!");
                    connected = true;
                    scanLeDevice(false);
                }
            }
        }
    };

    /**
     * Callback function for preferred Nano scanning. This function provides the instance of the
     * Bluetooth device {@link BluetoothDevice} that was found, it's rssi, and advertisement
     * data (scanRecord).
     * <p>
     * When a Bluetooth device with the advertised name matching the
     * string DEVICE_NAME {@link //NewScanActivity#DEVICE_NAME} is found, a call is made to connect
     * to the device. Also, the Bluetooth should stop scanning, even if
     * the {@link NanoBLEService#SCAN_PERIOD} has not expired
     */
    private final ScanCallback mPreferredLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            String name = device.getName();
            if (name != null) {
                if (device.getName().equals(DEVICE_NAME)) {
                    if (device.getAddress().equals(preferredDevice)) {
                        mNanoBLEService.connect(device.getAddress());
                        connected = true;
                        scanPreferredLeDevice(false);
                    }
                }
            }
        }
    };

    public class scanDataReadyReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            //calProgress.setVisibility(View.GONE);
            //btn_scan.setText(getString(R.string.scan));
            byte[] scanData = intent.getByteArrayExtra(KSTNanoSDK.EXTRA_DATA);

            String scanType = intent.getStringExtra(KSTNanoSDK.EXTRA_SCAN_TYPE);

            /*
            * 7 bytes representing the current data
            * byte0: uint8_t     year; //< years since 2000
            * byte1: uint8_t     month; /**< months since January [0-11]
            * byte2: uint8_t     day; /**< day of the month [1-31]
            * byte3: uint8_t     day_of_week; /**< days since Sunday [0-6]
            * byte3: uint8_t     hour; /**< hours since midnight [0-23]
            * byte5: uint8_t     minute; //< minutes after the hour [0-59]
            * byte6: uint8_t     second; //< seconds after the minute [0-60]
            */
            String scanDate = intent.getStringExtra(KSTNanoSDK.EXTRA_SCAN_DATE);

            KSTNanoSDK.ReferenceCalibration ref = KSTNanoSDK.ReferenceCalibration.currentCalibration.get(0);
            results = KSTNanoSDK.KSTNanoSDK_dlpSpecScanInterpReference(scanData, ref.getRefCalCoefficients(), ref.getRefCalMatrix());
            //Log.d(TAG, "scanDataReadyReceiver says, scanType: " + scanType + " scanDate " + scanDate + " and result: " + results.toJSONString());

            mXValues = new ArrayList<String>();
//            mIntensityFloat = new ArrayList<>();
            mAbsorbanceFloat = new ArrayList<Float>();
//            mReflectanceFloat = new ArrayList<>();
            mWavelengthFloat = new ArrayList<Float>();

            int index;
            for (index = 0; index < results.getLength(); index++) {
                mXValues.add(String.format("%.02f", KSTNanoSDK.ScanResults.getSpatialFreq(mContext, results.getWavelength()[index])));
//                mIntensityFloat.add((float) results.getUncalibratedIntensity()[index]);
                float absorbance = (-1) * (float) Math.log10((double) results.getUncalibratedIntensity()[index] / (double) results.getIntensity()[index]);
                if(Float.isNaN(absorbance) == false) {
                    mAbsorbanceFloat.add(absorbance);
//                mReflectanceFloat.add((float) results.getUncalibratedIntensity()[index] / results.getIntensity()[index]);
                    mWavelengthFloat.add((float) results.getWavelength()[index]);
                }
            }

            String resAbsorbance = "{\"wavelength\":"+mWavelengthFloat.toString()+",\"absorbance\":"+mAbsorbanceFloat.toString()+"}";
//            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,results.toJSONString());
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,resAbsorbance);
            pluginResult.setKeepCallback(false);
            mcallbackContext.sendPluginResult(pluginResult);
/*
            float minWavelength = mWavelengthFloat.get(0);
            float maxWavelength = mWavelengthFloat.get(0);

            for (Float f : mWavelengthFloat) {
                if (f < minWavelength) minWavelength = f;
                if (f > maxWavelength) maxWavelength = f;
            }

            float minAbsorbance = mAbsorbanceFloat.get(0).getVal();
            float maxAbsorbance = mAbsorbanceFloat.get(0).getVal();

            for (Entry e : mAbsorbanceFloat) {
                if (e.getVal() < minAbsorbance) minAbsorbance = e.getVal();
                if (e.getVal() > maxAbsorbance) maxAbsorbance = e.getVal();
            }

            float minReflectance = mReflectanceFloat.get(0).getVal();
            float maxReflectance = mReflectanceFloat.get(0).getVal();

            for (Entry e : mReflectanceFloat) {
                if (e.getVal() < minReflectance) minReflectance = e.getVal();
                if (e.getVal() > maxReflectance) maxReflectance = e.getVal();
            }

            float minIntensity = mIntensityFloat.get(0).getVal();
            float maxIntensity = mIntensityFloat.get(0).getVal();

            for (Entry e : mIntensityFloat) {
                if (e.getVal() < minIntensity) minIntensity = e.getVal();
                if (e.getVal() > maxIntensity) maxIntensity = e.getVal();
            }

            mViewPager.setAdapter(mViewPager.getAdapter());
            mViewPager.invalidate();

            if (scanType.equals("00")) {
                scanType = "Column 1";
            } else {
                scanType = "Hadamard";
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyhhmmss", java.util.Locale.getDefault());
            String ts = simpleDateFormat.format(new Date());

            ActionBar ab = getActionBar();
            if (ab != null) {

                if (filePrefix.getText().toString().equals("")) {
                    ab.setTitle("Nano" + ts);
                } else {
                    ab.setTitle(filePrefix.getText().toString() + ts);
                }
                ab.setSelectedNavigationItem(0);
            }

            boolean saveOS = btn_os.isChecked();
            boolean continuous = btn_continuous.isChecked();

            writeCSV(ts, results, saveOS);
            writeCSVDict(ts, scanType, scanDate, String.valueOf(minWavelength), String.valueOf(maxWavelength), String.valueOf(results.getLength()), String.valueOf(results.getLength()), "1", "2.00", saveOS);

            SettingsManager.storeStringPref(mContext, SettingsManager.SharedPreferencesKeys.prefix, filePrefix.getText().toString());

            if (continuous) {
                calProgress.setVisibility(View.VISIBLE);
                btn_scan.setText(getString(R.string.scanning));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(KSTNanoSDK.SEND_DATA));
            }*/
        }
    }

    public class refReadyReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            byte[] refCoeff = intent.getByteArrayExtra(KSTNanoSDK.EXTRA_REF_COEF_DATA);
            byte[] refMatrix = intent.getByteArrayExtra(KSTNanoSDK.EXTRA_REF_MATRIX_DATA);
            ArrayList<KSTNanoSDK.ReferenceCalibration> refCal = new ArrayList<KSTNanoSDK.ReferenceCalibration>();
            refCal.add(new KSTNanoSDK.ReferenceCalibration(refCoeff, refMatrix));
            KSTNanoSDK.ReferenceCalibration.writeRefCalFile(mContext, refCal);
            //calProgress.setVisibility(View.GONE);
            Log.d(TAG, "refReadyReceiver says, setting reference calibration");
        }
    }

    /**
     * Custom receiver for returning the event that a scan has been initiated from the button
     */
    public class ScanStartedReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            //calProgress.setVisibility(View.VISIBLE);
            //btn_scan.setText(getString(R.string.scanning));
            Log.d(TAG, "ScanStartedReceiver says, scan has been started!!");
        }
    }

    /**
     * Custom receiver that will request the time once all of the GATT notifications have been
     * subscribed to
     */
    public class notifyCompleteReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(KSTNanoSDK.SET_TIME));
            Log.d(TAG, "notifyCompleteReceiver says, setting time");
        }
    }

    public class requestCalCoeffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "requestCalCoeffReceiver says, requesting calCoEff");
            intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_COEFF_SIZE, 0);
            Boolean size = intent.getBooleanExtra(KSTNanoSDK.EXTRA_REF_CAL_COEFF_SIZE_PACKET, false);
            /*if (size) {
                calProgress.setVisibility(View.INVISIBLE);
                barProgressDialog = new ProgressDialog(NewScanActivity.this);

                barProgressDialog.setTitle(getString(R.string.dl_ref_cal));
                barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                barProgressDialog.setProgress(0);
                barProgressDialog.setMax(intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_COEFF_SIZE, 0));
                barProgressDialog.setCancelable(false);
                barProgressDialog.show();
            } else {
                barProgressDialog.setProgress(barProgressDialog.getProgress() + intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_COEFF_SIZE, 0));
            }*/
        }
    }

    /**
     * Custom receiver for receiving calibration matrix data. When this receiver action complete, it
     * will request the active configuration so that it can be displayed in the listview
     */
    public class requestCalMatrixReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "requestCalMatrixReceiver says, requesting calMatrix");
            intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE, 0);
            Boolean size = intent.getBooleanExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE_PACKET, false);
            if (size) {
//                barProgressDialog.dismiss();
//                barProgressDialog = new ProgressDialog(NewScanActivity.this);
//
//                barProgressDialog.setTitle(getString(R.string.dl_cal_matrix));
//                barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                barProgressDialog.setProgress(0);
//                barProgressDialog.setMax(intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE, 0));
//                barProgressDialog.setCancelable(false);
//                barProgressDialog.show();
                calMatSize = intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE, 0);
                calMatRecieveProgress=0;
            } else {
                //barProgressDialog.setProgress(barProgressDialog.getProgress() + intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE, 0));
                calMatRecieveProgress+=intent.getIntExtra(KSTNanoSDK.EXTRA_REF_CAL_MATRIX_SIZE, 0);
            }
            //if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
            if(calMatRecieveProgress==calMatSize){

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(KSTNanoSDK.REQUEST_ACTIVE_CONF));
            }
        }
    }

    private class ScanConfReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] smallArray = intent.getByteArrayExtra(KSTNanoSDK.EXTRA_DATA);
            byte[] addArray = new byte[smallArray.length * 3];
            byte[] largeArray = new byte[smallArray.length + addArray.length];

            System.arraycopy(smallArray, 0, largeArray, 0, smallArray.length);
            System.arraycopy(addArray, 0, largeArray, smallArray.length, addArray.length);

            Log.w("_JNI", "largeArray Size: " + largeArray.length);
            KSTNanoSDK.ScanConfiguration scanConf = KSTNanoSDK.KSTNanoSDK_dlpSpecScanReadConfiguration(intent.getByteArrayExtra(KSTNanoSDK.EXTRA_DATA));
            //KSTNanoSDK.ScanConfiguration scanConf = KSTNanoSDK.KSTNanoSDK_dlpSpecScanReadConfiguration(largeArray);

            activeConf = scanConf;

            Log.d(TAG, "ScanConfReceiver says, config: " + scanConf.toString());
            /*barProgressDialog.dismiss();
            btn_scan.setClickable(true);
            btn_scan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.kst_red));
            mMenu.findItem(R.id.action_settings).setEnabled(true);*/

            SettingsManager.storeStringPref(mContext, SettingsManager.SharedPreferencesKeys.scanConfiguration, scanConf.getConfigName());
            //tv_scan_conf.setText(scanConf.getConfigName());
            readyToScan = true;
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK,"ready to scan");
            pluginResult.setKeepCallback(false);
            mcallbackContext.sendPluginResult(pluginResult);

        }
    }

    /**
     * Broadcast Receiver handling the disconnect event. If the Nano disconnects,
     * this activity should finish so that the user is taken back to the {@link //ScanListActivity}
     * and display a toast message
     */
    public class DisconnReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "DisconnReceiver says, disconnecting :( :(");
            Toast.makeText(mContext, "Nano disconnected", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
}
