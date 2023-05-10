//package hansung.cse.withSpace.config.jwt;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.apache.http.HttpHeaders;
//
//public class JwtTokenExtractor {  //request 요청시 토큰을 추출하는 용도
//    public static final String HEADER_PREFIX = "Bearer ";
//
//    public static String extract(HttpServletRequest request) {
//        String header = request.getHeader("JWT-Authorization");
//
//        if (header == null || header.isEmpty() || !header.startsWith(HEADER_PREFIX)) {
//            return null;
//        }
//
//        return header.substring(HEADER_PREFIX.length());
//    }
//}
