var awsiot = require('aws-iot-device-sdk');

var certId = process.env['CERTID'];
if (!certId) {
    console.log('please set env var CERTID.');
    process.exit(1);
}

var device = awsiot.device({
    clientId: certId,
    region: 'eu-west-1',
    //protocol: 'wss', // no option when authenticating via cert
    keyPath: 'cert/private.pem.key',
    certPath: 'cert/cert.crt',
    caPath: 'cert/rootca.pem'
});

device.on('connect', function() {
    console.log('connect');
    device.subscribe(certId + '/newfacts', function(res) {
        console.log('subscribe res: ', res);
    });
});

device.on('message', function(topic, payload) {
    console.log('message', topic, payload.toString());
    //device.publish('iottopic2', JSON.stringify({ foo: 'bar123' }));
});

device.on('error', function(err, args) {
    console.log('error', err, args);
});
