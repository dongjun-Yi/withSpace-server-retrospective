//package hansung.cse.withSpace.config.websocket;
//
//import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
//import hansung.cse.withSpace.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//public class StompHandler implements ChannelInterceptor {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final JwtTokenUtil jwtTokenUtil;
//    private final MemberService memberService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            UserDetails principal = (UserDetails) auth.getPrincipal();
//            String username = principal.getUsername();
//
//
//            accessor.getSessionAttributes().put("username", username);
//        }
//
//        String username = accessor.getUser().getName();
//        System.out.println("username = " + username);
//
//        return message;
//    }
//}