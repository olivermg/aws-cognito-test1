var AWS = require('aws-sdk');

AWS.config.region = 'eu-west-1';
AWS.config.credentials = new AWS.CognitoIdentityCredentials({
	IdentityPoolId: 'eu-west-1:782d03f1-7e76-4526-b157-2a6ae2ec000e'
});

var cid = new AWS.CognitoIdentity();
cid.getId({
	IdentityPoolId: 'eu-west-1:782d03f1-7e76-4526-b157-2a6ae2ec000e'
}, function(err, data) {
	if (err) console.log(err, err.stack);
	else     console.log(data);
});

console.log(AWS.config.credentials);

