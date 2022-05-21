var exec = require('cordova/exec')

export class PosPrinter {
    print(arg0, success, error) {
        alert('hi')
        // exec(success, error, 'POSPrinter', 'coolMethod', [arg0])
    };
}

var posPrinter = new PosPrinter()
module.exports = posPrinter
