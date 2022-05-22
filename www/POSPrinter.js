var exec = require('cordova/exec')

function PosPrinter() {

}
PosPrinter.prototype.print = function (type, data, success, error) {
    exec(success, error, 'POSPrinter', 'print', [
        { "type": type, "data": data }
    ])
}

var posPrinter = new PosPrinter()
module.exports = posPrinter
