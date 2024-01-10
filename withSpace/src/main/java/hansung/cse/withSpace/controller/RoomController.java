package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.chat.*;
import hansung.cse.withSpace.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/chat/room/{roomId}") //채팅방 조회
    public ResponseEntity<BasicResponse> getRoom(@PathVariable("roomId") Long roomId, HttpServletRequest request) {
        Room room = roomService.findOne(roomId);
        GetRoomResponseDto roomResponseDto = new GetRoomResponseDto(room);
        BasicResponse basicResponse = new BasicResponse<>(1, "채팅방 조회 성공", roomResponseDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}/chatrooms") //회원이 들어가있는 채팅방 조회
    public ResponseEntity<BasicResponse> getMemberChatting(@PathVariable("memberId") Long memberId,
                                                           HttpServletRequest request) {
        List<MemberRoomResponseDto> membersRoom = roomService.findMembersRoom(memberId);
        BasicResponse basicResponse = new BasicResponse<>(1, "채팅방 조회 성공", membersRoom);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
