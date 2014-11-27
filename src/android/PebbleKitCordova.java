package com.dcrichards.pebble.pebbleKitCordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.getpebble.android.kit.PebbleKit;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Cordova plugin for the PebbleKit Android SDK for the Pebble Smartwatch
 *
 * @author DCRichards
 * @version 0.1
 */
public class PebbleKitCordova extends CordovaPlugin {

    private Context getApplicationContext() {
        return cordova.getActivity().getApplicationContext();
    }

    /**
     * Access methods through Cordova
     *
     * @param action            the method to call
     * @param args              an array of method arguments
     * @param callbackContext   the callback context
     * @return                  true if succeeded
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("getWatchFWVersion")) {
            PebbleKit.FirmwareVersionInfo pebbleFirmwareVersion = PebbleKit.getWatchFWVersion(this.getApplicationContext());
            JSONObject versionInfo = new JSONObject();
            versionInfo.put("version", pebbleFirmwareVersion.getTag());
            callbackContext.success(versionInfo);
            return true;
        }

        return false;
    }
}

