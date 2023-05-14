//package hansung.cse.withSpace;
//
//import hansung.cse.withSpace.domain.Member;
//import hansung.cse.withSpace.domain.Team;
//import hansung.cse.withSpace.domain.chat.Message;
//import hansung.cse.withSpace.domain.chat.Room;
//import hansung.cse.withSpace.domain.friend.FriendShip;
//import hansung.cse.withSpace.domain.space.schedule.Category;
//import hansung.cse.withSpace.domain.space.schedule.PublicSetting;
//import hansung.cse.withSpace.domain.space.schedule.Schedule;
//import hansung.cse.withSpace.domain.space.schedule.ToDo;
//import hansung.cse.withSpace.requestdto.member.MemberJoinRequestDto;
//import hansung.cse.withSpace.requestdto.schedule.category.CategoryRequestDto;
//import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
//import hansung.cse.withSpace.service.*;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class TestDB {
//
//    //scheduleInit()에 사용
//    private final CategoryService categoryService;
//
//    private final ScheduleService scheduleService;
//
//    private final ToDoService toDoService;
//
//    //friendInit()에 사용
//
//    private final FriendShipService friendShipService;
//
//    private final MemberService memberService;
//
//    // teamInit()에 사용
//    private final TeamService teamService;
//
//    // pageInit()에 사용
//    private final PageService pageService;
//    private final BlockService blockService;
//
//    //회원가입시에 컨트롤러에서 암호화 해주므로 더미데이터 집어넣을땐 암호화를 따로 해줘야함
//    private final PasswordEncoder passwordEncoder;
//
//    private final MessageService messageService;
//    private final RoomService roomService;
//
//
//    @PostConstruct
//    public void postConstruct() {
//        scheduleInit();
//        friendInit();
//        teamInit();
//        pageInit();
//    }
//
//
//    @Transactional
//    public void pageInit() {
//
//        MemberJoinRequestDto mj = new MemberJoinRequestDto("page@naver.com", "pageMember", passwordEncoder.encode("password"));
//        Long join = memberService.join(mj);
//        Long spaceId = memberService.findOne(join).getMemberSpace().getId();
//
//        PageCreateRequestDto firstPageDto = new PageCreateRequestDto("페이지 제목", null);
//
//        Long firstPageId = pageService.makePage(spaceId, firstPageDto);
//
//        PageCreateRequestDto secondPageDto = new PageCreateRequestDto("페이지 제목2", Optional.ofNullable(firstPageId));
//        pageService.makePage(spaceId, secondPageDto);
//
//        PageCreateRequestDto thirdPageDto = new PageCreateRequestDto("페이지 제목3", Optional.ofNullable(firstPageId));
//        pageService.makePage(spaceId, thirdPageDto);
//
//        //블록생성
//        Long blockId1 = blockService.makeBlock(firstPageId, join);
//        Long blockId2 = blockService.makeBlock(firstPageId, join);
//
//    }
//
//    @Transactional
//    public void teamInit() {
//
//        MemberJoinRequestDto mj1 = new MemberJoinRequestDto("aaaT@naver.com", "memberA_makeTeam", passwordEncoder.encode("password1"));
//        MemberJoinRequestDto mj2 = new MemberJoinRequestDto("bbbT@naver.com", "memberB_makeTeam", passwordEncoder.encode("password2"));
//        MemberJoinRequestDto mj3 = new MemberJoinRequestDto("cccT@naver.com", "memberC_makeTeam", passwordEncoder.encode("password3"));
//
//        Long join1 = memberService.join(mj1);
//        Long join2 = memberService.join(mj2);
//        Long join3 = memberService.join(mj3);
//
////        Long join1 = memberService.join("memberA_makeTeam", "aaaT@naver.com", "비밀번호1");
////        Long join2 = memberService.join("memberB_joinTeam", "bbbT@naver.com", "비밀번호2");
////        Long join3 = memberService.join("memberC_joinTeam", "cccT@naver.com", "비밀번호3");
//
//        Member memberA = memberService.findOne(join1);
//        Member memberB = memberService.findOne(join2);
//        Member memberC = memberService.findOne(join3);
//
//        //팀생성 - A가 만듦
//        Long teamId = teamService.makeTeam(memberA, "A가 만든 팀");
//        Team team = teamService.findOne(teamId);
//
//        // A가 만든 팀에 B,C 가입
//        teamService.join(memberB, teamId);
//        teamService.join(memberC, teamId);
//    }
//
//    @Transactional
//    public void friendInit() {
//
//        MemberJoinRequestDto mj1 = new MemberJoinRequestDto("aaa@naver.com", "memberA", passwordEncoder.encode("password1"));
//        MemberJoinRequestDto mj2 = new MemberJoinRequestDto("bbb@naver.com", "memberB", passwordEncoder.encode("password2"));
//        MemberJoinRequestDto mj3 = new MemberJoinRequestDto("ccc@naver.com", "memberC", passwordEncoder.encode("password3"));
//
//        Long join1 = memberService.join(mj1);
//        Long join2 = memberService.join(mj2);
//        Long join3 = memberService.join(mj3);
//
//        Member memberA = memberService.findOne(join1);
//        Member memberB = memberService.findOne(join2);
//        Member memberC = memberService.findOne(join3);
//
//
//        //A와 B가 친구
//        FriendShip friendShip1 = new FriendShip(memberA, memberB);
//        FriendShip friendShip2 = new FriendShip(memberB, memberA);
//
//        //A와 C가 친구
//        FriendShip friendShip3 = new FriendShip(memberA, memberC);
//        FriendShip friendShip4 = new FriendShip(memberC, memberA);
//
//
//        friendShipService.addFriend(friendShip1);
//        friendShipService.addFriend(friendShip2);
//
//        friendShipService.addFriend(friendShip3);
//        friendShipService.addFriend(friendShip4);
//    }
//
//    @Transactional
//    public void scheduleInit() {
//
//        //member_id 1번 애
//        MemberJoinRequestDto mj1 = new MemberJoinRequestDto("test1@naver.com", "memberD", passwordEncoder.encode("password4"));
//        Long join = memberService.join(mj1);
//
//        Member memberD = memberService.findOne(join);
//
//        Schedule schedule = memberD.getMemberSpace().getSchedule();
//
//        Category studyCategory = new Category(schedule, new CategoryRequestDto("공부", PublicSetting.PUBLIC, 1));
//        Category workOutCategory = new Category(schedule, new CategoryRequestDto("운동", PublicSetting.PRIVATE, 2));
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
//
//        // 팀+친구 데이터 추가
//        MemberJoinRequestDto mj2 = new MemberJoinRequestDto("test2@naver.com", "memberA : D의 팀원+D의 친구", passwordEncoder.encode("password1"));
//        MemberJoinRequestDto mj3 = new MemberJoinRequestDto("test3@naver.com", "memberB : D의 팀원+D의 친구", passwordEncoder.encode("password2"));
//        MemberJoinRequestDto mj4 = new MemberJoinRequestDto("test4@naver.com", "memberC : D의 팀원", passwordEncoder.encode("password3"));
//
//        Long join1 = memberService.join(mj2);
//        Long join2 = memberService.join(mj3);
//        Long join3 = memberService.join(mj4);
//
//        Member memberA = memberService.findOne(join1);
//        Member memberB = memberService.findOne(join2);
//        Member memberC = memberService.findOne(join3);
//
//        //D가 A,B와 친구
//        FriendShip friendShip1 = new FriendShip(memberA, memberD);
//        FriendShip friendShip2 = new FriendShip(memberD, memberA);
//        FriendShip friendShip3 = new FriendShip(memberD, memberB);
//        FriendShip friendShip4 = new FriendShip(memberB, memberD);
//        friendShipService.addFriend(friendShip1);
//        friendShipService.addFriend(friendShip2);
//        friendShipService.addFriend(friendShip3);
//        friendShipService.addFriend(friendShip4);
//
//
//        //D가 팀을 생성
//        Long teamId1 = teamService.makeTeam(memberD, "D(member1)가 만든 팀 1");   //팀 pk1
//        //채팅방 id는 request에 담겨온다고 가정
//        Long roomId = 1L;
//        Room room = roomService.findOne(roomId);
//        Message message = messageService.makeMessage(memberD, room, "채팅내용");
//
//
//
//        Long teamId2 = teamService.makeTeam(memberD, "D(member1)가 만든 팀 2");   //팀 pk2
//
//        //팀 1 - A와 B가 가입
//        //Team team1 = teamService.findOne(teamId1).get();
//        teamService.join(memberA, teamId1);
//        teamService.join(memberB, teamId1);
//
//        //팀2  - C가 가입
//        //Team team2 = teamService.findOne(teamId2).get();
//        teamService.join(memberC, teamId2);
//
//        //회원에게 페이지 추가
//        Long spaceId = memberService.findOne(join).getMemberSpace().getId();
//
//        PageCreateRequestDto firstPageDto = new PageCreateRequestDto("페이지 제목", null);
//
//        Long firstPageId = pageService.makePage(spaceId, firstPageDto);
//
//        PageCreateRequestDto secondPageDto = new PageCreateRequestDto("페이지 제목2", Optional.ofNullable(firstPageId));
//        pageService.makePage(spaceId, secondPageDto);
//
//        PageCreateRequestDto thirdPageDto = new PageCreateRequestDto("페이지 제목3", Optional.ofNullable(firstPageId));
//        pageService.makePage(spaceId, thirdPageDto);
//
//        //블록생성
//        Long blockId1 = blockService.makeBlock(firstPageId, join);
//        Long blockId2 = blockService.makeBlock(firstPageId, join);
//
//        //팀 1 스페이스에게 페이지 할당
//
//        Long teamSpaceId = teamService.findOne(teamId1).getTeamSpace().getId();
//
//        PageCreateRequestDto teamPageDto1 = new PageCreateRequestDto("팀에게 줄 페이지1", null);
//        Long teamPageId1 = pageService.makePage(teamSpaceId, teamPageDto1);
//
//        PageCreateRequestDto teamPageDto2 = new PageCreateRequestDto("팀에게 줄 페이지2", Optional.ofNullable(teamPageId1));
//        pageService.makePage(teamSpaceId, teamPageDto2);
//
//        PageCreateRequestDto teamPageDto3 = new PageCreateRequestDto("팀에게 줄 페이지3", Optional.ofNullable(teamPageId1));
//        pageService.makePage(teamSpaceId, teamPageDto3);
//
//
//    }
//
//
//}
