package cn.jerryshell.jwttest.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {
    private static Algorithm ALGORITHM;

    public static String sign(String username) {
        return JWT.create()
                .withClaim("username", username)
                .sign(ALGORITHM);
    }

    public static String signWithExpires(String username, Date expiresDate) {
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(expiresDate)
                .sign(ALGORITHM);
    }

    public static boolean verify(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    @Autowired
    public void init(Environment environment) {
        JWTUtil.ALGORITHM = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("jwt.secret")));
    }
}
