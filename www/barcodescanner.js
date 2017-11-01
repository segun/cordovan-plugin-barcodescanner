/*global cordova, module*/

module.exports = {
    scan: function(name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "BarcodeScanner", "scan", [name]);
    }
};