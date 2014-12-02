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
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.registerDataLoggingReceiver = function(succ, error) {
    console.log('To be implemented...');
    //cordova.exec(succ, error, 'PebbleKitCordova', 'registerDataLoggingReceiver');
};

/**
 * Unregister the data logging receiver, if registered
 *
 * @param succ  the success callback
 * @param error the error callback
 */
window.Pebble.unregisterDataLoggingReceiver = function(succ, error) {
    console.log('To be implemented...');
    //cordova.exec(succ, error, 'PebbleKitCordova', 'unregisterDataLoggingReceiver');
};