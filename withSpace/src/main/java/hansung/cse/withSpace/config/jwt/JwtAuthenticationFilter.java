package hansung.cse.withSpace.config.jwt;

import hansung.cse.withSpace.config.auth.CustomUserDetails;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.MemberTeam;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.exception.friend.NotFriendException;
import hansung.cse.withSpace.exception.jwt.TokenNotFoundException;
import hansung.cse.withSpace.service.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JwtTokenUtil jwtTokenUtil;
    private final TeamService teamService;
    private final MemberService memberService;
    private final SpaceService spaceService;
    private final PageService pageService;
    private final ScheduleService scheduleService;
    private final CategoryService categoryService;
    private final ToDoService toDoService;
    private final RoomService roomService;
    private final FriendShipService friendShipService;

    @Value("${jwt.secret}")
    private String secret;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, IOException {
        String token = extractToken(request); //토큰 추출
        if (token != null && jwtTokenUtil.validateToken(token)) { //토큰 검증
            Authentication authentication = getAuthentication(token); //로그인된 사용자
            System.out.println("authentication = " + authentication);
            log.info("Authentication before setting in SecurityContextHolder: " + authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Log the state of the authentication object after setting it in the SecurityContextHolder
            Authentication authenticationAfter = SecurityContextHolder.getContext().getAuthentication();
            log.info("Authentication after setting in SecurityContextHolder: " + authenticationAfter);

        }
        filterChain.doFilter(request, response);
    }
//    public Authentication getAuthentication() {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }
    private Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()//파싱할 수 있는 빌더객체 생성
                .setSigningKey(jwtTokenUtil.getSigningKey())//JWT 토큰을 검증할 때 사용할 서명 키를 설정
                .build()//빌더 객체를 빌드
                .parseClaimsJws(token) //파싱
                .getBody(); //JWT 토큰의 payload 얻어내기 << uuid와 같은 정보를 넣어둠

        SecurityContextHolder.getContext().getAuthentication();

        //String username = claims.getSubject();
        Long memberId = claims.get("id", Long.class);
        Member member = memberService.findOne(memberId);
        // UserDetails 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(member.getId(), member.getUuid(),
                member.getEmail(), member.getPassword(), member.getMemberName());

        return new UsernamePasswordAuthenticationToken(customUserDetails, token, customUserDetails.getAuthorities());
    }


    private String extractToken(HttpServletRequest request) { //토큰 추출
        String bearerToken = request.getHeader("JWT-Authorization");
        System.out.println("bearerToken = " + bearerToken);
        System.out.println("StringUtils.hasText(bearerToken) = " + StringUtils.hasText(bearerToken));
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            System.out.println("Extracted Token: " + token); // For Debugging
            return token;
        }
        return null;
    }

    public UUID getUUIDFromToken(HttpServletRequest request) {

        //jwt로 부터 uuid 얻어내기

        String token = extractToken(request); //Authorization 의 value를 추출

        System.out.println("token = " + token);

        if (token != null && jwtTokenUtil.validateToken(token)) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenUtil.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String uuidString = claims.get("UUID", String.class);
            UUID uuid = UUID.fromString(uuidString);
            return uuid;
        }
        throw new TokenNotFoundException("request에 JWT 토큰이 없습니다.");
    }

    public boolean checkUUID(HttpServletRequest request, Long memberId) {

        //토큰의 uuid와 DB상 회원의 uuid가 일치하는지 검사
        Member member = memberService.findOne(memberId);// 회원 먼저 확인
        UUID uuid = member.getUuid();

        return uuid.equals(getUUIDFromToken(request));
    }

    public Member findMemberByUUID(HttpServletRequest request) {
        UUID uuid = getUUIDFromToken(request);
        return memberService.findByUuid(uuid);
    }

//    public Authentication getAuthentication() { //현재 로그인된 사용자의 인증정보를 가져옴
//        return SecurityContextHolder.getContext().getAuthentication();
//    }

    public boolean checkSpaceOwner(HttpServletRequest request, Long spaceId) {
        Space space = spaceService.findOne(spaceId); //먼저 스페이스가 있는지 확인해줌
        Member member = findMemberByUUID(request);

        boolean memberSpaceOwner = member.getMemberSpace().getId().equals(spaceId); //본인의 스페이스인지 확인
        boolean teamSpaceOwner = member.getMemberTeams().stream()// 가입된 팀의 스페이스인지 확인
                .map(MemberTeam::getTeam)
                .filter(Objects::nonNull)
                .map(Team::getTeamSpace)
                .filter(Objects::nonNull)
                .anyMatch(teamSpace -> teamSpace.getId().equals(spaceId));

        return memberSpaceOwner || teamSpaceOwner;
    }


    public boolean isMemberOwner(HttpServletRequest request, Long memberId) {
        boolean check = checkUUID(request, memberId);
        if (!check) {
            throw new AccessDeniedException("회원 id: " + memberId + " 에 대한 접근 권한이 없습니다.");
        }
        return true;
    }

    public boolean isSpaceOwner(HttpServletRequest request, Long spaceId) {
        boolean check = checkSpaceOwner(request, spaceId);
        if (!check) {
            throw new AccessDeniedException("스페이스 id: " + spaceId + " 에 대한 접근 권한이 없습니다.");
        }
        return true;
    }

    public boolean isMemberInTeam(HttpServletRequest request, Long teamId) { //팀에 가입되었는지 확인

        teamService.findOne(teamId); //팀이 있는지 먼저 확인
        Member member = findMemberByUUID(request);

        return member.getMemberTeams().stream()
                .map(MemberTeam::getTeam)
                .anyMatch(teams -> teams.getId().equals(teamId)); //가입되어있는지 확인

    }


//    public boolean isSpaceOwner(HttpServletRequest request, Long spaceId) {
//        Space space = spaceService.findOne(spaceId); //먼저 스페이스가 있는지 확인해줌
//        Member member = findMemberByUUID(request);
//
//        boolean memberSpaceOwner = member.getMemberSpace().getId().equals(spaceId); //본인의 스페이스인지 확인
//        boolean teamSpaceOwner = member.getMemberTeams().stream()// 가입된 팀의 스페이스인지 확인
//                .map(MemberTeam::getTeam)
//                .filter(Objects::nonNull)
//                .map(Team::getTeamSpace)
//                .filter(Objects::nonNull)
//                .anyMatch(teamSpace -> teamSpace.getId().equals(spaceId));
//
//        if (!memberSpaceOwner &&  !teamSpaceOwner) {
//            throw new AccessDeniedException("space id: "+ spaceId+" 에 대한 접근 권한이 없습니다.");
//        }
//
//        return true;
//    }

    public boolean isPageOwner(HttpServletRequest request, Long pageId) {

        //페이지의 스페이스가 null일수도 있음
        Page page = pageService.findOne(pageId);
        Optional<Space> optionalSpace = Optional.ofNullable(page.getSpace());
        Long spaceId = null;
        if (optionalSpace.isPresent()) {
            spaceId = pageService.findOne(pageId).getSpace().getId();
        }
        else{ //페이지를 쓰레기통에 넣은경우
            spaceId = page.getTrashCan().getSpace().getId();
        }
        boolean check = checkSpaceOwner(request, spaceId);
        if (!check) {
            throw new AccessDeniedException("페이지 id: " + pageId + " 에 대한 접근 권한이 없습니다.");
        }
        return true;
    }

    public boolean isScheduleOwner(HttpServletRequest request, Long scheduleId) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        Long spaceId = schedule.getSpace().getId();
        boolean check = checkSpaceOwner(request, spaceId);
        if (!check) {
            throw new AccessDeniedException("스케줄 id: " + scheduleId + " 에 대한 접근 권한이 없습니다.");
        }
        return true;
    }
    public boolean isCategoryOwner(HttpServletRequest request, Long categoryId) {
        Category category = categoryService.findCategory(categoryId);
        return isScheduleOwner(request, category.getSchedule().getId());
    }
    public boolean isToDoOwner(HttpServletRequest request,Long toDoId) {
        ToDo toDo = toDoService.findToDo(toDoId);
        return isCategoryOwner(request, toDo.getCategory().getId());
    }

    public boolean isRoomOwner(HttpServletRequest request,Long roomId) { //채팅방 접근권 확인
        Room room = roomService.findOne(roomId);
        Long spaceId = room.getSpace().getId();
        boolean check = checkSpaceOwner(request, spaceId);
        if (!check) {
            throw new AccessDeniedException("방 id: " + roomId + " 에 대한 접근 권한이 없습니다.");
        }
        return true;
    }

    public boolean isTeamHost(HttpServletRequest request, Long teamId) { //팀장인지 확인

        Team team = teamService.findOne(teamId);
        Long memberId = findMemberByUUID(request).getId();
        boolean check = memberId.equals(team.getHost());
        if (!check) {
            throw new AccessDeniedException("팀 id: " + teamId + " 의 팀장만이 접근 가능합니다.");
        }
        return true;
    }

    public boolean isFriend(HttpServletRequest request, Long friendId) { //서로 친구인지 확인
        Member me = findMemberByUUID(request);
        Member friend = memberService.findOne(friendId);
        boolean check = friendShipService.isFriend(me.getId(), friendId);
        if (!check) {
            throw new NotFriendException("두 회원이 친구가 아닙니다.");
        }
        return true;
    }

}
