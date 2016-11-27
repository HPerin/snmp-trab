var fs = require('fs');

var goingUp = true;

function updateCurSpeed() {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    if (goingUp) {
        if (data.curSpeed == data.maxSpeed) {
            goingUp = false;
            data.curSpeed--;
        } else {
            data.curSpeed++;
        }
    } else {
        if (data.curSpeed == data.minSpeed) {
            goingUp = true;
            data.curSpeed++;
        } else {
            data.curSpeed--;
        }
    }

    data.curFuel--;
    if (data.curFuel == 0) {
        data.curFuel = data.maxFuel;
    }
    fs.writeFileSync('./data.json', JSON.stringify(data, null, 4));

    setTimeout(updateCurSpeed, 1000);
}

setTimeout(updateCurSpeed, 0);