package com.simonmacdonald.cordova.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.content.Context;
import android.telephony.TelephonyManager;

import android.provider.Settings;
import java.util.UUID;
import android.os.Build;
import android.os.Build.VERSION;


public class TelephoneNumber extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("get")) {
            TelephonyManager telephonyManager =
                (TelephonyManager)this.cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = telephonyManager.getSimSerialNumber();
            String line1Number = telephonyManager.getLine1Number();

            JSONObject json = new JSONObject();

            String androidId = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            String deviceId = telephonyManager.getDeviceId(); // << 32
            String unique = new UUID(androidId.hashCode(), deviceId.hashCode() << 32 | simSerialNumber.hashCode()).toString();
            String deviceIdRaw = telephonyManager.getDeviceId();
            String subscriberId = telephonyManager.getSubscriberId();
            String extraValue = (new StringBuilder("35")).append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10).append(Build.CPU_ABI.length() % 10).append(Build.DEVICE.length() % 10).append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10).append(Build.ID.length() % 10).append(Build.MANUFACTURER.length() % 10).append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10).append(Build.TAGS.length() % 10).append(Build.TYPE.length() % 10).append(Build.USER.length() % 10).toString();

            try{
            	json.put("simSerialNumber", simSerialNumber);
            	json.put("line1Number", line1Number);
            	json.put("unique", unique);
                json.put("deviceIdRaw", deviceIdRaw);
                json.put("subscriberId", subscriberId);                
                json.put("extraValue", extraValue);
            } catch(JSONException exc){}
            
           // String result = json.toString();
            if (json != null) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
               return true;
            }
        }
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
        return false;
    }
}

