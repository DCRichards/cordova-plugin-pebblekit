package com.dcrichards.pebble.pebbleKitCordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataLogReceiver;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;

/**
 * A Cordova plugin for the PebbleKit Android SDK for the Pebble Smartwatch
 *
 * @author DCRichards
 * @version 0.1
 */
public class PebbleKitCordova extends CordovaPlugin {

    private PebbleDataLogReceiver dataLoggingReceiver;
    private CallbackContext currentCallbackContext;
    private static final UUID APP_ID = UUID.fromString("APP_UUID_HERE");

    /**
     * Constructor
     */
    public PebbleKitCordova() {
    }

    /**
     * Get the current application context
     *
     * @return application context
     */
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

        if (action.equals("registerDataLoggingReceiver")) {
            //store callback context for asynchronous communication
            currentCallbackContext = callbackContext;

            dataLoggingReceiver = new PebbleDataLogReceiver(APP_ID) {
                private JSONArray loggedData = new JSONArray();

                @Override
                public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
                    // for now we're just going to put the data, we'll format later
                    loggedData.put(data);
                }

                @Override
                public void onFinishSession(Context context, UUID logUuid, Long timestamp, Long tag) {
                    super.onFinishSession(context, logUuid, timestamp, tag);
                    // using PluginResult with setKeepCallback(true) and storing
                    // the context allows the callback to be called asynchronously
                    PluginResult result = new PluginResult(PluginResult.Status.OK, loggedData);
                    result.setKeepCallback(true);
                    currentCallbackContext.sendPluginResult(result);
                }
            };
            PebbleKit.registerDataLogReceiver(getApplicationContext(), dataLoggingReceiver);
            return true;
        }

        if (action.equals("unregisterDataLoggingReceiver")) {
            if (dataLoggingReceiver != null) {
                getApplicationContext().unregisterReceiver(dataLoggingReceiver);
                callbackContext.success();
            } else {
                callbackContext.error("Receiver not registered");
            }
            return true;
        }
        return false;
    }
}

