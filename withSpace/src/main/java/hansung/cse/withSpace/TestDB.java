package hansung.cse.withSpace;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.PublicSetting;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryRequestDto;
import hansung.cse.withSpace.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDB {

    //scheduleInit()에 사용
    private final CategoryService categoryService;

    private final ScheduleService scheduleService;

    private final MemberService memberService;
    private final ToDoService toDoService;

    private final FriendShipService friendShipService;

    @PostConstruct
    public void postConstruct() {
        //scheduleInit();
        //friendInit();
    }

    @Transactional
    public void friendInit() {
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);

        FriendShip friendShip = new FriendShip(member1, member2);
        FriendShip friendShip1 = new FriendShip(member2, member1);
        FriendShip friendShip3 = new FriendShip(member3, member1);

        friendShipService.sendFriendRequest(friendShip);
        friendShipService.sendFriendRequest(friendShip1);
        friendShipService.sendFriendRequest(friendShip3);

        List<Member> friendList = friendShipService.findFriendList(member1);

        for (Member member1Friend : friendList) {
            System.out.println("member1Friend = " + member1Friend.getId());
        }

    }

    @Transactional
    public void scheduleInit() {
//        Member member1 = new Member();
//
//        memberService.save(member1);
//
//        Space memberSpace = new MemberSpace(member1);
//
//        Long scheduleId = scheduleService.makeSchedule(memberSpace);
//        Schedule schedule = scheduleService.findSchedule(scheduleId);
//
//
//        Category studyCategory = new Category(schedule, new CategoryRequestDto("공부", PublicSetting.PUBLIC, ""));
//        Category workOutCategory = new Category(schedule, new CategoryRequestDto("운동", PublicSetting.PRIVATE, ""));
//
//        categoryService.makeCategory(studyCategory);
//        categoryService.makeCategory(workOutCategory);
//
//        ToDo springToDo = new ToDo(studyCategory, "스프링 공부", false, LocalDateTime.now(), true, null);
//        ToDo codingToDo = new ToDo(studyCategory, "코딩 공부", false, LocalDateTime.now(), true, null);
//
//        toDoService.makeTodo(springToDo);
//        toDoService.makeTodo(codingToDo);
//
//        ToDo healthToDo = new ToDo(workOutCategory, "헬스", false, LocalDateTime.now(), true, null);
//        toDoService.makeTodo(healthToDo);
    }


}
