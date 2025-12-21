package cs102groupproject.Server;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Provides HTTP transport and JSON factory instances for Google API.
 */
public class GoogleCredentialManager {

    private static final String CLIENT_ID = "698771530420-m8t1rqdt78eb64c0stt3hc7s1f5v9dlc.apps.googleusercontent.com";

    public static final NetHttpTransport HTTP_TRANSPORT;
    public static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Prevents the creation of multiple HTTP transports.
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
