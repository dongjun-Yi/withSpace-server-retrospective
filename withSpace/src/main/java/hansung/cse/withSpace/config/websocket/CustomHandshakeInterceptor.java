//package hansung.cse.withSpace.config.websocket;
//
//import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
//import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class CustomHandshakeInterceptor implements HandshakeInterceptor {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final JwtTokenUtil tokenUtil;
//    final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//        log.info("beforeHandshake가 실행 - WebSocket 연결의 핸드셰이크 단계에서 추가 동작을 수행하는 역할 (아직 뭔가를 해주진 않음)");
//
////        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
////
////        String bearerToken = servletRequest.getHeader("JWT-Authorization");
////        System.out.println(bearerToken);
////        String token = null;
////
////        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
////            token = bearerToken.substring(7);
////        }else {
////            log.error("토큰이 오지 않음..");
////            return false;
////        }
////
////        if (!tokenUtil.validateToken(token)) { //토큰 검증
////            log.error("토큰이 유효하지 않음.");
////            return false;
////        }
//
//        return true;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//    }
//}
