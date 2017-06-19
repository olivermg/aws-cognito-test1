var awsiot = require('aws-iot-device-sdk');

var device = awsiot.device({
	clientId: 'client123',
	region: 'eu-west-1',
	protocol: 'wss',
	accessKeyId: '',
	secretKey: ''
});

device.on('connect', function() {
	console.log('connect');
	device.subscribe('mytopic1');
});

device.on('message', function(topic, payload) {
	console.log('message', topic, payload.toString());
	device.publish('mytopic2', JSON.stringify({ foo: 'bar123' }));
});

