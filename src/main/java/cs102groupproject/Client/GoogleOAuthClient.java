package cs102groupproject.Client;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GoogleOAuthClient {
    // Instance variables for HTTP transport and JSON factory
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    // Scopes needed for the app
    private static final List<String> SCOPES = List.of(
        "https://www.googleapis.com/auth/userinfo.profile",
        "https://www.googleapis.com/auth/userinfo.email",
        "https://www.googleapis.com/auth/calendar"
    );

    // Load client secrets from client_secrets.json file -> for OAuth2 authentication
    private GoogleClientSecrets loadClientSecrets() throws Exception {

        InputStream in = GoogleOAuthClient.class
                .getClassLoader()
                .getResourceAsStream("client_secrets.json");

        if (in == null) {
            throw new RuntimeException("client_secrets.json not found in resources");
        }

        return GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(in)
        );
    }

    // Authenticates user and obtain OAuth2 Credential
    public Credential authenticate() throws Exception {

        GoogleClientSecrets clientSecrets = loadClientSecrets();

        // Builds GoogleAuthorizationCodeFlow which manages OAuth2 authorization.
        GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
            )
            .setAccessType("offline") // Gives refresh token
            .setDataStoreFactory(new MemoryDataStoreFactory())
            .build();

        // Local server (our app) receives OAuth2 callback
        LocalServerReceiver receiver =
            new LocalServerReceiver.Builder()
                .setHost("localhost")
                .setPort(8888)
                .setCallbackPath("/callback")
                .build();

        // Catches OAuth2 authorization and returns Credential
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // Prevents null or expired access token by refreshing it if it is necessary.
        if (credential.getRefreshToken() != null && credential.getAccessToken() == null) {
            credential.refreshToken();
        }
        return credential;
    }
}

