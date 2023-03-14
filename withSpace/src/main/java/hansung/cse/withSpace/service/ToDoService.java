package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.ToDo;
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

    public Optional<ToDo> findToDo(Long todoId) {
        Optional<ToDo> findTodo = toDoRepository.findById(todoId);
        return findTodo;
    }

    @Transactional
    public Long updateDescription(Long id, String description) {
        Optional<ToDo> findToDo = toDoRepository.findById(id);
        findToDo.get().changeDescription(description);
        return findToDo.get().getId();
    }

    @Transactional
    public Long updateCompleted(Long id, Boolean completed) {
        Optional<ToDo> findToDo = toDoRepository.findById(id);
        findToDo.get().updateCompleted(completed);
        return findToDo.get().getId();
    }

    @Transactional
    public void deleteToDo(Long id) {
        toDoRepository.deleteById(id);
    }
}
