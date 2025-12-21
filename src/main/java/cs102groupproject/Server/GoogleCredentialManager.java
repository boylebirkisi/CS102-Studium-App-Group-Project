package cs102groupproject.Server;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;
import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

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

    public static Credential getCredentialsFromAccessToken(String accessToken) {
        return new GoogleCredential().setAccessToken(accessToken);
    }
}
