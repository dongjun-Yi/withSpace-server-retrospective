package hansung.cse.withSpace.controller.scheduler;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.requestdto.schedule.todo.ToDoRequestDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryBasicResponse;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoBasicResponse;
import hansung.cse.withSpace.service.CategoryService;
import hansung.cse.withSpace.service.ToDoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private static final int SUCCESS = 200;
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

        ToDoBasicResponse createResponseDto = new ToDoBasicResponse(activeToDoId, CREATED, "투두가 활성화 되었습니다.");
        return new ResponseEntity<>(createResponseDto, HttpStatus.CREATED);

    }
}
