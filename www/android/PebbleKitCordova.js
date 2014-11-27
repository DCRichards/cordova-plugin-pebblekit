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