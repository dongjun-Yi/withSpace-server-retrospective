package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.requestdto.member.MemberJoinRequestDto;
import hansung.cse.withSpace.requestdto.member.MemberUpdateRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.member.GetMemberResponseDto;
import hansung.cse.withSpace.responsedto.member.JoinMemberResponse;
import hansung.cse.withSpace.responsedto.member.MemberBasicResponse;
import hansung.cse.withSpace.responsedto.member.UpdateMemberResponse;
import hansung.cse.withSpace.responsedto.space.MemberSpaceDto;
import hansung.cse.withSpace.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화

    @PostMapping("/member") //회원가입
    public ResponseEntity<JoinMemberResponse> joinMember(@Validated @RequestBody MemberJoinRequestDto memberJoinRequestDto) {
        String encodedPassword = passwordEncoder.encode(memberJoinRequestDto.getPassword()); // 비밀번호 암호화
        memberJoinRequestDto.setPassword(encodedPassword);
        Long memberId = memberService.join(memberJoinRequestDto);
        JoinMemberResponse joinMemberResponse = new JoinMemberResponse(memberId, CREATED, "회원가입 완료");
        return new ResponseEntity<>(joinMemberResponse, HttpStatus.CREATED);
    }

    @GetMapping("/member/{memberId}") //회원 단건 조회
    public ResponseEntity<BasicResponse> getMember(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        GetMemberResponseDto getMemberResponseDto = new GetMemberResponseDto(member);
        BasicResponse basicResponse = new BasicResponse<>(1, "회원 조회 성공", getMemberResponseDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}/space") //회원 스페이스 조회
    @PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)") // 현재 로그인한 사용자와 요청한 회원 ID가 일치하는 경우에만 접근 허용
    public ResponseEntity<BasicResponse> getMemberSpace(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        MemberSpaceDto memberSpaceDto = new MemberSpaceDto(member);
        BasicResponse basicResponse = new BasicResponse<>(1, "스페이스 조회 성공", memberSpaceDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/member/{memberId}") // 회원 삭제
    @PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<MemberBasicResponse> deleteMember(@PathVariable Long memberId) {
        memberService.delete(memberId);
        MemberBasicResponse memberBasicResponse = new MemberBasicResponse(SUCCESS, "회원 삭제가 정상적으로 되었습니다.");
        return new ResponseEntity<>(memberBasicResponse, HttpStatus.OK);

    }

    @PatchMapping("/member/{memberId}") // 회원 업데이트
    @PreAuthorize("@customSecurityUtil.isMemberOwner(#memberId)")
    public ResponseEntity<UpdateMemberResponse> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        Long updatedMemberId = memberService.update(memberId, memberUpdateRequestDto);
        UpdateMemberResponse updateMemberResponse = new UpdateMemberResponse(updatedMemberId, SUCCESS, "멤버 업데이트 완료");
        return new ResponseEntity<>(updateMemberResponse, HttpStatus.OK);
    }

    @GetMapping("/api/member") //로그인한 회원 조회 api
    public ResponseEntity<BasicResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberService.findByEmail(email);

        GetMemberResponseDto getMemberResponseDto = new GetMemberResponseDto(member);
        BasicResponse basicResponse = new BasicResponse<>(1, "회원 조회 성공",  getMemberResponseDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
    @PostMapping("/api/logout") //로그아웃
    public ResponseEntity<BasicResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        BasicResponse basicResponse = new BasicResponse<>(1, "로그아웃 성공", null);
        return ResponseEntity.ok(basicResponse);
    }

}
