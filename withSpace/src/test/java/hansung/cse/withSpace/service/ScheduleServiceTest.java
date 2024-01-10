package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Space;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    @DisplayName("회원의 작업공간 하나당 하나의 스케줄이 부여된다.")
    @Test
    void makeSchedule() {
        //given
        Member member = new Member();
        memberService.save(member);

        Space memberSpace = new MemberSpace(member);

        //when
        Long scheduleId = scheduleService.makeSchedule(memberSpace);
        Long scheduleIdFromMemberSpace = memberSpace.getSchedule().getId();

        //then
        assertThat(scheduleId).isEqualTo(scheduleIdFromMemberSpace);
    }
}