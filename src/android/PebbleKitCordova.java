package com.dcrichards.pebble.pebbleKitCordova;

import android.util.Base64;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataLogReceiver;

import android.content.Context;
import android.util.Base64;
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

        /**
         * Get the current watch version as a version tag eg. v2.6
         */
        if (action.equals("getWatchFWVersion")) {
            PebbleKit.FirmwareVersionInfo pebbleFirmwareVersion = PebbleKit.getWatchFWVersion(this.getApplicationContext());
            JSONObject versionInfo = new JSONObject();
            versionInfo.put("version", pebbleFirmwareVersion.getTag());
            callbackContext.success(versionInfo);
            return true;
        }

        /**
         * Register a callback for data logging
         */
        if (action.equals("registerDataLoggingReceiver")) {
            currentCallbackContext = callbackContext;
            UUID appUUID = UUID.fromString(args.getString(0));

            dataLoggingReceiver = new PebbleDataLogReceiver(appUUID) {
                private JSONArray loggedData = new JSONArray();

                @Override
                public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
                    // encode to preserve byte array
                    String bytes = Base64.encodeToString(data, 0);
                    loggedData.put(bytes);
                }

                @Override
                public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, int data) {
                    //TODO: handle int data logging
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

        /**
         * Unregister a data logging callback
         */
        if (action.equals("unregisterDataLoggingReceiver")) {
            if (dataLoggingReceiver != null) {
                getApplicationContext().unregisterReceiver(dataLoggingReceiver);
                dataLoggingReceiver = null;
                callbackContext.success();
            } else {
                callbackContext.error("No registered receiver found");
            }
            return true;
        }
        
        /**
         * Request current data logs
        **/
        if (action.equals("requestDataLogsForApp")) {
            if (dataLoggingReceiver != null) {
                UUID appUUID = UUID.fromString(args.getString(0));
                PebbleKit.requestDataLogsForApp(getApplicationContext(), appUUID);
                callbackContext.success();
            } else {
                callbackContext.error("No registered data logging receiver found");
            }
            return true;
        }
        
        return false;
    }
}

