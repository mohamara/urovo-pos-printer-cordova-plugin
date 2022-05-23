var exec = require('cordova/exec')

function PosPrinter() {

}
PosPrinter.prototype.print = function (action, message, success, error) {
    exec(success, error, 'POSPrinter', 'print', [
        { "action": action, "message": message }
    ])
}

var posPrinter = new PosPrinter()
module.exports = posPrinter
