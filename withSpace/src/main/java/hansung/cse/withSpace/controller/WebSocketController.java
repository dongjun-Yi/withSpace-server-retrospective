package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.config.auth.MyMemberDetailService;
import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.MessageService;
import hansung.cse.withSpace.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate webSocket;
    private final MessageService messageService;
    private final RoomService roomService;
    private final MemberService memberService;
    final Logger log = LoggerFactory.getLogger(this.getClass());

    @MessageMapping("/{roomId}/message/{memberId}")
    public void SendTemplateMessage(@Payload String message, @DestinationVariable String roomId, @DestinationVariable String memberId) {
        log.trace("SendTemplateMessage 호출");
        log.trace("들어온 메시지(request body)= " + message);

        String topic = "/sub/" + roomId;

        messageService.makeMessage(message, roomId, memberId);

        webSocket.convertAndSend(topic, message);
    }

//    @MessageMapping("/ws/{roomId}/message")
//    @SendTo("/topic/ws/{roomId}") // 메시지를 발행할 토픽 주소
//    public void sendMessage(@DestinationVariable Long roomId,
//                            @Payload Message message, Principal principal) {
//        System.out.println("test22222");
//        Room room = roomService.findOne(roomId);
//        String userEmail = principal.getName();
//        Member member = memberService.findByEmail(userEmail);
//        Message newMessage = messageService.makeMessage(member, room, message.getContent());
//        messagingTemplate.convertAndSend("/topic/chat/" + roomId, newMessage);
//    }




}
