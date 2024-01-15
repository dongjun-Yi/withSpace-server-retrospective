package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.MemberSpace;
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

        MemberSpace memberSpace = new MemberSpace(member);

        //when
        Long scheduleId = scheduleService.makeSchedule(memberSpace);

        //then
        Long savedScheduleId = memberSpace.getSchedule().getId();
        assertThat(scheduleId).isEqualTo(savedScheduleId);
    }

    @DisplayName("회원가입 시 할당받은 스케줄 번호로 스케줄에 대한 정보를 조회한다.")
    @Test
    void getSchedule() {
        //given
        Member member = new Member();
        memberService.save(member);

        MemberSpace memberSpace = new MemberSpace(member);
        Long scheduleId = scheduleService.makeSchedule(memberSpace);

        //when
        Long findScheduleId = scheduleService.findSchedule(scheduleId).getId();

        //then
        assertThat(findScheduleId).isEqualTo(scheduleId);
    }
}