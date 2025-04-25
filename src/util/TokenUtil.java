package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import exception.TokenInvalidoException;

import java.util.Date;

public class TokenUtil {
    private static final String SECRET_KEY_JWT = "j2k4h5k2j4h5k2j4h5k2j4h5k2j4h5k2";
    private static final String ISSUER = "api-bibliotech-java";

    // método para gerar token JWT
    public static String gerarToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_JWT);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm); // expira em 1 hora
    }

    // método para validar o token JWT
    public static void validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY_JWT);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();

            verifier.verify(token);
        } catch (Exception e) {
            throw new TokenInvalidoException("Token de autenticação inválido ou expirado.");
        }
    }

}
