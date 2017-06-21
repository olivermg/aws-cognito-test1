import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.cognitoidentity.*;
import com.amazonaws.services.cognitoidentity.model.*;

public class JavaClient {
    private static String getId(String idToken) {
        // this needs to be called only once for a new user

        AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();

        Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.eu-west-1.amazonaws.com/eu-west-1_jWXOE6x1x", idToken);

        GetIdRequest idRequest = new GetIdRequest();
        //idRequest.setAccountId("");
        idRequest.setIdentityPoolId("eu-west-1:ced310dc-9feb-41e7-b098-1ee17e92a2ce");
        idRequest.setLogins(logins);
        GetIdResult idResponse = client.getId(idRequest);

        return idResponse.getIdentityId();
    }

    public static void main(String[] args) {
        String idToken = System.getenv("AWS_COGNITO_IDTOKEN"); // you get this from js signin (see index.html)

        String identityId = getId(idToken);

        System.out.println(identityId);
    }
}
