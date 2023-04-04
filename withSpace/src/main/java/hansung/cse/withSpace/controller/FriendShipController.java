package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.requestdto.friendship.FriendRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendBasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendDto;
import hansung.cse.withSpace.responsedto.friend.SendFriendShipResponseDto;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FriendShipController {

    private static final int SUCCESS = 200;

    private static final int CREATED = 201;


    private final FriendShipService friendShipService;
    private final MemberService memberService;

    @GetMapping("/{memberId}/friend")
    @PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<BasicResponse> friend(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        List<Member> friendList = friendShipService.findFriendList(member);
        List<FriendDto> collect = friendList.stream()
                .map(f -> new FriendDto(f))
                .collect(Collectors.toList());
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "친구목록 요청 성공", collect);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/friend")
    public ResponseEntity<SendFriendShipResponseDto> sendFriendShip(@RequestBody FriendRequestDto friendRequestDto) {
        //친구 요청 보낸 사람
        Member friendRequester = memberService.findOne(friendRequestDto.getId());
        //친구 요청 받은 사람
        Member friendReceiver = memberService.findOne(friendRequestDto.getFriendId());

        FriendShip friendShip = new FriendShip(friendRequester, friendReceiver);

        friendShipService.addFriend(friendShip);

        SendFriendShipResponseDto friendResponseDto = new SendFriendShipResponseDto(friendRequester.getId(), CREATED, "친구신청을 보냈습니다.");
        return new ResponseEntity<>(friendResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/friend/{memberId}/{friendId}")
    @PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<FriendBasicResponse> deleteFriendShip(@PathVariable("memberId") Long memberId, @PathVariable("friendId") Long friendId) {
        friendShipService.deleteFriendShip(memberId, friendId);
        return new ResponseEntity<>(new FriendBasicResponse(SUCCESS, "친구 삭제가 정상적으로 되었습니다."), HttpStatus.OK);
    }
}
