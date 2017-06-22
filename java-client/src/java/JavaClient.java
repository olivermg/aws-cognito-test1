import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.cognitoidentity.*;
import com.amazonaws.services.cognitoidentity.model.*;
import com.amazonaws.services.securitytoken.*;
import com.amazonaws.services.securitytoken.model.*;

public class JavaClient {
    private static String getId(AmazonCognitoIdentity client, String idToken) {
        // this needs to be called only once for a new user

        Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.eu-west-1.amazonaws.com/eu-west-1_jWXOE6x1x", idToken);

        GetIdRequest idRequest = new GetIdRequest();
        //idRequest.setAccountId("");
        idRequest.setIdentityPoolId("eu-west-1:ced310dc-9feb-41e7-b098-1ee17e92a2ce");
        idRequest.setLogins(logins);
        GetIdResult idResponse = client.getId(idRequest);

        return idResponse.getIdentityId();
    }

    private static String getOpenIdToken(AmazonCognitoIdentity client, String identityId, String idToken) {
        Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.eu-west-1.amazonaws.com/eu-west-1_jWXOE6x1x", idToken);

        GetOpenIdTokenRequest tokenRequest = new GetOpenIdTokenRequest();
        tokenRequest.setIdentityId(identityId);
        tokenRequest.setLogins(logins);
        GetOpenIdTokenResult tokenResponse = client.getOpenIdToken(tokenRequest);

        return tokenResponse.getToken();
    }

    private static com.amazonaws.services.securitytoken.model.Credentials assumeRoleWithWebIdentity(String openIdToken) {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.defaultClient();
        AssumeRoleWithWebIdentityRequest stsReq = new AssumeRoleWithWebIdentityRequest();
        stsReq.setRoleArn("arn:aws:iam::962362945363:role/Cognito_idpool1Auth_Role");
        stsReq.setWebIdentityToken(openIdToken);
        stsReq.setRoleSessionName("TestRole1");
        AssumeRoleWithWebIdentityResult stsRes = stsClient.assumeRoleWithWebIdentity(stsReq);

        return stsRes.getCredentials();
    }

    public static void main(String[] args) {
        String idToken = System.getenv("AWS_COGNITO_IDTOKEN"); // you get this from js signin (see index.html)
        String identityId;
        String openIdToken = System.getenv("AWS_COGNITO_OPENIDTOKEN");

        if (openIdToken == null || openIdToken.isEmpty()) {
            AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();
            identityId = getId(client, idToken);
            openIdToken = getOpenIdToken(client, identityId, idToken);
        }

        com.amazonaws.services.securitytoken.model.Credentials credentials1 = assumeRoleWithWebIdentity(openIdToken);

        System.out.println("identityId: " + identityId);
        System.out.println("\nopenIdToken: " + openIdToken);
        System.out.println("\ncredentials1: " + credentials1);

        try {
            System.out.println("\ngetting credentials again after 5s...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("\nINTERRUPTED!");
        }
        com.amazonaws.services.securitytoken.model.Credentials credentials2 = assumeRoleWithWebIdentity(openIdToken);

        System.out.println("\ncredentials2: " + credentials2.toString());
    }
}
