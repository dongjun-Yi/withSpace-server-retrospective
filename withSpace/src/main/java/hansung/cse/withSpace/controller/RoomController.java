package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.RequiredValueMissingException;
import hansung.cse.withSpace.requestdto.chat.CreateMessageRequestDto;
import hansung.cse.withSpace.requestdto.chat.CreateRoomRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.chat.CreateMessageResponse;
import hansung.cse.withSpace.responsedto.chat.CreateRoomResponse;
import hansung.cse.withSpace.responsedto.chat.GetRoomResponseDto;
import hansung.cse.withSpace.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MessageService messageService;


    @GetMapping("/room/{roomId}") //채팅방 조회
    @PreAuthorize("@customSecurityUtil.isRoomOwner(#roomId)")
    public ResponseEntity<BasicResponse> getRoom(@PathVariable("roomId") Long roomId) {
        Room room = roomService.findOne(roomId);
        GetRoomResponseDto roomResponseDto = new GetRoomResponseDto(room);
        BasicResponse basicResponse = new BasicResponse<>(1, "채팅방 조회 성공", roomResponseDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/space/{spaceId}/room")//채팅방 생성
    @PreAuthorize("@customSecurityUtil.isSpaceOwner(#spaceId)")
    public ResponseEntity<CreateRoomResponse> createChattingRoom(@PathVariable("spaceId") Long spaceId, @RequestBody CreateRoomRequestDto roomRequestDto) {
        Space space = null;
        Long roomId = null;

        if (roomRequestDto.getMemberId().isPresent() && roomRequestDto.getFriendId().isPresent()) { //개인 채팅방 생성
            Long memberId = roomRequestDto.getMemberId().get();
            Long friendId= roomRequestDto.getFriendId().get();
            Member friend = memberService.findOne(friendId);
            Member member = memberService.findOne(memberId);

            space = member.getMemberSpace();
            Space friendSpace = friend.getMemberSpace();

            //서로 친구인지 확인하고 아니라면 예외가 터짐
            roomService.isFriend(memberId, friendId);

            //본인 채팅방 생성
            roomId = roomService.makePersonalChattingRoom(space, roomRequestDto.getRoomName(),  memberId, friendId);
            //친구에게도 채팅방 생성
            Long friendRoomId = roomService.makePersonalChattingRoom(friendSpace , roomRequestDto.getRoomName(), friendId, memberId);
            // 두 채팅방의 상대 채팅방 번호 셋팅
            roomService.makeRoomFriendRelation(roomId, friendRoomId);


        }
        else if (roomRequestDto.getTeamId().isPresent()) {  //팀 채팅방 생성
            Team team = teamService.findOne(roomRequestDto.getTeamId().get());
            space = team.getTeamSpace();
            roomId = roomService.makeTeamChattingRoom(space, roomRequestDto.getRoomName());
        }
        else {
            //팀 또는 회원 아이디 정보가 안 실려온 경우
            throw new RequiredValueMissingException("ID 정보 부족");
        }
        CreateRoomResponse createRoomResponse = new CreateRoomResponse(roomId, CREATED, "채팅방 생성 완료");
        return new ResponseEntity<>(createRoomResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/message") //메세지 보냄
    @PreAuthorize("@customSecurityUtil.isRoomOwner(#roomId)")
    public ResponseEntity<CreateMessageResponse> createMessage(@PathVariable("roomId") Long roomId, @RequestBody CreateMessageRequestDto messageRequestDto) {

        Room room = roomService.findOne(roomId);
        Member member = memberService.findOne(messageRequestDto.getSenderId());
        Long messageId = messageService.makeMessage(member, room, messageRequestDto.getContent());

        if(room.getFriendRoomId() != null){ //채팅방이 개인채팅방인경우 상대쪽 채팅방에도 메세지 날려줌
            Room friendRoom = roomService.findOne(room.getFriendRoomId());
            Member friend = memberService.findOne(friendRoom.getMemberId());
            Long sendingMessageId = messageService.makeMessage(member, friendRoom, messageRequestDto.getContent());
        }
        CreateMessageResponse createMessageResponse = new CreateMessageResponse(messageId, CREATED, "메세지 전송 완료");
        return new ResponseEntity<>(createMessageResponse, HttpStatus.CREATED);
    }


}
