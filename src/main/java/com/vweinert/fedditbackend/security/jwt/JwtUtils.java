package com.vweinert.fedditbackend.security.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.services.UserDetailsImpl;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final Map<String, Object> keys;
    private final JwtParser parser;
    private final UserRepository userRepository;
    @Value("${feddit.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    public JwtUtils(UserRepository userRepository) throws Exception{
        this.userRepository = userRepository;
        this.keys =  getKeys();
        PublicKey publicKey = (PublicKey) keys.get("public");
        this.parser = Jwts.parser().verifyWith(publicKey).build();
        logger.debug("jwtutils initialized");
    }
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(privateKey)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {

        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public long getUserIdFromJwtToken(String token) {

        return userRepository.findByUsername(
                parser.parseSignedClaims(token)
                        .getPayload()
                        .getSubject())
                .orElseThrow(RuntimeException::new).getId();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            parser.parseSignedClaims(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT claims from an old feddit instance: {}", e.getMessage());
        }

        return false;
    }

    private Map<String,Object> getKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("private", privateKey);
        keys.put("public",publicKey);
        return keys;
    }
}
