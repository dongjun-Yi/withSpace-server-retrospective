package hansung.cse.withSpace.config.websocket;

import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        System.out.println("message:" + message);
        logger.info("message: " + message);

        System.out.println("헤더 : " + message.getHeaders());
        logger.info("헤더 : " + message.getHeaders());

        System.out.println("토큰" + accessor.getNativeHeader("Authorization"));
        logger.info("토큰" + accessor.getNativeHeader("Authorization"));

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            jwtTokenUtil.validateToken(Objects.requireNonNull
                    (accessor.getFirstNativeHeader("Authorization")).substring(7));
        }
        return message;
    }
}