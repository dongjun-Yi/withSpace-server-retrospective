package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.requestdto.chat.CreateRoomRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.chat.CreateRoomResponse;
import hansung.cse.withSpace.responsedto.chat.GetRoomResponseDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.RoomService;
import hansung.cse.withSpace.service.SpaceService;
import hansung.cse.withSpace.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class RoomController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final RoomService roomService;
    private final MemberService memberService;
    private final TeamService teamService;
    private final SpaceService spaceService;
    @GetMapping("/room/{roomId}") //채팅방 조회
    public ResponseEntity<BasicResponse> getRoom(@PathVariable("roomId") Long roomId) {
        Room room = roomService.findOne(roomId);
        GetRoomResponseDto roomResponseDto = new GetRoomResponseDto(room);
        BasicResponse basicResponse = new BasicResponse<>(1, "채팅방 조회 성공", roomResponseDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/room") //채팅방 생성
    public ResponseEntity<CreateRoomResponse> createChattingRoom(@RequestBody CreateRoomRequestDto roomRequestDto) {
        Space space = null;
        if (roomRequestDto.getMemberId().isPresent() && roomRequestDto.getFriendId().isPresent()) {
            //보완 필요
            Member member = memberService.findOne(roomRequestDto.getMemberId().get());
            space = member.getMemberSpace();
        } else if (roomRequestDto.getMemberId().isPresent()) {
            Team team = teamService.findOne(roomRequestDto.getTeamId().get());
            space = team.getTeamSpace();
        } else {
            //팀 또는 회원 아이디 정보가 안 실려온 경우
        }
        Long roomId = roomService.makeRoom(space, roomRequestDto.getRoomName());
        CreateRoomResponse createRoomResponse = new CreateRoomResponse(roomId, CREATED, "채팅방 생성 완료");
        return new ResponseEntity<>(createRoomResponse, HttpStatus.CREATED);
    }

}
