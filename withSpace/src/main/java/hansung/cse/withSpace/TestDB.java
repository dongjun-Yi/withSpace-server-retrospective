package hansung.cse.withSpace;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.requestdto.member.MemberJoinRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import hansung.cse.withSpace.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestDB {

    //scheduleInit()에 사용
    private final CategoryService categoryService;

    private final ScheduleService scheduleService;

    private final ToDoService toDoService;

    //friendInit()에 사용

    private final FriendShipService friendShipService;

    private final MemberService memberService;

    // teamInit()에 사용
    private final TeamService teamService;

    // pageInit()에 사용
    private final PageService pageService;
    private final BlockService blockService;


    @PostConstruct
    public void postConstruct() {
        scheduleInit();
        friendInit();
        teamInit();
        pageInit();
    }



    @Transactional
    public void pageInit() {
        MemberJoinRequestDto mj = new MemberJoinRequestDto("pageMember", "page@naver.com", "비밀번호");
        Long join = memberService.join(mj);
        Long spaceId = memberService.findOne(join).get().getMemberSpace().getId();

        PageCreateRequestDto firstPageDto = new PageCreateRequestDto("페이지 제목", null);

        Long firstPageId = pageService.makePage(spaceId, firstPageDto);

        PageCreateRequestDto secondPageDto= new PageCreateRequestDto("페이지 제목2", Optional.ofNullable(firstPageId));
        pageService.makePage(spaceId, secondPageDto);

        PageCreateRequestDto thirdPageDto= new PageCreateRequestDto("페이지 제목3", Optional.ofNullable(firstPageId));
        pageService.makePage(spaceId, thirdPageDto);

        //블록생성
        Long blockId1 = blockService.makeBlock(firstPageId, join);
        Long blockId2 = blockService.makeBlock(firstPageId, join);

    }

    @Transactional
    public void teamInit() {

        MemberJoinRequestDto  mj1 = new MemberJoinRequestDto("memberA_makeTeam", "aaaT@naver.com", "비밀번호1");
        MemberJoinRequestDto  mj2 = new MemberJoinRequestDto("memberB_makeTeam", "bbbT@naver.com", "비밀번호2");
        MemberJoinRequestDto  mj3 = new MemberJoinRequestDto("memberC_makeTeam", "cccT@naver.com", "비밀번호3");

        Long join1 = memberService.join(mj1);
        Long join2 = memberService.join(mj2);
        Long join3 = memberService.join(mj3);

//        Long join1 = memberService.join("memberA_makeTeam", "aaaT@naver.com", "비밀번호1");
//        Long join2 = memberService.join("memberB_joinTeam", "bbbT@naver.com", "비밀번호2");
//        Long join3 = memberService.join("memberC_joinTeam", "cccT@naver.com", "비밀번호3");

        Member memberA = memberService.findOne(join1).get();
        Member memberB = memberService.findOne(join2).get();
        Member memberC = memberService.findOne(join3).get();

        //팀생성 - A가 만듦
        Long teamId = teamService.makeTeam(memberA, "A가 만든 팀");
        Team team = teamService.findOne(teamId).get();

        // A가 만든 팀에 B,C 가입
        teamService.join(memberB, teamId);
        teamService.join(memberC, teamId);
    }

    @Transactional
    public void friendInit() {

        MemberJoinRequestDto  mj1 = new MemberJoinRequestDto("memberA", "aaa@naver.com", "비밀번호1");
        MemberJoinRequestDto  mj2 = new MemberJoinRequestDto("memberB", "bbb@naver.com", "비밀번호1");
        MemberJoinRequestDto  mj3 = new MemberJoinRequestDto("memberC", "ccc@naver.com", "비밀번호3");

        Long join1 = memberService.join(mj1);
        Long join2 = memberService.join(mj2);
        Long join3 = memberService.join(mj3);

        Member memberA = memberService.findOne(join1).get();
        Member memberB = memberService.findOne(join2).get();
        Member memberC = memberService.findOne(join3).get();


        //A와 B가 친구
        FriendShip friendShip1 = new FriendShip(memberA, memberB);
        FriendShip friendShip2 = new FriendShip(memberB, memberA);

        //A와 C가 친구
        FriendShip friendShip3 = new FriendShip(memberA, memberC);
        FriendShip friendShip4 = new FriendShip(memberC, memberA);


        friendShipService.addFriend(friendShip1);
        friendShipService.addFriend(friendShip2);

        friendShipService.addFriend(friendShip3);
        friendShipService.addFriend(friendShip4);
    }

    @Transactional
    public void scheduleInit() {

        MemberJoinRequestDto  mj1 = new MemberJoinRequestDto("memberD", "ddd@naver.com", "비밀번호4");
        Long join = memberService.join(mj1);

        Member memberA = memberService.findOne(join).get();

        Schedule schedule = memberA.getMemberSpace().getSchedule();

        Category studyCategory = new Category(schedule, "공부");
        Category workOutCategory = new Category(schedule, "운동");

        categoryService.makeCategory(studyCategory);
        categoryService.makeCategory(workOutCategory);

        ToDo springToDo = new ToDo(studyCategory, "스프링 공부", false, LocalDateTime.now());
        ToDo codingToDo = new ToDo(studyCategory, "코딩 공부", false, LocalDateTime.now());

        toDoService.makeTodo(springToDo);
        toDoService.makeTodo(codingToDo);

        ToDo healthToDo = new ToDo(workOutCategory, "헬스", false, LocalDateTime.now());
        toDoService.makeTodo(healthToDo);
    }


}
