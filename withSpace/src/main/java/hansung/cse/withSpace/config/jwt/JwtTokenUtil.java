package hansung.cse.withSpace.config.jwt;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedSecret);
    }

    @Autowired
    MemberService memberService;

    public String generateToken(Authentication authentication, boolean rememberMe) {

        // 토큰 만료 시간 계산
        long expireTime = rememberMe ? expiration * 6 * 24 * 7 : expiration;

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberService.findByEmail(userDetails.getUsername()); // 로그인한 유저의 회원 정보를 가져옴
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", member.getId()) // 회원 id를 토큰에 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 여기에 예외 로깅을 추가할 수 있습니다.
            return false;
        }
    }
}

