package hansung.cse.withSpace.config.jwt;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtTokenUtil {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;
    @Autowired
    MemberService memberService;

    private SecretKeySpec createSecretKey(String secret, String algorithm) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(decodedKey, algorithm);
    }

    public Key getSigningKey() { //대칭키 암호화
        SecretKeySpec secretKeySpec = createSecretKey(secret, "HmacSHA256");
        return Keys.hmacShaKeyFor(secretKeySpec.getEncoded());
    }

    public String generateToken(Authentication authentication, boolean rememberMe) {

        // 토큰 만료 시간 계산
        long expireTime = rememberMe ? expiration * 6 * 24 * 7 : expiration;

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberService.findByEmail(userDetails.getUsername()); // 로그인한 유저의 회원 정보를 가져옴
        //Date now = new Date();
        //Date expiryDate = new Date(now.getTime() + expiration);

        // 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", member.getId()) // 회원 id를 토큰에 추가
                .claim("UUID", member.getUuid())// uuid를 토큰에 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) //서명
                .compact();
    }




    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token)  {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // JWT가 만료된 경우 처리
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 형식인 경우 처리
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // 잘못된 형식의 JWT인 경우 처리
            log.error("Malformed JWT token: {}", e.getMessage());
        }  catch (IllegalArgumentException e) {
            // 잘못된 인자가 전달된 경우 처리
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

