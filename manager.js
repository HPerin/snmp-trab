var fs = require("fs");
var app = require('express')();
var http = require('http').Server(app);

app.get('/', function (req, res) {
    res.send(JSON.parse(fs.readFileSync('./data.json', 'utf8')));
});

http.listen(3000, function () {
    console.log('listening on *:3000');
});