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