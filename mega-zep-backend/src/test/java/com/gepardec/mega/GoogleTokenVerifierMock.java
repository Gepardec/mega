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
import java.util.function.Function;

@ApplicationScoped
@Mock
public class GoogleTokenVerifierMock extends GoogleIdTokenVerifier {

    private Function<String, GoogleIdToken> answer;

    public GoogleTokenVerifierMock() {
        super(new Builder(new NetHttpTransport(), new JacksonFactory()));
    }


    @Override
    public GoogleIdToken verify(String idToken) throws GeneralSecurityException, IOException {
        return answer.apply(idToken);
    }

    public void setAnswer(Function<String, GoogleIdToken> answer) {
        this.answer = answer;
    }
}
