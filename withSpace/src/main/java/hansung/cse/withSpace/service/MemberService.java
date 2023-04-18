package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.exception.member.MemberUpdateException;
import hansung.cse.withSpace.exception.member.join.DuplicateEmailException;
import hansung.cse.withSpace.repository.MemberRepository;
import hansung.cse.withSpace.repository.PageRepository;
import hansung.cse.withSpace.repository.ScheduleRepository;
import hansung.cse.withSpace.repository.SpaceRepository;
import hansung.cse.withSpace.requestdto.member.MemberJoinRequestDto;
import hansung.cse.withSpace.requestdto.member.MemberUpdateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 필드 생성자 주입 코드 자동 생성
public class MemberService {

    private final MemberRepository memberRepository;
    private final SpaceService spaceService;
    private final ScheduleService scheduleService;
    private final PageService pageService;

//    private final SpaceRepository spaceRepository;
//    private final ScheduleRepository scheduleRepository;
//    private final PageRepository pageRepository;

    //final private PasswordEncoder passwordEncoder; //비밀번호 암호화

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
    }

    public boolean existsByEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 email 입니다.");
        }
        return false;
    }

    @Transactional
    public Long join(MemberJoinRequestDto joinRequestDto) { //회원가입

        // 중복 이메일 검사
        existsByEmail(joinRequestDto.getEmail());


        Member member = new Member(joinRequestDto.getMemberName(), joinRequestDto.getEmail(), joinRequestDto.getPassword());
        memberRepository.save(member);

        //회원가입시 스페이스 생성(Member생성자에서 이루어짐) + 저장은 SpaceService에서
        Space memberSpace = spaceService.makeMemberSpace(member);

        //스페이스 생성했으니 바로 스케줄도 만들어서 줌..
        scheduleService.makeSchedule(memberSpace);

        //페이지도 만들어서 하나 넣어줌
        PageCreateRequestDto pageCreateRequestDto = new PageCreateRequestDto("새로운 페이지", null);
        pageService.makePage(memberSpace.getId(), pageCreateRequestDto);

        return member.getId();
    }




//    public Optional<Member> findOneWithMemberTeams(Long memberId){
//        return memberRepository.findByIdWithMemberTeams(memberId);
//    }

    @Transactional
    public void delete(Long memberId) {
        Member member = findOne(memberId);
        memberRepository.delete(member);
    }

    @Transactional
    public Long update(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto) {

        Member member = findOne(memberId);

        String email = memberUpdateRequestDto.getEmail();
        String password = memberUpdateRequestDto.getPassword();
        String memberName = memberUpdateRequestDto.getMemberName();

        if (email != null || password != null || memberName != null) {
            member.update(email, password, memberName);
            memberRepository.save(member);
        }

        // 업데이트
        memberRepository.save(member);
        return member.getId();
    }


    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
    }


}
