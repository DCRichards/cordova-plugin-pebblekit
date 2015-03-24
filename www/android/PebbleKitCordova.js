/**
 * Interface for PebbleKit Cordova Plugin
 *
 * @author DCRichards
 */

window.Pebble = {};

/**
 * Get version information about the currently connected Pebble
 *
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.getWatchFWVersion = function(succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'getWatchFWVersion', []);
};

/**
 * Check if the currently connected Pebble supports app messages
 *
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.areAppMessagesSupported = function(succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'areAppMessagesSupported', []);
};

/**
 * Start an app on the Pebble
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.startAppOnPebble = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'startAppOnPebble', [UUID]);
};

/**
 * Send data to a specific Pebble app.
 *
 * @param UUID      the UUID of the pebble app
 * @param message   the data to send (as JSON)
 * @param succ      the success callback
 * @param error     the error callback
 */
window.Pebble.sendDataToPebble = function(UUID, message, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'sendDataToPebble', [UUID, message]);
};

/**
 * Send a byte array to a specific pebble app
 *
 * @param UUID      the UUID of the pebble app
 * @param key       the key to use in the Pebble Dictionary
 * @param byteArray the data to send (byte array)
 * @param succ      the success callback
 * @param error     the error callback
 */
window.Pebble.sendBytesToPebble = function(UUID, key, byteArray, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'sendBytesToPebble', [UUID, key, byteArray]);
};

/**
 * Send data to a specific Pebble app with a specific transaction ID.
 *
 * @param UUID      the UUID of the pebble app
 * @param message   the data to send (as JSON)
 * @param transId   the transaction ID
 * @param succ      the success callback
 * @param error     the error callback
 */
window.Pebble.sendDataToPebbleWithTransactionId = function(UUID, message, transId, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'sendDataToPebble', [UUID, message, transId]);
};

/**
 * Register a data logging reciever to receieve logged data from Pebble
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.registerDataLoggingReceiver = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'registerDataLoggingReceiver', [UUID]);
};

/**
 * Unregister the data logging receiver, if registered
 *
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.unregisterDataLoggingReceiver = function(succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'unregisterDataLoggingReceiver', []);
};

/**
 * Request the data logs for an application (requires a receiver to be registered)
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.requestDataLogsForApp = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'requestDataLogsForApp', [UUID]);
};

/**
 * Register an ACK handler to receieve message ACKs from Pebble
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.registerReceivedAckHandler = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'registerReceivedAckHandler', [UUID]);
};

/**
 * Register a NACK handler to receieve message NACKs from Pebble
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.registerReceivedNackHandler = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'registerReceivedNackHandler', [UUID]);
};

/**
 * Register a receiver to receive messages sent from the Pebble
 *
 * @param UUID  the UUID of the pebble app
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.registerReceivedDataHandler = function(UUID, succ, error) {
    cordova.exec(succ, error, 'PebbleKitCordova', 'registerReceivedDataHandler', [UUID]);
};