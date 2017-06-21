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

        AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();
        String identityId = getId(client, idToken);
        String openIdToken = getOpenIdToken(client, identityId, idToken);
        com.amazonaws.services.securitytoken.model.Credentials credentials = assumeRoleWithWebIdentity(openIdToken);

        System.out.println("identityId: " + identityId);
        System.out.println("openIdToken: " + openIdToken);
        System.out.println("credentials: " + credentials.toString());
    }
}
