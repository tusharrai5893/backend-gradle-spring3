package com.reddit.backend.security;

import com.reddit.backend.exceptions.RedditCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static java.util.Date.from;

@Service
public class JwtProviderService {

    //Secret pass for jks file
    private static final String SECRET = "secret";
    public static final String SPRINGBLOG = "springblog";
    private KeyStore keyStore;

    @Getter
    @Value("${jwt.expire.time}")
    private long jwtExpirationTimeInMillis;

    @PostConstruct
    public void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream keyStoreStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(keyStoreStream, SECRET.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RedditCustomException("Error in JwtProviderService class while loading " + e.getMessage());
        }
    }

    public String generateJWToken(Authentication authentication) {
        UserDetailsImpl loggingUser = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject(loggingUser.getUsername())
                .issuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
                .compact();
    }


    public String generateRefreshJWToken(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(SPRINGBLOG, SECRET.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RedditCustomException("Exception occurred while retrieving getPrivateKey from keystore " + e);
        }
    }

    public boolean validateJwtToken(String JwtToken) {
        try {
            Jws<Claims> isValidated = Jwts.parser().setSigningKey(getPublicKey()).build().parseSignedClaims(JwtToken);
        } catch (JwtException e) {
            throw new RedditCustomException(e.getLocalizedMessage());
        }
        return true;

    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(SPRINGBLOG).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditCustomException("Error while getting Certificate in Public key method JWT provider class " + e);
        }
    }

    public String getUsernameFromJwt(String jwtToken) {
        return Jwts.parser().setSigningKey(getPublicKey())
                .build().parseSignedClaims(jwtToken)
                .getBody().getSubject();
    }

}
