cordova.define("org.cordova.plugin.urovo.posprinter.POSPrinter", function (require, exports, module) {
    var exec = require('cordova/exec')

    function PosPrinter() {

    }
    PosPrinter.prototype.printer = function (success, error, action, message) {
        exec(
            function (result) {
                success(result)
            },

            function (err) {
                error(err)
            },
            'POSPrinter', 'execute', [action, message])
    }

    PosPrinter.prototype.printerMini = function (action, message) {
        exec('POSPrinter', 'execute', [action, message])
    }


    var posPrinter = new PosPrinter()
    module.exports = posPrinter

})
