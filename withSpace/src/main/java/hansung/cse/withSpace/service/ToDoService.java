package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.exception.schedule.ScheduleNotFoundException;
import hansung.cse.withSpace.exception.todo.ToDoNotFoundException;
import hansung.cse.withSpace.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Transactional
    public Long makeTodo(ToDo todo) {
        ToDo saveToDo = toDoRepository.save(todo);
        return saveToDo.getId();
    }

    public ToDo findToDo(Long todoId) {
        ToDo findToDo = toDoRepository.findById(todoId).orElseThrow(()
                -> new ToDoNotFoundException("해당하는 투두가 존재하지 않습니다."));
        return findToDo;
    }

    @Transactional
    public Long updateToDo(Long id, String description, Boolean completed) {
        ToDo findToDo = findToDo(id);
        findToDo.changeDescription(description);
        findToDo.updateCompleted(completed);
        return findToDo.getId();
    }
    @Transactional
    public Long activeToDo(Long todoId) {
        ToDo toDo = findToDo(todoId);
        toDo.changeActive();
        return todoId;
    }
    @Transactional
    public void deleteToDo(Long id) {
        toDoRepository.deleteById(id);
    }


}
