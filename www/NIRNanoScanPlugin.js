var exec = require('cordova/exec');

exports.startSearch = function( success, error) {exec(success, error, "NanoPlugin", "startSearch", [])};
exports.scan = function( success, error) {exec(success, error, "NanoPlugin", "scan", [])};
exports.turnOnBT = function( success, error) {exec(success, error, "NanoPlugin", "turnOnBT", [])};
