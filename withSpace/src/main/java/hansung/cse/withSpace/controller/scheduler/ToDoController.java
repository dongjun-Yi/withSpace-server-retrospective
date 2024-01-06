package hansung.cse.withSpace.controller.scheduler;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryDailyEasyDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryMonthlyEasyDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryWeeklyEasyDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ChangeToDoRequestDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoDescriptionUpdateDto;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoRequestDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryBasicResponse;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoBasicResponse;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoEasyResponse;
import hansung.cse.withSpace.service.CategoryService;
import hansung.cse.withSpace.service.ToDoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private static final int SUCCESS_CODE = 200;
    private static final int CREATED = 201;

    private final CategoryService categoryService;
    private final ToDoService toDoService;

    @PostMapping("/category/{categoryId}/todo") //투두 생성 (카테고리에서 일반생성)
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoBasicResponse> createToDo(@PathVariable("categoryId") Long categoryId,
                                                        @RequestBody ToDoRequestDto toDoRequestDto,
                                                        HttpServletRequest request) {
        Category findCategory = categoryService.findCategory(categoryId);
        ToDo todo = new ToDo(findCategory, toDoRequestDto.getDescription(),
                toDoRequestDto.getCompleted(), LocalDateTime.now(), true, null);
        // 카테고리에서 직접 투두 생성시 active는 무조건 true, UUID는 필요없음

        Long saveToDoId = toDoService.makeTodo(todo);
        ToDoBasicResponse createResponseDto = new ToDoBasicResponse(saveToDoId, CREATED, "할일이 등록되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/todo/{todoId}/active") //간편으로 입력된 투두 활성화
    @PreAuthorize("@jwtAuthenticationFilter.isToDoOwner(#request, #todoId)")
    public ResponseEntity<ToDoBasicResponse> activeEasyTodo(@PathVariable("todoId") Long todoId,
                                                            HttpServletRequest request) {
        Long activeToDoId = toDoService.activeToDo(todoId);

        ToDoBasicResponse createResponseDto = new ToDoBasicResponse(activeToDoId, SUCCESS_CODE, "투두가 활성화 되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.OK);

    }

    @PatchMapping("/todo/{todoId}/today") // 오늘하기
    @PreAuthorize("@jwtAuthenticationFilter.isToDoOwner(#request, #todoId)")
    public ResponseEntity<ToDoBasicResponse> changeToday(@PathVariable("todoId") Long todoId,
                                                         HttpServletRequest request) {
        Long activeToDoId = toDoService.changeToday(todoId);

        ToDoBasicResponse createResponseDto =
                new ToDoBasicResponse(activeToDoId, SUCCESS_CODE, "날짜가 오늘로 변경되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.OK);

    }

    @PatchMapping("/todo/{todoId}/time") // 날짜 바꾸기
    @PreAuthorize("@jwtAuthenticationFilter.isToDoOwner(#request, #todoId)")
    public ResponseEntity<ToDoBasicResponse> changeToDoDate(@PathVariable("todoId") Long todoId,
                                                            @RequestBody ChangeToDoRequestDto toDoRequestDto,
                                                            HttpServletRequest request) {
        Long activeToDoId = toDoService.toDoServicechangeToDoDate(todoId, toDoRequestDto);

        ToDoBasicResponse createResponseDto =
                new ToDoBasicResponse(activeToDoId, SUCCESS_CODE, "요청 날짜로 변경되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}/todo/daily") // 간편입력 - 매일반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> makeDailyEasy(@PathVariable("categoryId") Long categoryId,
                                                          HttpServletRequest request,
                                                          @Valid @RequestBody CategoryDailyEasyDto dailyDto) {
        Long easyToDoId = categoryService.makeDailyEasy(categoryId, dailyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyToDoId, CREATED,
                "할일 간편입력이 완료되었습니다. - 매일 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}/todo/weekly") // 간편입력 - 매주반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> makeWeeklyEasy(@PathVariable("categoryId") Long categoryId,
                                                           HttpServletRequest request,
                                                           @Valid @RequestBody CategoryWeeklyEasyDto weeklyDto) {
        Long easyToDoId = categoryService.makeWeeklyEasy(categoryId, weeklyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyToDoId, CREATED,
                "할일 간편입력이 완료되었습니다. - 매주 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}/todo/monthly") // 간편입력 - 매월반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> makeMonthlyEasy(@PathVariable("categoryId") Long categoryId,
                                                            HttpServletRequest request,
                                                            @Valid @RequestBody CategoryMonthlyEasyDto monthlyDto) {
        Long easyToDoId = categoryService.makeMonthlyDtoEasy(categoryId, monthlyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyToDoId, CREATED,
                "할일 간편입력이 완료되었습니다. - 매월 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------

    @PatchMapping("/category/{categoryId}/todo/{easyToDoId}/daily") // 간편입력 수정 - 매일반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> modifyDailyEasy(@PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("easyToDoId") Long easyToDoId,
                                                            HttpServletRequest request,
                                                            @Valid @RequestBody CategoryDailyEasyDto dailyDto) {
        Long easyId = categoryService.modifyDailyEasy(easyToDoId, dailyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyId, SUCCESS_CODE,
                "할일 간편입력이 수정되었습니다. - 매일 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    @PatchMapping("/category/{categoryId}/todo/{easyToDoId}/weekly") // 간편입력 수정 - 매주반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> modifyWeeklyEasy(@PathVariable("categoryId") Long categoryId,
                                                             @PathVariable("easyToDoId") Long easyToDoId,
                                                             HttpServletRequest request,
                                                             @Valid @RequestBody CategoryWeeklyEasyDto weeklyDto) {
        Long easyId = categoryService.modifyWeeklyEasy(easyToDoId, weeklyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyId, SUCCESS_CODE,
                "할일 간편입력이 수정되었습니다. - 매주 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    @PatchMapping("/category/{categoryId}/todo/{easyToDoId}/monthly") // 간편입력 수정 - 매월반복
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<ToDoEasyResponse> modifyMonthlyEasy(@PathVariable("categoryId") Long categoryId,
                                                              @PathVariable("easyToDoId") Long easyToDoId,
                                                              HttpServletRequest request,
                                                              @Valid @RequestBody CategoryMonthlyEasyDto monthlyDto) {
        Long easyId = categoryService.modifyMonthlyDtoEasy(easyToDoId, monthlyDto);
        ToDoEasyResponse toDoEasyResponse = new ToDoEasyResponse(easyId, SUCCESS_CODE,
                "할일 간편입력이 수정되었습니다. - 매월 반복");
        return new ResponseEntity<>(toDoEasyResponse, HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------

    @DeleteMapping("/category/{categoryId}") //카테고리 삭제
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<CategoryBasicResponse> deleteCategory(@PathVariable("categoryId") Long categoryId,
                                                                HttpServletRequest request) {
        categoryService.delete(categoryId);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(SUCCESS_CODE,
                "카테고리가 삭제되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}/todo/{easyToDoId}") // 간편입력 해제
    @PreAuthorize("@jwtAuthenticationFilter.isCategoryOwner(#request, #categoryId)")
    public ResponseEntity<CategoryBasicResponse> deleteEasy(@PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("easyToDoId") Long easyToDoId,
                                                            HttpServletRequest request) {
        EasyToDo easyToDo = categoryService.findEasyToDoById(easyToDoId);
        categoryService.deleteInactiveToDos(easyToDo.getEasyMake()); //엮여있는 투두들 삭제
        categoryService.deleteEasyToDo(easyToDo);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(SUCCESS_CODE,
                "간편입력이 삭제되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @PatchMapping("/todo/{todoId}") //투두 수정
    @PreAuthorize("@jwtAuthenticationFilter.isToDoOwner(#request,#todoId)")
    public ResponseEntity<ToDoBasicResponse> updateToDoDescription(@PathVariable("todoId") Long todoId,
                                                                   @RequestBody ToDoDescriptionUpdateDto toDoDescriptionUpdateDto,
                                                                   HttpServletRequest request) {
        Long updateToDoId = toDoService.updateToDo(todoId, toDoDescriptionUpdateDto.getDescription(), toDoDescriptionUpdateDto.getCompleted());
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(updateToDoId, SUCCESS_CODE, "할일이 수정되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/todo/{todoId}") //투두 삭제
    @PreAuthorize("@jwtAuthenticationFilter.isToDoOwner(#request,#todoId)")
    public ResponseEntity<ToDoBasicResponse> deleteToDo(@PathVariable("todoId") Long todoId,
                                                        HttpServletRequest request) {
        toDoService.deleteToDo(todoId);
        ToDoBasicResponse toDoBasicResponse = new ToDoBasicResponse(SUCCESS_CODE, "할일이 삭제되었습니다.");
        return new ResponseEntity<>(toDoBasicResponse, HttpStatus.OK);
    }
}
