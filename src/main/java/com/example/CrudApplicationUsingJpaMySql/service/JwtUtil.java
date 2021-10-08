package com.example.CrudApplicationUsingJpaMySql.service;

import com.example.CrudApplicationUsingJpaMySql.entity.User;
import com.example.CrudApplicationUsingJpaMySql.exception.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil  {
    private final SecretKey SECRET_KEY;
    String token;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    final Key signingKey;
    public JwtUtil() {
        SECRET_KEY = MacProvider.generateKey(SignatureAlgorithm.HS256);
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(String.valueOf(SECRET_KEY));
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }
    public String generateToken(String email, User user) {
        int jwtTimeToLive = 800000;
        Date exp=null;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (jwtTimeToLive >= 0) {
            long expMillis = nowMillis + jwtTimeToLive;
            exp = new Date(expMillis);
        }

       token=Jwts.builder()
               .setHeaderParam("typ","JWT")
               .setId(String.valueOf(user.getId()))
               .setSubject(email)
               .setIssuedAt(now)
               .setExpiration(exp) //10 saat
               .signWith(signatureAlgorithm, SECRET_KEY).compact();
        return token;
    }
    public String getSubject(String token) throws NotAuthorizedException {
        return parseClaims(token).getSubject();
    }
    public String getId(String token) throws NotAuthorizedException {
        return parseClaims(token).getId();
    }
    private Date getExpiration(String token) throws NotAuthorizedException {
        return parseClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) throws NotAuthorizedException {
        return getExpiration(token).before(new Date());
    }

    private Claims parseClaims(String token) throws NotAuthorizedException {
        Claims jwt=null;
        try{
            jwt= Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token).getBody();
        }
        catch (Exception exception){
            NotAuthorizedException response =
                    new NotAuthorizedException("Unauthorized");
            throw response;
        }
        return jwt;
    }

    public boolean validateToken(String token, User user) throws NotAuthorizedException {
        final String email = getSubject(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }
}
