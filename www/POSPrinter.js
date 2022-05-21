var exec = require('cordova/exec')

function PosPrinter() {

}
PosPrinter.prototype.print = function (arg0, success, error) {
    alert('hi')
    // exec(success, error, 'POSPrinter', 'coolMethod', [arg0])
}

var posPrinter = new PosPrinter()
module.exports = posPrinter
