package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.chat.CreateMessageResponse;
import hansung.cse.withSpace.service.MessageService;
import hansung.cse.withSpace.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final RoomService roomService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @MessageMapping("/chat/{roomId}/message")
    @PreAuthorize("@jwtAuthenticationFilter.isRoomOwner(#request, #roomId)")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload Message message, HttpServletRequest request) {
        Room room = roomService.findOne(roomId);
        Member member = jwtAuthenticationFilter.findMemberByUUID(request);
        Message newMessage = messageService.makeMessage(member, room, message.getContent());
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, newMessage);
    }
}
