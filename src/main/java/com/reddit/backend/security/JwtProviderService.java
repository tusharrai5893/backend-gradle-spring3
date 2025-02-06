package com.reddit.backend.security;

import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtProviderService {

    //Secret pass for jks file
    private static final String SECRET = "secret";
    public static final String SPRINGBLOG = "springblog";
    private KeyStore keyStore;

    @Value("${jwt.expire.time}")
    public long JWT_EXPIRATION_TIME_IN_MILLIS;

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

    private <T> T extractClaims(String jwt, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims(jwt));
    }

    public String generateJWToken(Authentication authentication) {
        User loggingUser = (User) authentication.getPrincipal();
        return Jwts.builder()
                .subject(loggingUser.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .expiration(Date.from(Instant.now().plusMillis(JWT_EXPIRATION_TIME_IN_MILLIS)))
                .compact();
    }


    public String generateRefreshJWToken(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .expiration(Date.from(Instant.now().plusMillis(JWT_EXPIRATION_TIME_IN_MILLIS)))
                .compact();
    }

 /*   private SecretKey getPrivateKey() {
        try {
            Key key = keyStore.getKey(SPRINGBLOG, SECRET.toCharArray());
            return (SecretKey) key;
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RedditCustomException("Exception occurred while retrieving getPrivateKey from keystore " + e);
        }
    }*/

    private PrivateKey getPrivateKey() {
        try {
            Key key = keyStore.getKey(SPRINGBLOG, SECRET.toCharArray());
            if (key instanceof PrivateKey) {
                return (PrivateKey) key;
            } else {
                throw new RedditCustomException("Key retrieved from keystore is not a PrivateKey.");
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RedditCustomException("Exception occurred while retrieving PrivateKey from keystore: " + e.getMessage());
        }
    }

    public boolean isTokenExpired(String JwtToken) throws ExpiredJwtException {
        Date expiration = extractClaims(JwtToken, Claims::getExpiration);
        System.out.println("Token Expiration: " + expiration);
        System.out.println("Current Time: " + Date.from(Instant.now()));
        return expiration.after(Date.from(Instant.now()));
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(SPRINGBLOG).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditCustomException("Error while getting Certificate in Public key method JWT provider class " + e);
        }
    }

    public Claims claims(String jwtToken) {
        return Jwts.parser().verifyWith(getPublicKey())
                .build().parseSignedClaims(jwtToken)
                .getPayload();
    }

}
