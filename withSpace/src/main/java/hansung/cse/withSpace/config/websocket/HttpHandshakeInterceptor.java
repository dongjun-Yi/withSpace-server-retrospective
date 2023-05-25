//package hansung.cse.withSpace.config.websocket;
//
//import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
//import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class HttpHandshakeInterceptor implements HandshakeInterceptor {
//
//    private final JwtTokenUtil jwtTokenUtil;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//        log.error(request.getHeaders().toString());
//
//
//        if (request instanceof ServletServerHttpRequest) {
//
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//
//            String token = jwtAuthenticationFilter.extractToken(httpRequest);
//
//            if (token != null && jwtTokenUtil.validateToken(token)) {
//                String username = jwtTokenUtil.getUsernameFromToken(token);
//                attributes.put("username", username);
//                return true;
//            }
//        }
//
//        log.error("beforeHandshake 에러");
//        return false;
//
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//    }
//}
