package com.wang.fromzerotoexpert.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wang.fromzerotoexpert.handler.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    private static final String ISSURE = "签发者";

    public static String generateToken(Long userId) throws Exception {
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // TODO 30
        calendar.add(Calendar.HOUR, 12);
        return JWT.create().withKeyId(String.valueOf(userId)).withIssuer(ISSURE).withExpiresAt(calendar.getTime()).sign(algorithm);
    }

    public static Long verifyToken(String token) throws ConditionException {
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555", "token已过期!");
        } catch (Exception e) {
            throw new ConditionException("非法用户token!");
        }
    }

}
