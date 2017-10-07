//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kstechnologies.nirscannanolibrary;

import com.google.gson.GsonBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import com.kstechnologies.nirscannanolibrary.SettingsManager;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class KSTNanoSDK {
    private static final String TAG = "KSTNanoSDK";
    public static BluetoothGatt mBluetoothGatt;
    public static BluetoothAdapter mBluetoothAdapter;
    public static final String ACTION_GATT_CONNECTED = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_NOTIFY_DONE = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_NOTIFY_DONE";
    public static final String EXTRA_DATA = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_DATA";
    public static final String SEND_DATA = "com.kstechnologies.NanoScan.bluetooth.le.SEND_DATA";
    public static final String SCAN_DATA = "com.kstechnologies.NanoScan.bluetooth.le.SCAN_DATA";
    public static final String REF_CONF_DATA = "com.kstechnologies.NanoScan.bluetooth.le.REF_CONF_DATA";
    public static final String SCAN_CONF_DATA = "com.kstechnologies.NanoScan.bluetooth.le.SCAN_CONF_DATA";
    public static final String STORED_SCAN_DATA = "com.kstechnologies.NanoScan.bluetooth.le.STORED_SCAN_DATA";
    public static final String EXTRA_REF_COEF_DATA = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_REF_COEF_DATA";
    public static final String EXTRA_REF_MATRIX_DATA = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_REF_MATRIX_DATA";
    public static final String EXTRA_SCAN_NAME = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SCAN_NAME";
    public static final String EXTRA_SCAN_TYPE = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SCAN_TYPE";
    public static final String EXTRA_SCAN_DATE = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SCAN_DATE";
    public static final String EXTRA_SCAN_INDEX = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SCAN_INDEX";
    public static final String EXTRA_INDEX_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_INDEX_SIZE";
    public static final String EXTRA_CONF_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_CONF_SIZE";
    public static final String EXTRA_ACTIVE_CONF = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_ACTIVE_CONF";
    public static final String EXTRA_SCAN_FMT_VER = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SCAN_FMT_VER";
    public static final String GET_INFO = "com.kstechnologies.NanoScan.bluetooth.le.GET_INFO";
    public static final String GET_STATUS = "com.kstechnologies.NanoScan.bluetooth.le.GET_STATUS";
    public static final String GET_SCAN_CONF = "com.kstechnologies.NanoScan.bluetooth.le.GET_SCAN_CONF";
    public static final String GET_STORED_SCANS = "com.kstechnologies.NanoScan.bluetooth.le.GET_STORED_SCANS";
    public static final String SET_TIME = "com.kstechnologies.NanoScan.bluetooth.le.SET_TIME";
    public static final String START_SCAN = "com.kstechnologies.NanoScan.bluetooth.le.START_SCAN";
    public static final String DELETE_SCAN = "com.kstechnologies.NanoScan.bluetooth.le.DELETE_SCAN";
    public static final String SD_SCAN_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.SD_SCAN_SIZE";
    public static final String SCAN_CONF_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.SCAN_CONF_SIZE";
    public static final String GET_ACTIVE_CONF = "com.kstechnologies.NanoScan.bluetooth.le.GET_ACTIVE_CONF";
    public static final String SET_ACTIVE_CONF = "com.kstechnologies.NanoScan.bluetooth.le.SET_ACTIVE_CONF";
    public static final String SEND_ACTIVE_CONF = "com.kstechnologies.NanoScan.bluetooth.le.SEND_ACTIVE_CONF";
    public static final String UPDATE_THRESHOLD = "com.kstechnologies.NanoScan.bluetooth.le.UPDATE_THRESHOLD";
    public static final String REQUEST_ACTIVE_CONF = "com.kstechnologies.NanoScan.bluetooth.le.REQUEST_ACTIVE_CONF";
    public static final String ACTION_INFO = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_INFO";
    public static final String ACTION_STATUS = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_STATUS";
    public static final String EXTRA_MANUF_NAME = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_MANUF_NAME";
    public static final String EXTRA_MODEL_NUM = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_MODEL_NUM";
    public static final String EXTRA_SERIAL_NUM = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SERIAL_NUM";
    public static final String EXTRA_HW_REV = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_HW_REV";
    public static final String EXTRA_TIVA_REV = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_TIVA_REV";
    public static final String EXTRA_SPECTRUM_REV = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_SPECTRUM_REV";
    public static final String EXTRA_BATT = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_BATT";
    public static final String EXTRA_TEMP = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_TEMP";
    public static final String EXTRA_HUMID = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_HUMID";
    public static final String EXTRA_DEV_STATUS = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_DEV_STATUS";
    public static final String EXTRA_ERR_STATUS = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_ERR_STATUS";
    public static final String EXTRA_TEMP_THRESH = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_TEMP_THRESH";
    public static final String EXTRA_HUMID_THRESH = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_HUMID_THRESH";
    public static final String ACTION_REQ_CAL_COEFF = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_REQ_CAL_COEFF";
    public static final String ACTION_REQ_CAL_MATRIX = "com.kstechnologies.NanoScan.bluetooth.le.ACTION_REQ_CAL_MATRIX";
    public static final String EXTRA_REF_CAL_COEFF_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.REF_CAL_COEFF_SIZE";
    public static final String EXTRA_REF_CAL_MATRIX_SIZE = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_REF_CAL_MATRIX_SIZE";
    public static final String EXTRA_REF_CAL_COEFF_SIZE_PACKET = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_REF_CAL_COEFF_SIZE_PACKET";
    public static final String EXTRA_REF_CAL_MATRIX_SIZE_PACKET = "com.kstechnologies.NanoScan.bluetooth.le.EXTRA_REF_CAL_MATRIX_SIZE_PACKET";

    public KSTNanoSDK() {
    }

    public static native Object dlpSpecScanInterpReference(byte[] var0, byte[] var1, byte[] var2);

    public static native Object dlpSpecScanReadConfiguration(byte[] var0);
    //public static native Object testNativeFunc();
    public static KSTNanoSDK.ScanResults KSTNanoSDK_dlpSpecScanInterpReference(byte[] data, byte[] coeff, byte[] matrix) {
//        Log.d("KSTnanoSDK", "calling the test native func");
//        testNativeFunc();
        return (KSTNanoSDK.ScanResults)dlpSpecScanInterpReference(data, coeff, matrix);
    }

    public static KSTNanoSDK.ScanConfiguration KSTNanoSDK_dlpSpecScanReadConfiguration(byte[] data) {
        return (KSTNanoSDK.ScanConfiguration)dlpSpecScanReadConfiguration(data);
    }

    private static boolean characteristicError() {
        if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISHardwareRev == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_HW_REV.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISManufName == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_MANUF_NAME.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISModelNumber == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_MODEL_NUMBER.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSerialNumber == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_SERIAL_NUMBER.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSpectrumCRev == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_SPECC_REV.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISTivaFirmwareRev == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.DIS_TIVA_FW_REV.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharBASBattLevel == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.BAS_BATT_LVL.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISBatteryRechargeCycles == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_NUM_BATT_RECHARGE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISDevStatus == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_DEV_STATUS.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISErrorStatus == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_ERR_STATUS.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHoursOfUse == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_HOURS_OF_USE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidMeasurement == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_HUMID_MEASUREMENT.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidThreshold == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_HUMID_THRESH.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISLampHours == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_LAMP_HOURS.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempMeasurement == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_TEMP_MEASUREMENT.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempThreshold == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GGIS_TEMP_THRESH.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGDTSTime == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GDTS_TIME.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalCoefficients == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GCIS_REQ_REF_CAL_COEFF.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalMatrix == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GCIS_REQ_REF_CAL_MATRIX.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqSpecCalCoefficients == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GCIS_REQ_SPEC_CAL_COEFF.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalCoefficients == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GCIS_RET_REF_CAL_COEFF.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalMatrix == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GCIS_RET_REF_CAL_MATRIX.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISActiveScanConf == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_ACTIVE_SCAN_CONF.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISNumberStoredConf == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_NUM_STORED_CONF.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqScanConfData == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_REQ_SCAN_CONF_DATA.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqStoredConfList == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_REQ_STORED_CONF_LIST.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetScanConfData == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_RET_SCAN_CONF_DATA.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetStoredConfList == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSCIS_RET_STORED_CONF_LIST.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISNumberSDStoredScans == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_NUM_SD_STORED_SCANS.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesList == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_SD_STORED_SCAN_IND_LIST.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesListData == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_SD_STORED_SCAN_IND_LIST_DATA.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSetScanNameStub == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_SET_SCAN_NAME_STUB.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanWrite == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_START_SCAN.toString() + " (Write)");
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanNotify == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_START_SCAN.toString() + " (Notify)");
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanWrite == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_CLEAR_SCAN.toString() + " (Write)");
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanNotify == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_CLEAR_SCAN.toString() + " (Notify)");
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanName == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_NAME.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanName == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_NAME.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanType == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_TYPE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanType == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_TYPE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanDate == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_DATE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanDate == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_DATE.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqPacketFormatVersion == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_REQ_PKT_FMT_VER.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetPacketFormatVersion == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_RET_PKT_FMT_VER.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqSerialScanDataStruct == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_REQ_SER_SCAN_DATA_STRUCT.toString());
            return true;
        } else if(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetSerialScanDataStruct == null) {
            Log.e("KSTNanoSDK", "Failed to enumerate UUID:" + KSTNanoSDK.NanoGATT.GSDIS_RET_SER_SCAN_DATA_STRUCT.toString());
            return true;
        } else {
            return false;
        }
    }

    public static void initialize() {
    }

    public static boolean enumerateServices(BluetoothGatt gatt) {
        List gattServices = mBluetoothGatt.getServices();
        Iterator error = gattServices.iterator();

        while(error.hasNext()) {
            BluetoothGattService gattService = (BluetoothGattService)error.next();
            List gattCharacteristics = gattService.getCharacteristics();
            Iterator var5 = gattCharacteristics.iterator();

            while(var5.hasNext()) {
                BluetoothGattCharacteristic gattCharacteristic = (BluetoothGattCharacteristic)var5.next();
                if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_MANUF_NAME) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISManufName = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_MODEL_NUMBER) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISModelNumber = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_SERIAL_NUMBER) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSerialNumber = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_HW_REV) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISHardwareRev = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_TIVA_FW_REV) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISTivaFirmwareRev = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.DIS_SPECC_REV) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSpectrumCRev = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.BAS_BATT_LVL) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharBASBattLevel = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_TEMP_MEASUREMENT) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempMeasurement = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_HUMID_MEASUREMENT) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidMeasurement = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_DEV_STATUS) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISDevStatus = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_ERR_STATUS) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISErrorStatus = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_TEMP_THRESH) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempThreshold = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_HUMID_THRESH) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidThreshold = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_HOURS_OF_USE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHoursOfUse = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_NUM_BATT_RECHARGE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISBatteryRechargeCycles = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_LAMP_HOURS) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISLampHours = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GGIS_ERR_LOG) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISErrorLog = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GDTS_TIME) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGDTSTime = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_REQ_SPEC_CAL_COEFF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqSpecCalCoefficients = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_RET_SPEC_CAL_COEFF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetSpecCalCoefficients = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_REQ_REF_CAL_COEFF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalCoefficients = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_RET_REF_CAL_COEFF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalCoefficients = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalCoefficients, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_REQ_REF_CAL_MATRIX) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalMatrix = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GCIS_RET_REF_CAL_MATRIX) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalMatrix = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISRetRefCalMatrix, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_NUM_STORED_CONF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISNumberStoredConf = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_REQ_STORED_CONF_LIST) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqStoredConfList = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_RET_STORED_CONF_LIST) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetStoredConfList = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetStoredConfList, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_REQ_SCAN_CONF_DATA) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqScanConfData = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_RET_SCAN_CONF_DATA) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetScanConfData = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISRetScanConfData, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSCIS_ACTIVE_SCAN_CONF) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISActiveScanConf = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_NUM_SD_STORED_SCANS) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISNumberSDStoredScans = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_SD_STORED_SCAN_IND_LIST) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesList = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_SD_STORED_SCAN_IND_LIST_DATA) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesListData = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesListData, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_SET_SCAN_NAME_STUB) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSetScanNameStub = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_START_SCAN) == 0) {
                    if(gattCharacteristic.getProperties() == 8) {
                        KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanWrite = gattCharacteristic;
                    } else if(gattCharacteristic.getProperties() == 16) {
                        KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanNotify = gattCharacteristic;
                        gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanNotify, true);
                    }
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_CLEAR_SCAN) == 0) {
                    if(gattCharacteristic.getProperties() == 8) {
                        KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanWrite = gattCharacteristic;
                    } else if(gattCharacteristic.getProperties() == 16) {
                        KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanNotify = gattCharacteristic;
                        gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanNotify, true);
                    }
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_NAME) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanName = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_NAME) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanName = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanName, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_TYPE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanType = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_TYPE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanType = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanType, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_REQ_SCAN_DATE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanDate = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_RET_SCAN_DATE) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanDate = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetScanDate, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_REQ_PKT_FMT_VER) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqPacketFormatVersion = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_RET_PKT_FMT_VER) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetPacketFormatVersion = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetPacketFormatVersion, true);
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_REQ_SER_SCAN_DATA_STRUCT) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqSerialScanDataStruct = gattCharacteristic;
                } else if(gattCharacteristic.getUuid().compareTo(KSTNanoSDK.NanoGATT.GSDIS_RET_SER_SCAN_DATA_STRUCT) == 0) {
                    KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetSerialScanDataStruct = gattCharacteristic;
                    gatt.setCharacteristicNotification(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISRetSerialScanDataStruct, true);
                }
            }
        }

        boolean error1 = characteristicError();
        return !error1;
    }

    public static void setStub(byte[] data) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSetScanNameStub, data);
    }

    public static void startScan(byte[] saveToSD) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISStartScanWrite, saveToSD);
    }

    public static void setTime() {
        Calendar currentTime = Calendar.getInstance();
        byte[] dateBytes = new byte[]{(byte)(currentTime.get(1) - 2000), (byte)(currentTime.get(2) + 1), (byte)currentTime.get(5), (byte)(currentTime.get(7) - 1), (byte)currentTime.get(11), (byte)currentTime.get(12), (byte)currentTime.get(13)};
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGDTSTime, dateBytes);
    }

    public static void setTemperatureThreshold(byte[] threshold) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempThreshold, threshold);
    }

    public static void setHumidityThreshold(byte[] threshold) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidThreshold, threshold);
    }

    public static void setActiveConf(byte[] index) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISActiveScanConf, index);
    }

    public static void getActiveConf() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISActiveScanConf);
    }

    public static void deleteScan(byte[] index) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISClearScanWrite, index);
    }

    public static void getModelNumber() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISModelNumber);
    }

    public static void getSerialNumber() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSerialNumber);
    }

    public static void getHardwareRev() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISHardwareRev);
    }

    public static void getFirmwareRev() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISTivaFirmwareRev);
    }

    public static void getSpectrumCRev() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISSpectrumCRev);
    }

    public static void getTemp() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISTempMeasurement);
    }

    public static void getHumidity() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISHumidMeasurement);
    }

    public static void getManufacturerName() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharDISManufName);
    }

    public static void getBatteryLevel() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharBASBattLevel);
    }

    public static void getNumberStoredConfigurations() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISNumberStoredConf);
    }

    public static void getNumberStoredScans() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISNumberSDStoredScans);
    }

    public static void getDeviceStatus() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISDevStatus);
    }

    public static void getErrorStatus() {
        readCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGGISErrorStatus);
    }

    public static void requestStoredConfigurationList() {
        byte[] writeData = new byte[]{0};
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqStoredConfList, writeData);
    }

    public static void requestScanName(byte[] index) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanName, index);
    }

    public static void requestScanConfiguration(byte[] index) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSCISReqScanConfData, index);
    }

    public static void requestRefCalCoefficients() {
        byte[] data = new byte[]{0};
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalCoefficients, data);
    }

    public static void requestRefCalMatrix() {
        byte[] data = new byte[]{0};
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGCISReqRefCalMatrix, data);
    }

    public static void requestScanIndicesList() {
        byte[] writeData = new byte[]{0};
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISSDStoredScanIndicesList, writeData);
    }

    public static void requestScanDate(byte[] scanIndex) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanDate, scanIndex);
    }

    public static void requestScanType(byte[] scanIndex) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqScanType, scanIndex);
    }

    public static void requestPacketFormatVersion(byte[] scanIndex) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqPacketFormatVersion, scanIndex);
    }

    public static void requestSerializedScanDataStruct(byte[] scanIndex) {
        writeCharacteristic(KSTNanoSDK.NanoGattCharacteristic.mBleGattCharGSDISReqSerialScanDataStruct, scanIndex);
    }

    public static void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        if(characteristic == null) {
            Log.e("KSTNanoSDK", "Error writing NULL characteristic");
        } else {
            characteristic.setValue(data);
            if(mBluetoothAdapter != null && mBluetoothGatt != null) {
                mBluetoothGatt.writeCharacteristic(characteristic);
            } else {
                Log.e("KSTNanoSDK", "ERROR: mBluetoothAdapter is null");
            }
        }
    }

    public static void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if(mBluetoothAdapter != null && mBluetoothGatt != null) {
            if(characteristic != null) {
                mBluetoothGatt.readCharacteristic(characteristic);
            } else {
                Log.e("KSTNanoSDK", "ERROR: Reading NULL characteristic");
            }

        }
    }

    static {
        System.loadLibrary("dlpspectrum");
    }

    public static class SlewScanSection implements Serializable {
        byte sectionScanType;
        byte widthPx;
        int wavelengthStartNm;
        int wavelengthEndNm;
        int numPatterns;
        int numRepeats;
        int exposureTime;

        public SlewScanSection(byte sectionScanType, byte widthPx, int wavelengthStartNm, int wavelengthEndNm, int numPatterns, int numRepeats, int exposureTime) {
            this.sectionScanType = sectionScanType;
            this.widthPx = widthPx;
            this.wavelengthStartNm = wavelengthStartNm;
            this.wavelengthEndNm = wavelengthEndNm;
            this.numPatterns = numPatterns;
            this.numRepeats = numRepeats;
            this.exposureTime = exposureTime;
        }

        public int getExposureTime() {
            return this.exposureTime;
        }

        public void setExposureTime(int exposureTime) {
            this.exposureTime = exposureTime;
        }

        public int getNumPatterns() {
            return this.numPatterns;
        }

        public void setNumPatterns(int numPatterns) {
            this.numPatterns = numPatterns;
        }

        public int getNumRepeats() {
            return this.numRepeats;
        }

        public void setNumRepeats(int numRepeats) {
            this.numRepeats = numRepeats;
        }

        public byte getSectionScanType() {
            return this.sectionScanType;
        }

        public void setSectionScanType(byte sectionScanType) {
            this.sectionScanType = sectionScanType;
        }

        public int getWavelengthEndNm() {
            return this.wavelengthEndNm;
        }

        public void setWavelengthEndNm(int wavelengthEndNm) {
            this.wavelengthEndNm = wavelengthEndNm;
        }

        public int getWavelengthStartNm() {
            return this.wavelengthStartNm;
        }

        public void setWavelengthStartNm(int wavelengthStartNm) {
            this.wavelengthStartNm = wavelengthStartNm;
        }

        public byte getWidthPx() {
            return this.widthPx;
        }

        public void setWidthPx(byte widthPx) {
            this.widthPx = widthPx;
        }
    }

    public static class ScanListManager {
        private String infoTitle;
        private String infoBody;

        public ScanListManager(String infoTitle, String infoBody) {
            this.infoTitle = infoTitle;
            this.infoBody = infoBody;
        }

        public String getInfoTitle() {
            return this.infoTitle;
        }

        public String getInfoBody() {
            return this.infoBody;
        }
    }

    public static class ScanConfiguration implements Serializable {
        private static final int SCAN_CFG_FILENAME_LEN = 8;
        int scanType;
        int scanConfigIndex;
        byte[] scanConfigSerialNumber;
        byte[] configName;
        int wavelengthStartNm;
        int wavelengthEndNm;
        int widthPx;
        int numPatterns;
        int numRepeats;
        boolean active;
        byte[] sectionScanType;
        byte[] sectionWidthPx;
        int[] sectionWavelengthStartNm;
        int[] sectionWavelengthEndNm;
        int[] sectionNumPatterns;
        int[] sectionNumRepeats;
        int[] sectionExposureTime;
        byte numSections;

        public ScanConfiguration(int scanType, int scanConfigIndex, byte[] scanConfigSerialNumber, byte[] configName, int wavelengthStartNm, int wavelengthEndNm, int widthPx, int numPatterns, int numRepeats) {
            this.scanType = scanType;
            this.scanConfigIndex = scanConfigIndex;
            this.scanConfigSerialNumber = scanConfigSerialNumber;
            this.configName = configName;
            this.wavelengthStartNm = wavelengthStartNm;
            this.wavelengthEndNm = wavelengthEndNm;
            this.widthPx = widthPx;
            this.numPatterns = numPatterns;
            this.numRepeats = numRepeats;
        }

        public ScanConfiguration(int scanType, int scanConfigIndex, byte[] scanConfigSerialNumber, byte[] configName, byte numSections, byte[] sectionScanType, byte[] sectionWidthPx, int[] sectionWavelengthStartNm, int[] sectionWavelengthEndNm, int[] sectionNumPatterns, int[] sectionNumRepeats, int[] sectionExposureTime) {
            this.scanType = scanType;
            this.scanConfigIndex = scanConfigIndex;
            this.scanConfigSerialNumber = scanConfigSerialNumber;
            this.configName = configName;
            this.sectionScanType = sectionScanType;
            this.sectionWidthPx = sectionWidthPx;
            this.sectionWavelengthStartNm = sectionWavelengthStartNm;
            this.sectionWavelengthEndNm = sectionWavelengthEndNm;
            this.sectionNumPatterns = sectionNumPatterns;
            this.sectionNumRepeats = sectionNumRepeats;
            this.sectionExposureTime = sectionExposureTime;
            this.numSections = numSections;
        }

        public String getScanType() {
            return this.scanType == 1?"Hadamard":(this.scanType == 2?"Slew":"Column");
        }

        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setScanType(int scanType) {
            this.scanType = scanType;
        }

        public int getScanConfigIndex() {
            return this.scanConfigIndex;
        }

        public void setScanConfigIndex(int scanConfigIndex) {
            this.scanConfigIndex = scanConfigIndex;
        }

        public String getScanConfigSerialNumber() {
            String s = null;

            try {
                s = new String(this.scanConfigSerialNumber, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }

            return s;
        }

        public void setScanConfigSerialNumber(byte[] scanConfigSerialNumber) {
            this.scanConfigSerialNumber = scanConfigSerialNumber;
        }

        public String getConfigName() {
            byte[] byteChars = new byte[40];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] s = byteChars;
            int e = byteChars.length;

            for(int var5 = 0; var5 < e; ++var5) {
                byte b = s[var5];
                byteChars[b] = 0;
            }

            String var8 = null;

            for(e = 0; e < this.configName.length; ++e) {
                byteChars[e] = this.configName[e];
                if(this.configName[e] == 0) {
                    break;
                }

                os.write(this.configName[e]);
            }

            try {
                var8 = new String(os.toByteArray(), "UTF-8");
            } catch (UnsupportedEncodingException var7) {
                var7.printStackTrace();
            }

            return var8;
        }

        public void setConfigName(byte[] configName) {
            this.configName = configName;
        }

        public int getWavelengthStartNm() {
            return this.wavelengthStartNm;
        }

        public void setWavelengthStartNm(int wavelengthStartNm) {
            this.wavelengthStartNm = wavelengthStartNm;
        }

        public int getWavelengthEndNm() {
            return this.wavelengthEndNm;
        }

        public void setWavelengthEndNm(int wavelengthEndNm) {
            this.wavelengthEndNm = wavelengthEndNm;
        }

        public int getWidthPx() {
            return this.widthPx;
        }

        public void setWidthPx(int widthPx) {
            this.widthPx = widthPx;
        }

        public int getNumPatterns() {
            return this.numPatterns;
        }

        public void setNumPatterns(int numPatterns) {
            this.numPatterns = numPatterns;
        }

        public int getNumRepeats() {
            return this.numRepeats;
        }

        public void setNumRepeats(int numRepeats) {
            this.numRepeats = numRepeats;
        }

        public int[] getSectionExposureTime() {
            return this.sectionExposureTime;
        }

        public void setSectionExposureTime(int[] sectionExposureTime) {
            this.sectionExposureTime = sectionExposureTime;
        }

        public int[] getSectionNumPatterns() {
            return this.sectionNumPatterns;
        }

        public void setSectionNumPatterns(int[] sectionNumPatterns) {
            this.sectionNumPatterns = sectionNumPatterns;
        }

        public int[] getSectionNumRepeats() {
            return this.sectionNumRepeats;
        }

        public void setSectionNumRepeats(int[] sectionNumRepeats) {
            this.sectionNumRepeats = sectionNumRepeats;
        }

        public byte[] getSectionScanType() {
            return this.sectionScanType;
        }

        public void setSectionScanType(byte[] sectionScanType) {
            this.sectionScanType = sectionScanType;
        }

        public int[] getSectionWavelengthEndNm() {
            return this.sectionWavelengthEndNm;
        }

        public void setSectionWavelengthEndNm(int[] sectionWavelengthEndNm) {
            this.sectionWavelengthEndNm = sectionWavelengthEndNm;
        }

        public int[] getSectionWavelengthStartNm() {
            return this.sectionWavelengthStartNm;
        }

        public void setSectionWavelengthStartNm(int[] sectionWavelengthStartNm) {
            this.sectionWavelengthStartNm = sectionWavelengthStartNm;
        }

        public byte[] getSectionWidthPx() {
            return this.sectionWidthPx;
        }

        public void setSectionWidthPx(byte[] sectionWidthPx) {
            this.sectionWidthPx = sectionWidthPx;
        }

        public byte getSlewNumSections() {
            return this.numSections;
        }

        public void setSlewNumSections(byte numSections) {
            this.numSections = numSections;
        }
    }

    public static class ScanResults {
        double[] wavelength;
        int[] intensity;
        int[] uncalibratedIntensity;
        int length;

        public double[] getWavelength() {
            return this.wavelength;
        }

        public void setWavelength(double[] wavelength) {
            this.wavelength = wavelength;
        }

        public int[] getIntensity() {
            return this.intensity;
        }

        public void setIntensity(int[] intensity) {
            this.intensity = intensity;
        }

        public int getLength() {
            return this.length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int[] getUncalibratedIntensity() {
            return this.uncalibratedIntensity;
        }

        public void setUncalibratedIntensity(int[] uncalibratedIntensity) {
            this.uncalibratedIntensity = uncalibratedIntensity;
        }

        public ScanResults(double[] wavelength, int[] intensity, int[] uncalibratedIntensity, int length) {
            this.wavelength = wavelength;
            this.intensity = intensity;
            this.uncalibratedIntensity = uncalibratedIntensity;
            this.length = length;
        }

        public static double getSpatialFreq(Context ctx, double wavelength) {
            return SettingsManager.getBooleanPref(ctx, "spatialFreq", true)?wavelength:1.0E7D / wavelength;
        }

        public String toJSONString() {
            return new GsonBuilder().create().toJson(this, ScanResults.class);
        }
    }

    public static class ReferenceCalibration implements Serializable {
        public static final String REF_FILENAME = "refcals";
        public static ArrayList<KSTNanoSDK.ReferenceCalibration> currentCalibration;
        private byte[] refCalCoefficients;
        private byte[] refCalMatrix;

        public ReferenceCalibration(byte[] refCalCoefficients, byte[] refCalMatrix) {
            this.refCalCoefficients = refCalCoefficients;
            this.refCalMatrix = refCalMatrix;
        }

        public static boolean refCalFileExists(Context context) {
            ObjectInputStream in;
            try {
                FileInputStream fis = context.openFileInput("refcals");
                in = new ObjectInputStream(fis);
            } catch (IOException var5) {
                var5.printStackTrace();
                return false;
            }

            try {
                in.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return true;
        }

        public byte[] getRefCalCoefficients() {
            return this.refCalCoefficients;
        }

        public byte[] getRefCalMatrix() {
            return this.refCalMatrix;
        }

        public static boolean writeRefCalFile(Context context, ArrayList<KSTNanoSDK.ReferenceCalibration> list) {
            currentCalibration = list;
            ObjectOutputStream out = null;

            try {
                FileOutputStream fos = context.openFileOutput("refcals", 0);
                out = new ObjectOutputStream(fos);
                Iterator ex = list.iterator();

                while(ex.hasNext()) {
                    KSTNanoSDK.ReferenceCalibration e = (KSTNanoSDK.ReferenceCalibration)ex.next();
                    out.writeObject(e);
                }

                out.close();
                return true;
            } catch (IOException var7) {
                var7.printStackTrace();
                Log.e("__REFS", "IO exception when writing groups file: " + var7);

                try {
                    assert out != null;

                    out.close();
                } catch (IOException var6) {
                    var6.printStackTrace();
                    Log.e("__REFS", "IO exception when closing groups file: " + var6);
                }

                return false;
            }
        }
    }

    public static class NanoDevice {
        private BluetoothDevice device;
        private int rssi;
        byte[] scanRecord;
        private String nanoName;
        private String nanoMac;

        public NanoDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            this.device = device;
            this.rssi = rssi;
            this.scanRecord = scanRecord;
            this.nanoName = device.getName();
            this.nanoMac = device.getAddress();
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public String getRssiString() {
            return String.valueOf(this.rssi);
        }

        public String getNanoName() {
            return this.nanoName;
        }

        public String getNanoMac() {
            return this.nanoMac;
        }
    }

    public static class NanoGattCharacteristic {
        public static BluetoothGattCharacteristic mBleGattCharDISManufName;
        public static BluetoothGattCharacteristic mBleGattCharDISModelNumber;
        public static BluetoothGattCharacteristic mBleGattCharDISSerialNumber;
        public static BluetoothGattCharacteristic mBleGattCharDISHardwareRev;
        public static BluetoothGattCharacteristic mBleGattCharDISTivaFirmwareRev;
        public static BluetoothGattCharacteristic mBleGattCharDISSpectrumCRev;
        public static BluetoothGattCharacteristic mBleGattCharBASBattLevel;
        public static BluetoothGattCharacteristic mBleGattCharGGISTempMeasurement;
        public static BluetoothGattCharacteristic mBleGattCharGGISHumidMeasurement;
        public static BluetoothGattCharacteristic mBleGattCharGGISDevStatus;
        public static BluetoothGattCharacteristic mBleGattCharGGISErrorStatus;
        public static BluetoothGattCharacteristic mBleGattCharGGISTempThreshold;
        public static BluetoothGattCharacteristic mBleGattCharGGISHumidThreshold;
        public static BluetoothGattCharacteristic mBleGattCharGGISHoursOfUse;
        public static BluetoothGattCharacteristic mBleGattCharGGISBatteryRechargeCycles;
        public static BluetoothGattCharacteristic mBleGattCharGGISLampHours;
        public static BluetoothGattCharacteristic mBleGattCharGGISErrorLog;
        public static BluetoothGattCharacteristic mBleGattCharGDTSTime;
        public static BluetoothGattCharacteristic mBleGattCharGCISReqSpecCalCoefficients;
        public static BluetoothGattCharacteristic mBleGattCharGCISRetSpecCalCoefficients;
        public static BluetoothGattCharacteristic mBleGattCharGCISReqRefCalCoefficients;
        public static BluetoothGattCharacteristic mBleGattCharGCISRetRefCalCoefficients;
        public static BluetoothGattCharacteristic mBleGattCharGCISReqRefCalMatrix;
        public static BluetoothGattCharacteristic mBleGattCharGCISRetRefCalMatrix;
        public static BluetoothGattCharacteristic mBleGattCharGSCISNumberStoredConf;
        public static BluetoothGattCharacteristic mBleGattCharGSCISReqStoredConfList;
        public static BluetoothGattCharacteristic mBleGattCharGSCISRetStoredConfList;
        public static BluetoothGattCharacteristic mBleGattCharGSCISReqScanConfData;
        public static BluetoothGattCharacteristic mBleGattCharGSCISRetScanConfData;
        public static BluetoothGattCharacteristic mBleGattCharGSCISActiveScanConf;
        public static BluetoothGattCharacteristic mBleGattCharGSDISNumberSDStoredScans;
        public static BluetoothGattCharacteristic mBleGattCharGSDISSDStoredScanIndicesList;
        public static BluetoothGattCharacteristic mBleGattCharGSDISSDStoredScanIndicesListData;
        public static BluetoothGattCharacteristic mBleGattCharGSDISSetScanNameStub;
        public static BluetoothGattCharacteristic mBleGattCharGSDISStartScanWrite;
        public static BluetoothGattCharacteristic mBleGattCharGSDISStartScanNotify;
        public static BluetoothGattCharacteristic mBleGattCharGSDISClearScanWrite;
        public static BluetoothGattCharacteristic mBleGattCharGSDISClearScanNotify;
        public static BluetoothGattCharacteristic mBleGattCharGSDISReqScanName;
        public static BluetoothGattCharacteristic mBleGattCharGSDISRetScanName;
        public static BluetoothGattCharacteristic mBleGattCharGSDISReqScanType;
        public static BluetoothGattCharacteristic mBleGattCharGSDISRetScanType;
        public static BluetoothGattCharacteristic mBleGattCharGSDISReqScanDate;
        public static BluetoothGattCharacteristic mBleGattCharGSDISRetScanDate;
        public static BluetoothGattCharacteristic mBleGattCharGSDISReqPacketFormatVersion;
        public static BluetoothGattCharacteristic mBleGattCharGSDISRetPacketFormatVersion;
        public static BluetoothGattCharacteristic mBleGattCharGSDISReqSerialScanDataStruct;
        public static BluetoothGattCharacteristic mBleGattCharGSDISRetSerialScanDataStruct;

        public NanoGattCharacteristic() {
        }
    }

    public static class NanoGATT {
        public static final UUID DIS_MANUF_NAME = UUID.fromString("00002A29-0000-1000-8000-00805F9B34FB");
        public static final UUID DIS_MODEL_NUMBER = UUID.fromString("00002A24-0000-1000-8000-00805F9B34FB");
        public static final UUID DIS_SERIAL_NUMBER = UUID.fromString("00002A25-0000-1000-8000-00805F9B34FB");
        public static final UUID DIS_HW_REV = UUID.fromString("00002A27-0000-1000-8000-00805F9B34FB");
        public static final UUID DIS_TIVA_FW_REV = UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB");
        public static final UUID DIS_SPECC_REV = UUID.fromString("00002A28-0000-1000-8000-00805F9B34FB");
        public static final UUID BAS_BATT_LVL = UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB");
        public static final UUID GGIS_TEMP_MEASUREMENT = UUID.fromString("43484101-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_HUMID_MEASUREMENT = UUID.fromString("43484102-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_DEV_STATUS = UUID.fromString("43484103-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_ERR_STATUS = UUID.fromString("43484104-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_TEMP_THRESH = UUID.fromString("43484105-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_HUMID_THRESH = UUID.fromString("43484106-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_HOURS_OF_USE = UUID.fromString("43484107-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_NUM_BATT_RECHARGE = UUID.fromString("43484108-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_LAMP_HOURS = UUID.fromString("43484109-444C-5020-4E49-52204E616E6F");
        public static final UUID GGIS_ERR_LOG = UUID.fromString("4348410A-444C-5020-4E49-52204E616E6F");
        public static final UUID GDTS_TIME = UUID.fromString("4348410C-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_REQ_SPEC_CAL_COEFF = UUID.fromString("4348410D-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_RET_SPEC_CAL_COEFF = UUID.fromString("4348412E-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_REQ_REF_CAL_COEFF = UUID.fromString("4348410F-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_RET_REF_CAL_COEFF = UUID.fromString("43484110-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_REQ_REF_CAL_MATRIX = UUID.fromString("43484111-444C-5020-4E49-52204E616E6F");
        public static final UUID GCIS_RET_REF_CAL_MATRIX = UUID.fromString("43484112-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_NUM_STORED_CONF = UUID.fromString("43484113-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_REQ_STORED_CONF_LIST = UUID.fromString("43484114-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_RET_STORED_CONF_LIST = UUID.fromString("43484115-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_REQ_SCAN_CONF_DATA = UUID.fromString("43484116-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_RET_SCAN_CONF_DATA = UUID.fromString("43484117-444C-5020-4E49-52204E616E6F");
        public static final UUID GSCIS_ACTIVE_SCAN_CONF = UUID.fromString("43484118-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_NUM_SD_STORED_SCANS = UUID.fromString("43484119-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_SD_STORED_SCAN_IND_LIST = UUID.fromString("4348411A-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_SD_STORED_SCAN_IND_LIST_DATA = UUID.fromString("4348411B-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_SET_SCAN_NAME_STUB = UUID.fromString("4348411C-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_START_SCAN = UUID.fromString("4348411D-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_CLEAR_SCAN = UUID.fromString("4348411E-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_REQ_SCAN_NAME = UUID.fromString("4348411F-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_RET_SCAN_NAME = UUID.fromString("43484120-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_REQ_SCAN_TYPE = UUID.fromString("43484121-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_RET_SCAN_TYPE = UUID.fromString("43484122-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_REQ_SCAN_DATE = UUID.fromString("43484123-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_RET_SCAN_DATE = UUID.fromString("43484124-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_REQ_PKT_FMT_VER = UUID.fromString("43484125-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_RET_PKT_FMT_VER = UUID.fromString("43484126-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_REQ_SER_SCAN_DATA_STRUCT = UUID.fromString("43484127-444C-5020-4E49-52204E616E6F");
        public static final UUID GSDIS_RET_SER_SCAN_DATA_STRUCT = UUID.fromString("43484128-444C-5020-4E49-52204E616E6F");

        public NanoGATT() {
        }
    }
}
