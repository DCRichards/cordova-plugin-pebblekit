# PebbleKit Cordova

A plugin for Apache Cordova for integration with PebbleKit SDK for the [Pebble Smartwatch](http://developer.getpebble.com/).

I am aware that there are already a number of [similar efforts](https://github.com/konsumer/phonegap-pebble), but none I have encountered so far have successfully implemented [data logging](http://developer.getpebble.com/guides/pebble-apps/communications/pebble-datalogging/), thus the first goal of this is to do just that.

## Installation

Run the following from your Cordova project directory:

	cordova plugin add https://github.com/DCRichards/cordova-plugin-pebblekit.git
		
## API

Functions are available through the __Pebble__ object. For example:

```javaScript
Pebble.getWatchFWVersion(function(version) {
	console.log('The version is: ' + version);
}, function(error) {
	console.log('An error occured: ' + error);
});
```

The following have been implemented. See [PebbleKit Documentation](http://developer.getpebble.com/docs/android) for details:

* getWatchFWVersion()
* registerDataLoggingReceiver()
* unregisterDataLoggingReceiver()
* requestDataLogsForApp()
* areAppMessagesSupported()
* startAppOnPebble()
* sendDataToPebble()
* sendDataToPebbleWithTransactionId()
* startAppOnPebble()

## Notes

### Message Formats

Credit to those in [this thread](http://forums.getpebble.com/discussion/15538/android-pebbledictionary-fromjson) for this super helpful insight. App msessages must be in the following JSON format:

    {
        "key":5,
        "value":"myString",
        "type":"string",
        "length":0
    }
    
Another example with an integer as the value:

    {
        "key":4,
        "value":2,
        "type":"unit",
        "length":1
    }

### Data Logging

At present, data logging returns byte and integer data, the methods for interpreting and using this data will be elaborated on soon.

### Platforms 
Currently, only __android__ is supported.

## Issues & Extensions

If you find a problem or have improvement suggestions then raise and issue or send a pull request