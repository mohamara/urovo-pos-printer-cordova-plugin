var exec = require('cordova/exec')

function PosPrinter() {

}
PosPrinter.prototype.print = function (success, error, action, message) {
    exec(
        function (result) {
            success(result)
        },

        function (err) {
            error(err)
        },
        'POSPrinter', 'print', [action, message])
}

var posPrinter = new PosPrinter()
module.exports = posPrinter
