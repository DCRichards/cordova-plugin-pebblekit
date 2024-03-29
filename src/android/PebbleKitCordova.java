package com.dcrichards.pebble.pebbleKitCordova;

import com.getpebble.android.kit.util.PebbleDictionary;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataLogReceiver;
import com.getpebble.android.kit.PebbleKit.PebbleAckReceiver;
import com.getpebble.android.kit.PebbleKit.PebbleNackReceiver;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * A Cordova plugin for the PebbleKit Android SDK for the Pebble Smartwatch
 *
 * For more information and the full API refer to the PebbleKit documentation
 * at http://developer.getpebble.com/docs/android
 *
 * @author DCRichards
 * @version 0.1.0
 */
public class PebbleKitCordova extends CordovaPlugin {

    // Store listeners so callbacks are routed correctly
    private PebbleDataLogReceiver dataLoggingReceiver;
    private PebbleAckReceiver ackReceiver;
    private PebbleNackReceiver nackReceiver;
    private PebbleDataReceiver dataReceiver;
    private CallbackContext dataLoggingCallbackContext;
    private CallbackContext messageAckCallbackContext;
    private CallbackContext messageNackCallbackContext;
    private CallbackContext dataHandlerCallbackContext;

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
         * Check it the current watch is running a version which supports app messages
         **/
        if (action.equals("areAppMessagesSupported")) {
            JSONObject appMessageInfo = new JSONObject();
            boolean appMessageSupported = PebbleKit.areAppMessagesSupported(this.getApplicationContext());
            appMessageInfo.put("supported", appMessageSupported);
            callbackContext.success(appMessageInfo);
            return true;
        }

        /**
         * Send a message to the Pebble
         */
        if (action.equals("sendDataToPebble")) {
            UUID appUUID = UUID.fromString(args.getString(0));
            PebbleDictionary messageData = PebbleDictionary.fromJson(args.getString(1));
            PebbleKit.sendDataToPebble(this.getApplicationContext(), appUUID, messageData);
            callbackContext.success();
            return true;
        }

        /**
         * Send a message to the Pebble with a given transaction ID
         */
        if (action.equals("sendDataToPebbleWithTransactionId")) {
            UUID appUUID = UUID.fromString(args.getString(0));
            PebbleDictionary messageData;
            messageData = PebbleDictionary.fromJson(args.getString(1));
            int transactionId = args.getInt(2);
            PebbleKit.sendDataToPebbleWithTransactionId(this.getApplicationContext(), appUUID, messageData, transactionId);
            callbackContext.success();
            return true;
        }

        /**
         * Send a byte array to the Pebble
         */
        if (action.equals("sendBytesToPebble")) {
            UUID appUUID = UUID.fromString(args.getString(0));
            PebbleDictionary dict = new PebbleDictionary();
            int key = args.getInt(1);
            JSONArray bytes = args.getJSONArray(2);
            byte[] byteArray = new byte[bytes.length()];
            for (int i = 0; i < bytes.length(); i++) {
                byteArray[i] = (byte)bytes.getInt(i);
            }
            dict.addBytes(key,byteArray);
            PebbleKit.sendDataToPebble(this.getApplicationContext(), appUUID, dict);
            callbackContext.success();
            return true;
        }

        /**
         * Start a specified app on the Pebble
         */
        if (action.equals("startAppOnPebble")) {
            UUID appUUID = UUID.fromString(args.getString(0));
            PebbleKit.startAppOnPebble(this.getApplicationContext(), appUUID);
            callbackContext.success();
            return true;
        }

        /**
         * Register a callback for data logging
         */
        if (action.equals("registerDataLoggingReceiver")) {
            dataLoggingCallbackContext = callbackContext;
            UUID appUUID = UUID.fromString(args.getString(0));

            dataLoggingReceiver = new PebbleDataLogReceiver(appUUID) {
                private JSONArray loggedData = new JSONArray();

                @Override
                public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
                    loggedData.put(data);
                }

                @Override
                public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, int data) {
                    loggedData.put(data);
                }

                @Override
                public void onFinishSession(Context context, UUID logUuid, Long timestamp, Long tag) {
                    super.onFinishSession(context, logUuid, timestamp, tag);
                    // using PluginResult with setKeepCallback(true) and storing
                    // the context allows the callback to be called asynchronously
                    PluginResult result = new PluginResult(PluginResult.Status.OK, loggedData);
                    result.setKeepCallback(true);
                    dataLoggingCallbackContext.sendPluginResult(result);
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

        /**
         * Register a handler for ACK messages
         */
        if (action.equals("registerReceivedAckHandler")) {
            messageAckCallbackContext = callbackContext;
            UUID appUUID = UUID.fromString(args.getString(0));
            ackReceiver = new PebbleAckReceiver(appUUID) {

                @Override
                public void receiveAck(Context context, int transactionId) {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, transactionId);
                    result.setKeepCallback(true);
                    messageAckCallbackContext.sendPluginResult(result);
                }

            };
            PebbleKit.registerReceivedAckHandler(getApplicationContext(), ackReceiver);
            return true;
        }

        /**
         * Register a handler for NACK messages
         */
        if (action.equals("registerReceivedNackHandler")) {
            messageNackCallbackContext = callbackContext;
            UUID appUUID = UUID.fromString(args.getString(0));
            nackReceiver = new PebbleNackReceiver(appUUID) {

                @Override
                public void receiveNack(Context context, int transactionId) {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, transactionId);
                    result.setKeepCallback(true);
                    messageNackCallbackContext.sendPluginResult(result);

                }

            };

            PebbleKit.registerReceivedNackHandler(getApplicationContext(), nackReceiver);
            return true;
        }

        /**
         * Register a handler for recieving data from the Pebble 
         */
        if (action.equals("registerReceivedDataHandler")) {
            dataHandlerCallbackContext = callbackContext;
            UUID appUUID = UUID.fromString(args.getString(0));
            dataReceiver = new PebbleKit.PebbleDataReceiver(appUUID) {

                @Override
                public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                    // Pebble messages must always be ack'd
                    PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
                    PluginResult result = new PluginResult(PluginResult.Status.OK, data.toJsonString());
                    result.setKeepCallback(true);
                    dataHandlerCallbackContext.sendPluginResult(result);
                }

            };

            PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataReceiver);
            return true;
        }
        
        return false;
    }
}