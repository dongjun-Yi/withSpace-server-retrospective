package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.config.auth.MyMemberDetailService;
import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.config.websocket.StompHandler;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.chat.CreateMessageResponse;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.MessageService;
import hansung.cse.withSpace.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final RoomService roomService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final MyMemberDetailService myMemberDetailService;

    private final MemberService memberService;

    @MessageMapping("/chat/{roomId}/message")
    @SendTo("/topic/chat/{roomId}") // 메시지를 발행할 토픽 주소
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload Message message, Principal principal) {
        System.out.println("test22222");
        Room room = roomService.findOne(roomId);
        String userEmail = principal.getName();
        Member member = memberService.findByEmail(userEmail);
        Message newMessage = messageService.makeMessage(member, room, message.getContent());
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, newMessage);
    }




}
