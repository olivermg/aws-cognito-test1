var awsiot = require('aws-iot-device-sdk');

var device = awsiot.device({
    clientId: 'iotclient1',
    region: 'eu-west-1',
    protocol: 'wss'

    // via env vars:
    //accessKeyId: process.env['AWS_ACCESS_KEY_ID'],
    //secretKey: process.env['AWS_SECRET_ACCESS_KEY'],
    //sessionToken: process.env['AWS_SESSION_TOKEN']
});

device.on('status', function(thing, stat, clientToken, state) {
    console.log('status');
    console.log(thing);
    console.log(stat);
    console.log(clientToken);
    console.log(state);
});

device.on('connect', function() {
    console.log('connect');
    device.subscribe('iottopic1', function(res) {
        console.log('subscribe res: ', res);
    });
});

device.on('message', function(topic, payload) {
    console.log('message', topic, payload.toString());
    //device.publish('mytopic2', JSON.stringify({ foo: 'bar123' }));
});

device.on('error', function(err, args) {
    console.log('error', err, args);
});
