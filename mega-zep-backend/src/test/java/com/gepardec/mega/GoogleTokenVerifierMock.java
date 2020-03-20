package com.gepardec.mega;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.security.GeneralSecurityException;

@ApplicationScoped
@Mock
public class GoogleTokenVerifierMock extends GoogleIdTokenVerifier {

    private GoogleIdTokenVerifier delegate;

    public GoogleTokenVerifierMock() {
        super(new Builder(new NetHttpTransport(), new JacksonFactory()));
    }

    @Override
    public boolean verify(GoogleIdToken googleIdToken) throws GeneralSecurityException, IOException {return delegate.verify(googleIdToken);}

    @Override
    public GoogleIdToken verify(String idTokenString) throws GeneralSecurityException, IOException {return delegate.verify(idTokenString);}

    @Override
    @Deprecated
    public GoogleIdTokenVerifier loadPublicCerts() throws GeneralSecurityException, IOException {return delegate.loadPublicCerts();}

    @Override
    public boolean verify(IdToken idToken) {return delegate.verify(idToken);}

    public void setDelegate(GoogleIdTokenVerifier delegate) {
        this.delegate = delegate;
    }
}
