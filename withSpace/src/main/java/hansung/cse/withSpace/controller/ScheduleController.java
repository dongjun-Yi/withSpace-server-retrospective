package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryRequestDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryUpdateDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoCompletedUpdateDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoDescriptionUpdateDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.schedule.ScheduleDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryBasicResponse;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoBasicResponse;
import hansung.cse.withSpace.service.CategoryService;
import hansung.cse.withSpace.service.ScheduleService;
import hansung.cse.withSpace.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private static final int SUCCESS = 200;

    private static final int CREATED = 201;

    private final ScheduleService scheduleService;

    private final CategoryService categoryService;

    private final ToDoService toDoService;

    @GetMapping("/schedule/{scheduleId}")
    @PreAuthorize("@customSecurityUtil.isScheduleOwner(#scheduleId)")
    public ResponseEntity<BasicResponse> schedule(@PathVariable("scheduleId") Long scheduleId) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        List<ScheduleDto> collect = Collections.singletonList(new ScheduleDto(schedule));
        BasicResponse basicResponse = new BasicResponse<>(collect.size(), "스케줄 요청 성공", collect.get(0));
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    /**
     * 카테고리 생성
     */
    @PostMapping("/schedule/{scheduleId}/category")
    @PreAuthorize("@customSecurityUtil.isScheduleOwner(#scheduleId)")
    public ResponseEntity<CategoryBasicResponse> createCategory(@PathVariable("scheduleId") Long scheduleId, @RequestBody CategoryRequestDto categoryRequestDto) {
        Schedule schedule = scheduleService.findSchedule(scheduleId);

        Category category = new Category(schedule, categoryRequestDto.getTitle());

        Long saveCategoryId = categoryService.makeCategory(category);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(saveCategoryId, CREATED, "카테고리가 등록되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/category/{categoryId}")
    @PreAuthorize("@customSecurityUtil.isCategoryOwner(#categoryId)")
    public ResponseEntity<CategoryBasicResponse> changeCategoryTitle(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Long updateCategoryId = categoryService.update(categoryId, categoryUpdateDto.getTitle());
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(updateCategoryId, SUCCESS, "카테고리 제목이 수정되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    @PreAuthorize("@customSecurityUtil.isCategoryOwner(#categoryId)")
    public ResponseEntity<CategoryBasicResponse> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(SUCCESS, "카테고리가 삭제되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}/todo")
    @PreAuthorize("@customSecurityUtil.isToDoOwner(#categoryId)")
    public ResponseEntity<ToDoBasicResponse> createToDo(@PathVariable("categoryId") Long categoryId, @RequestBody ToDoRequestDto toDoRequestDto) {
        Category findCategory = categoryService.findCategory(categoryId);
        ToDo todo = new ToDo(findCategory, toDoRequestDto.getDescription(), toDoRequestDto.getCompleted(), LocalDateTime.now());

        Long saveToDoId = toDoService.makeTodo(todo);
        ToDoBasicResponse createResponseDto = new ToDoBasicResponse(saveToDoId, CREATED, "할일이 등록되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/todo/{todoId}")
    @PreAuthorize("@customSecurityUtil.isToDoOwner(#todoId)")
    public ResponseEntity<ToDoBasicResponse> updateToDoDescription(@PathVariable("todoId") Long todoId, @RequestBody ToDoDescriptionUpdateDto toDoDescriptionUpdateDto) {
        Long updateToDoId = toDoService.updateToDo(todoId, toDoDescriptionUpdateDto.getDescription(), toDoDescriptionUpdateDto.getCompleted());
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(updateToDoId, SUCCESS, "할일이 수정되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/todo/{todoId}")
    @PreAuthorize("@customSecurityUtil.isToDoOwner(#todoId)")
    public ResponseEntity<ToDoBasicResponse> deleteToDo(@PathVariable("todoId") Long todoId) {
        toDoService.deleteToDo(todoId);
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(SUCCESS, "할일이 삭제되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }
}
