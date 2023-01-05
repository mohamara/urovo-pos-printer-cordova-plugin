cordova.define("org.cordova.plugin.urovo.posprinter.POSPrinter", function (require, exports, module) {
    var exec = require('cordova/exec')

    function PosPrinter() {

    }
    PosPrinter.prototype.printer = function (success, error, mainAction, action, message) {
        exec(
            function (result) {
                success(result)
            },

            function (err) {
                error(err)
            },
            'POSPrinter', mainAction, [action, message])
    }

    PosPrinter.prototype.printerMini = function (mainAction, action, message) {
        exec(
            function (result) {
                alert(result)
            },

            function (err) {
                alert(err)
            },
            'POSPrinter', mainAction, [action, message])
    }


    var posPrinter = new PosPrinter()
    module.exports = posPrinter

})

