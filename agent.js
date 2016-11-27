/**
 * Created by lucas on 11/27/16.
 */
var os = require('os');
var snmp = require('snmpjs');
var logger = require('bunyan');
var fs = require('fs');

var log = new logger({
    name: 'snmpd',
    level: 'info'
});

var agent = snmp.createAgent({
    log: log
});

// -----------------------------------------------------------------------
// SPEED
// -----------------------------------------------------------------------

// curSpeed
agent.request({ oid: '.1.2.3.1', handler: function (prq) {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    console.log(prq);;
    var val = snmp.data.createData({ type: 'Integer', value: data.curSpeed });
    snmp.provider.writableScalar(prq, val);
} });

// maxSpeed
agent.request({ oid: '.1.2.3.2', handler: function (prq) {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    console.log(prq);
    if (prq['_value'] != null) {
        data.maxSpeed = prq['_value']['_value'];
        fs.writeFileSync('./data.json', JSON.stringify(data, null, 4));
    }
    var val = snmp.data.createData({ type: 'Integer', value: data.maxSpeed });
    snmp.provider.writableScalar(prq, val);
} });

// minSpeed
agent.request({ oid: '.1.2.3.3', handler: function (prq) {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    console.log(prq);
    if (prq['_value'] != null) {
        data.minSpeed = prq['_value']['_value'];
        fs.writeFileSync('./data.json', JSON.stringify(data, null, 4));
    }
    var val = snmp.data.createData({ type: 'Integer', value: data.minSpeed });
    snmp.provider.writableScalar(prq, val);
} });

// -----------------------------------------------------------------------
// -----------------------------------------------------------------------

// -----------------------------------------------------------------------
// FUEL
// -----------------------------------------------------------------------

// curFuel
agent.request({ oid: '.1.2.4.1', handler: function (prq) {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    console.log(prq);
    var val = snmp.data.createData({ type: 'Integer', value: data.curSpeed });
    snmp.provider.writableScalar(prq, val);
} });

// maxFuel
agent.request({ oid: '.1.2.4.2', handler: function (prq) {
    var data = JSON.parse(fs.readFileSync('./data.json', 'utf8'));
    console.log(prq);
    var val = snmp.data.createData({ type: 'Integer', value: data.maxFuel });
    snmp.provider.writableScalar(prq, val);
} });

// -----------------------------------------------------------------------
// -----------------------------------------------------------------------

agent.request({ oid: '.1.2.3.99', handler: function (prq) {
    var nodeName = os.hostname();
    var val = snmp.data.createData({ type: 'OctetString', value: nodeName });
    snmp.provider.readOnlyScalar(prq, val);
} });

agent.bind({ family: 'udp4', port: 1025 });