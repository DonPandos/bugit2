package com.study.bugit.jwt;

import com.study.bugit.constants.ErrorConstants;
import com.study.bugit.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String username, Collection<? extends GrantedAuthority> grantedAuthorities) {
        Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .claim("ROLES", grantedAuthorities)
                .setSubject(username)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("{} -> Jwt expired");
        } catch (Exception e) {
            log.info("{} -> Invalid jwt token");
        }

        return false;
    }
//        } catch (UnsupportedJwtException e) {
//            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.UNSUPPORTED_JWT);
//        } catch (MalformedJwtException e) {
//            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.MALFORMED_JWT);
//        } catch (SignatureException e) {
//            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.INVALID_SIGNATURE);
//        } catch (Exception e) {
//            throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.INVALID_JWT);
//        }

    public String getLoginFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

}
