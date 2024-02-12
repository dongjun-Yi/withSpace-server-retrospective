package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.exception.friend.FriendRequestException;
import hansung.cse.withSpace.requestdto.friendship.FriendRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendBasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendDto;
import hansung.cse.withSpace.responsedto.friend.SendFriendShipResponseDto;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FriendShipController {

    private static final int SUCCESS = 200;

    private static final int CREATED = 201;

    private static final int CONFLICT = 409;


    private final FriendShipService friendShipService;
    private final MemberService memberService;
    private final RoomService roomService;

    @GetMapping("/member/{memberId}/friend")
    public ResponseEntity<BasicResponse> getFriend(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        List<Member> friendList = friendShipService.findFriendList(member);
        List<FriendDto> collect = friendList.stream()
                .map(FriendDto::new)
                .collect(Collectors.toList());
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "친구목록 요청 성공", collect);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/member/{memberId}/friend-request") // 친구신청
    public ResponseEntity<SendFriendShipResponseDto> sendFriendShip(@PathVariable("memberId") Long memberId,
                                                                    @Valid @RequestBody FriendRequestDto friendRequestDto) {
        Long friendShipId = null;

        //친구 요청 보낸 사람
        Member friendRequester = memberService.findOne(memberId);
        //친구 요청 받은 사람
        Member friendReceiver = memberService.findOne(friendRequestDto.getFriendId());

        FriendShip friendShip = new FriendShip(friendRequester, friendReceiver);


        try {
            friendShipId = friendShipService.sendFriendRequest(friendShip);
        } catch (FriendRequestException e) {
            SendFriendShipResponseDto friendResponseDto
                    = new SendFriendShipResponseDto(friendShipId, CONFLICT, e.getMessage());
            return new ResponseEntity<>(friendResponseDto, HttpStatus.CREATED);
        }

        SendFriendShipResponseDto friendResponseDto
                = new SendFriendShipResponseDto(friendShipId, CREATED, "친구신청을 보냈습니다.");
        return new ResponseEntity<>(friendResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/member/{memberId}/friend/{friendId}")
    public ResponseEntity<FriendBasicResponse> deleteFriendShip(@PathVariable("memberId") Long memberId,
                                                                @PathVariable("friendId") Long friendId) {
        friendShipService.deleteFriendShip(memberId, friendId);

        roomService.removeFriendRoom(memberId, friendId); //친구 삭제시 채팅방도 삭제

        return new ResponseEntity<>(new FriendBasicResponse(SUCCESS, "친구 삭제가 정상적으로 되었습니다."), HttpStatus.OK);
    }

    @GetMapping("member/{memberId}/friend-received")
    public ResponseEntity<BasicResponse> friendReceivedList(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        List<Member> friendReceiveList = friendShipService.findFriendReceiveList(member.getId());
        List<FriendDto> collect = friendReceiveList.stream()
                .map(f -> new FriendDto(f))
                .collect(Collectors.toList());
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "친구요청목록 요청 성공", collect);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("member/{memberId}/friend-reject")
    public ResponseEntity<FriendBasicResponse> friendRequestReject(@PathVariable("memberId") Long memberId, @RequestBody FriendRequestDto friendRequestDto) {
        Member member = memberService.findOne(memberId);
        friendShipService.rejectFriendShip(member.getId(), friendRequestDto.getFriendId());
        return new ResponseEntity<>(new FriendBasicResponse(SUCCESS, "친구 신청을 거절했습니다."), HttpStatus.OK);
    }
}
