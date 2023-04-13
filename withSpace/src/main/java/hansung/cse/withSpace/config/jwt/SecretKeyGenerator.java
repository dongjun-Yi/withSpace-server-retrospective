package hansung.cse.withSpace.config.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator { //설정 등록시 한번만 쓰입니다
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] secretKey = new byte[64]; // 64 바이트 (512비트) 길이의 시크릿 키를 생성
        random.nextBytes(secretKey);

        String encodedKey = Base64.getEncoder().encodeToString(secretKey);
        System.out.println("Secret key: " + encodedKey);
    }
}

