var AWS = require('aws-sdk');
AWS.config.region = 'eu-west-1';

var Iot = new AWS.Iot();

var thing;
var cert;

/*
function createThing() {
    var params = {
        thingName: 'iotthing2'
        //attributePayload: {},
        //thingTypeName: ''
    };

    console.log('createThing...');
    Iot.createThing(params, function(err, res) {
        if (err) console.log(err, err.stack);
        else {
            console.log('success createThing:', res);
            thing = res;
        }
    });
}
*/

function createCert() {
    var params = {
        setAsActive: true
    };

    console.log('createCert...');
    Iot.createKeysAndCertificate(params, function(err, res) {
        if (err) console.log(err, err.stack);
        else {
            console.log('success createCert:', res);
            cert = res;
        }
    });
}

/*
function attachCertToThing() {
    var params = {
        thingName: thing.thingName,
        principal: cert.certificateArn
    };

    console.log('attachCertToThing...');
    Iot.attachThingPrincipal(params, function(err, res) {
        if (err) console.log(err, err.stack);
        else {
            console.log('success attachCertToThing:', res);
        }
    });
}
*/

function attachCertToPolicy() {
    var params = {
        policyName: 'iotpolicy1',
        principal: cert.certificateArn

    };

    console.log('attachCertToPolicy...');
    Iot.attachPrincipalPolicy(params, function(err, res) {
        if (err) console.log(err, err.stack);
        else {
            console.log('success attachCertToPolicy:', res);
        }
    });
}

//createThing();
createCert();
setTimeout(function() {
    //attachCertToThing();
    attachCertToPolicy();
}, 5000);
