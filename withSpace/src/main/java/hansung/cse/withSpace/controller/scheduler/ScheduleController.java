package hansung.cse.withSpace.controller.scheduler;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.schedule.ScheduleCategoryDto;
import hansung.cse.withSpace.responsedto.schedule.ScheduleDto;
import hansung.cse.withSpace.responsedto.schedule.ScheduleFriendDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryBasicResponse;
import hansung.cse.withSpace.responsedto.schedule.easy.EasyCategory;
import hansung.cse.withSpace.service.CategoryService;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private static final int CREATED = 201;
    private final MemberService memberService;

    private final ScheduleService scheduleService;

    private final CategoryService categoryService;

    @GetMapping("/schedule/{scheduleId}") //스케줄 조회
    public ResponseEntity<BasicResponse> schedule(@PathVariable("scheduleId") Long scheduleId,
                                                  HttpServletRequest request) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        List<ScheduleDto> collect = Collections.singletonList(new ScheduleDto(schedule));
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "스케줄 요청 성공", collect.get(0));
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/friend/{friendId}/schedule") //친구 스케줄 조회
    public ResponseEntity<BasicResponse> getFriendSchedule(@PathVariable("friendId") Long friendId, HttpServletRequest request) {
        Member friend = memberService.findOne(friendId);
        Schedule friendSchedule = friend.getMemberSpace().getSchedule();
        List<ScheduleFriendDto> collect = Collections.singletonList(new ScheduleFriendDto(friendSchedule));
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "스케줄 요청 성공", collect.get(0));
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/schedule/{scheduleId}/categories") //카테고리 조회
    public ResponseEntity<BasicResponse> categories(@PathVariable("scheduleId") Long scheduleId,
                                                    HttpServletRequest request) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        List<ScheduleCategoryDto> collect = Collections.singletonList(new ScheduleCategoryDto(schedule));
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "카테고리들 요청 성공", collect.get(0));
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/schedule/{scheduleId}/easytodo") //간편입력 조회
    public ResponseEntity<List<EasyCategory>> easyToDo(@PathVariable("scheduleId") Long scheduleId,
                                                       HttpServletRequest request) {
        List<EasyCategory> easyToDo = scheduleService.findEasyToDo(scheduleId);

        return new ResponseEntity<>(easyToDo, HttpStatus.OK);
    }

    /**
     * 카테고리 생성
     */
    @PostMapping("/schedule/{scheduleId}/category")
    public ResponseEntity<CategoryBasicResponse> createCategory(@PathVariable("scheduleId") Long scheduleId,
                                                                @RequestBody CategoryRequestDto categoryRequestDto,
                                                                HttpServletRequest request) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);

        Category category = new Category(schedule, categoryRequestDto);

        Long saveCategoryId = categoryService.makeCategory(category);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(saveCategoryId, CREATED, "카테고리가 등록되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.CREATED);
    }
}
