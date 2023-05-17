package hansung.cse.withSpace.config.websocket;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String token = jwtAuthenticationFilter.extractToken(servletRequest);

        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtAuthenticationFilter.getUsernameFromToken(token);
            // username을 attributes에 저장하고 WebSocketSession에서 사용할 수 있음
            attributes.put("username", username);
            return true;
        }
        log.error("검증관련 문제");
        return false;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
