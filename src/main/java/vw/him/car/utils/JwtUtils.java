package vw.him.car.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtils {

    public static String decodeJWT(String jwt) {

        String[] jwtParts = jwt.split("\\.");

        if (jwtParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }


        byte[] bytes = Base64.getUrlDecoder().decode(jwtParts[1]);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}

