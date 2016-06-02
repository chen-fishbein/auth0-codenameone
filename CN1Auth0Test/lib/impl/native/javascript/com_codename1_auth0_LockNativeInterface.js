(function(exports){

var o = {};

    o.showLockScreen_ = function(callback) {
        callback.error(new Error("Not implemented yet"));
    };

    o.isSupported_ = function(callback) {
        callback.complete(false);
    };

exports.com_codename1_auth0_LockNativeInterface= o;

})(cn1_get_native_interfaces());
