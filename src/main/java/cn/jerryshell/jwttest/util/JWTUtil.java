package cn.jerryshell.jwttest.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {
    private static Algorithm ALGORITHM;

    public static String sign(String username, Date expiresAt, String role) {
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(expiresAt)
                .withClaim("role", role)
                .sign(ALGORITHM);
    }

    public static boolean verify(String token, String username, String role) {
        try {
            Verification verification = JWT.require(ALGORITHM);
            verification.withClaim("username", username);
            verification.withClaim("role", role);
            JWTVerifier verifier = verification.build();
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

    public static String getRole(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("role").asString();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    @Autowired
    public void init(Environment environment) {
        JWTUtil.ALGORITHM = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("jwt.secret")));
    }
}
