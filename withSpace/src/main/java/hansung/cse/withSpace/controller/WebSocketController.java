package hansung.cse.withSpace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hansung.cse.withSpace.config.auth.MyMemberDetailService;
import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.dto.MemberIdDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.MessageService;
import hansung.cse.withSpace.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate webSocket;
    private final MessageService messageService;
    private final RoomService roomService;
    private final MemberService memberService;
    final Logger log = LoggerFactory.getLogger(this.getClass());

    //private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @MessageMapping("/{roomId}/message/{memberId}") // 채팅 메시지
    public void SendTemplateMessage(@Payload String message, @DestinationVariable String roomId, @DestinationVariable String memberId) {
        //log.trace("SendTemplateMessage 호출");
        //log.trace("들어온 메시지(request body)= " + message);

        String topic = "/sub/" + roomId;

        messageService.makeMessage(message, roomId, memberId);

        webSocket.convertAndSend(topic, message); //구독자에게 메시지 뿌리기
    }

    @MessageMapping("/heartbeat") // heartbeat 응답
    public void receiveHeartBeat(@Payload String message, SimpMessageHeaderAccessor accessor) throws JsonProcessingException {
        //log.info("heartbeat 응답 호출");
        //log.info("들어온 메시지(request body)= " + message);
//        String sessionId = accessor.getSessionId();
//
        JsonNode jsonNode = objectMapper.readTree(message);
        String memberId = jsonNode.get("memberId").asText();
        //log.info("추출된 memberId = " + memberId);

        String memberIdKey = "memberId:"+ memberId;
        //stringRedisTemplate.opsForHash().put(memberIdKey, "time", LocalDateTime.now().toString());
        stringRedisTemplate.opsForValue().set(memberIdKey, memberId, 5, TimeUnit.SECONDS);

//        String memberId = payload.
//        stringRedisTemplate.opsForHash().put(memberId, "time", LocalDateTime.now());
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3000)
    public void sendHeartBeat() { //3초마다 각 웹소켓 세션에 대해 heartbeat 메시지 전송



        Set<String> keys = stringRedisTemplate.keys("memberId:*");

        for (String key : keys) {


            // 키에 대한 원하는 작업 수행
            String memberId = stringRedisTemplate.opsForValue().get(key);

            String topic = "/sub/heartbeat/" + memberId;

            Map<String, Object> payload = new HashMap<>();
            payload.put("memberId", memberId);
            payload.put("message", "heartbeat");

            webSocket.convertAndSend(topic, payload);
        }
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3000)
    public void checkHeartBeat() { //3초마다 각 회원의 접속여부 검사


        List<Long> membersId = memberService.findMembersId();

        Set<Long> memberIdKeys = stringRedisTemplate.keys("memberId:*")
                .stream()
                .map(key -> Long.valueOf(key.split(":")[1]))
                .collect(Collectors.toSet());


        Set<Long> membersIdSet = new HashSet<>(membersId);


        Set<Long> onlyInMembersId = new HashSet<>(membersIdSet);
        onlyInMembersId.removeAll(memberIdKeys);

        Set<Long> onlyInRedis = new HashSet<>(memberIdKeys);
        onlyInRedis.removeAll(membersIdSet);

        onlyInMembersId.forEach(memberService::setMemberInActive);
        onlyInRedis.forEach(memberService::setMemberInActive);


    }

}
