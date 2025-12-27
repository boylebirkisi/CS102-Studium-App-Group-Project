package cs102groupproject.Server;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.auth.oauth2.Credential;

/**
 * GoogleCredentialManager facilitates the Google processes by staticly creating an http tansport for the google.
 * Author: Delfin Eryılmaz
 * Date: 27/12/2025
 */

public class GoogleCredentialManager {

    public static final NetHttpTransport HTTP_TRANSPORT;
    public static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Prevents the creation of multiple HTTP transports.
    // Providing HTTO_TRANSPORT needed for the Google process.
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the credentials through access token. (not tested)
     * @param accessToken
     * @return
     */
    public static Credential getCredentialsFromAccessToken(String accessToken) {
        return new GoogleCredential().setAccessToken(accessToken);
    }
}
