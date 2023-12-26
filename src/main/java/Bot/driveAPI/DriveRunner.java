package Bot.driveAPI;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveRunner {
    private static final String APPLICATION_NAME = "Google Drive";
    private static final String credentials;

    private static Drive getService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredential credential = GoogleCredential.fromStream(new ByteArrayInputStream(credentials.getBytes()))
                .createScoped(Collections.singletonList(DriveScopes.DRIVE));

        Drive service = new Drive.Builder(httpTransport, GsonFactory.getDefaultInstance(),credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }


    public static Drive getDrive() {
        try {
            System.out.println("got drive");
            return getService();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

    }
}
