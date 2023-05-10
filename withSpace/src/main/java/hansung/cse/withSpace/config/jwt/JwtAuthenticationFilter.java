package hansung.cse.withSpace.config.jwt;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.MemberTeam;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.jwt.TokenNotFoundException;
import hansung.cse.withSpace.service.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final TeamService teamService;
    private final MemberService memberService;
    private final SpaceService spaceService;
    private final PageService pageService;
    private final ScheduleService scheduleService;
    private final CategoryService categoryService;
    private final ToDoService toDoService;
    private final RoomService roomService;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.e(request);
        if (token != null && jwtTokenUtil.validateToken(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    public UUID getUUIDFromToken(HttpServletRequest request) {

        //jwt로 부터 uuid 얻어내기

        String token = JwtTokenExtractor.extract(request); //JWT-Authorization 의 value를 추출
        if (token != null && tokenUtil.validateToken(token)) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenUtil.getSigningKey())
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

        UUID uuid = getUUIDFromToken(request); // JWT 검증 및 추출
        Member member = memberService.findOne(memberId);// 회원 먼저 확인

        return uuid.equals(member.getUuid());
    }

    public Member findMemberByUUID(HttpServletRequest request) {
        UUID uuid = getUUIDFromToken(request);
        return memberService.findByUuid(uuid);
    }



    public void isMemberOwner(HttpServletRequest request, Long memberId) {

        boolean check = checkUUID(request, memberId);

        if (!check) {
            throw new AccessDeniedException("회원 id: "+ memberId+" 에 대한 접근 권한이 없습니다.");
        }

    }

    public void isSpaceOwner(HttpServletRequest request, Long spaceId) {
        Space space = spaceService.findOne(spaceId); //먼저 스페이스가 있는지 확인해줌
        Member member = findMemberByUUID(request);

        boolean memberSpaceOwner = member.getMemberSpace().getId().equals(spaceId); //본인의 스페이스인지 확인

        boolean teamSpaceOwner = member.getMemberTeams().stream()// 가입된 팀의 스페이스인지 확인
                .map(MemberTeam::getTeam)
                .filter(Objects::nonNull)
                .map(Team::getTeamSpace)
                .filter(Objects::nonNull)
                .anyMatch(teamSpace -> teamSpace.getId().equals(spaceId));

        System.out.println("memberSpaceOwner = " + memberSpaceOwner);
        System.out.println("teamSpaceOwner = " + teamSpaceOwner);


        if (!memberSpaceOwner &&  !teamSpaceOwner) {
            throw new AccessDeniedException("space id: "+ spaceId+" 에 대한 접근 권한이 없습니다.");
        }

    }

    public void isPageOwner(HttpServletRequest request, Long pageId) {

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

        isSpaceOwner(request, spaceId);
    }


}
