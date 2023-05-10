package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.exception.friend.FriendAddException;
import hansung.cse.withSpace.requestdto.friendship.FriendRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendBasicResponse;
import hansung.cse.withSpace.responsedto.friend.FriendDto;
import hansung.cse.withSpace.responsedto.friend.SendFriendShipResponseDto;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping("/{memberId}/friend")
    //@PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<BasicResponse> friend(@PathVariable("memberId") Long memberId, HttpServletRequest request) {
        //jwtAuthenticationFilter.isMemberOwner( request, memberId); //접근권한 확인
        Member member = memberService.findOne(memberId);
        List<Member> friendList = friendShipService.findFriendList(member);
        List<FriendDto> collect = friendList.stream()
                .map(f -> new FriendDto(f))
                .collect(Collectors.toList());
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "친구목록 요청 성공", collect);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/{memberId}/friend")
   //@PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<SendFriendShipResponseDto> sendFriendShip(@PathVariable("memberId") Long memberId,
                                                                    @RequestBody FriendRequestDto friendRequestDto,
                                                                    HttpServletRequest request) {
        //jwtAuthenticationFilter.isMemberOwner( request, memberId); //접근권한 확인
        //친구 요청 보낸 사람
        Member friendRequester = memberService.findOne(memberId);
        //친구 요청 받은 사람
        Member friendReceiver = memberService.findOne(friendRequestDto.getFriendId());

        if (friendRequester.getFriendRequester().get(0).getStatus().equals(FriendStatus.ACCEPTED))
            throw new FriendAddException("이미 친구관계를 맺은 회원입니다.");

        FriendShip friendShip = new FriendShip(friendRequester, friendReceiver);

        friendShipService.addFriend(friendShip);

        SendFriendShipResponseDto friendResponseDto = new SendFriendShipResponseDto(friendRequester.getId(), CREATED, "친구신청을 보냈습니다.");
        return new ResponseEntity<>(friendResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/friend/{memberId}/{friendId}")
    //@PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<FriendBasicResponse> deleteFriendShip(@PathVariable("memberId") Long memberId,
                                                                @PathVariable("friendId") Long friendId,
                                                                HttpServletRequest request) {
        //jwtAuthenticationFilter.isMemberOwner( request, memberId); //접근권한 확인
        friendShipService.deleteFriendShip(memberId, friendId);
        return new ResponseEntity<>(new FriendBasicResponse(SUCCESS, "친구 삭제가 정상적으로 되었습니다."), HttpStatus.OK);
    }
}
