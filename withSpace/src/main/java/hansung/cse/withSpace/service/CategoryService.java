package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.EasyType;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.exception.category.CategoryActiveException;
import hansung.cse.withSpace.exception.category.CategoryNotFoundException;
import hansung.cse.withSpace.exception.todo.ToDoNotFoundException;
import hansung.cse.withSpace.repository.CategoryRepository;
import hansung.cse.withSpace.repository.EasyToDoRepository;
import hansung.cse.withSpace.repository.ToDoRepository;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryDailyEasyDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryInactiveDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryMonthlyEasyDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryUpdateDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryWeeklyEasyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ToDoRepository toDoRepository;
    private final EasyToDoRepository easyToDoRepository;

    @Transactional
    public Long makeCategory(Category category) {
        Category save = categoryRepository.save(category);
        return save.getId();
    }

    public Category findCategory(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(()
                -> new CategoryNotFoundException("해당하는 카테고리가 존재하지 않습니다."));
        return findCategory;
    }

    @Transactional
    public Long update(Long id, CategoryUpdateDto categoryUpdateDto) {
        Category findCategory = findCategory(id);

        Optional.ofNullable(categoryUpdateDto.getTitle())
                .ifPresent(findCategory::changeTitle);
        Optional.ofNullable(categoryUpdateDto.getPublicSetting())
                .ifPresent(findCategory::changePublicSetting);
        Optional.ofNullable(categoryUpdateDto.getColor())
                .ifPresent(findCategory::changeColor);

        return findCategory.getId();
    }

    @Transactional
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Long inActiveCategory(Long categoryId, CategoryInactiveDto categoryInactiveDto) { //카테고리 비활성화
        Category category = findCategory(categoryId);
        if (category.isEnd()) {
            throw new CategoryActiveException("이미 비활성화중인 카테고리입니다.");
        }
        category.changeToInActive(categoryInactiveDto);
        return categoryId;
    }

    public Long activeCategory(Long categoryId) { //카테고리 활성화
        Category category = findCategory(categoryId);
        if (!category.isEnd()) {
            throw new CategoryActiveException("이미 활성화중인 카테고리입니다.");
        }
        category.changeToActive();
        return categoryId;
    }

//    public LocalDateTime changeLocalDateTime(Date date) {
//        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//    }

    @Transactional
    public Long makeDailyEasy(Long categoryId, CategoryDailyEasyDto dailyDto) { //매일반복 투두 생성

        Category category = findCategory(categoryId);

        LocalDateTime startDay = dailyDto.getStartDay();
        LocalDateTime endDay = dailyDto.getEndDay();
        LocalDateTime currentDateTime = startDay;

        UUID uuid = UUID.randomUUID();


        //조회용 따로 저장
        EasyToDo easyToDo = new EasyToDo(category, dailyDto.getDescription(), dailyDto.getStartDay(),
                dailyDto.getEndDay(), uuid, EasyType.DAILY);
        //category.getEasyToDoList().add(easyToDo);
        easyToDoRepository.save(easyToDo);


        while (!currentDateTime.isAfter(endDay)) {
            ToDo todo = new ToDo(category, dailyDto.getDescription(),
                    false, currentDateTime, false, uuid);
            todo.changeDate(startDay, endDay);
            toDoRepository.save(todo);
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }


        return easyToDo.getId();
    }

    @Transactional
    public Long makeWeeklyEasy(Long categoryId, CategoryWeeklyEasyDto weeklyDto) {
        Category category = findCategory(categoryId);

        LocalDateTime startDay = weeklyDto.getStartDay();
        LocalDateTime endDay = weeklyDto.getEndDay();
        DayOfWeek[] weeks = weeklyDto.getWeek();

        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = startDay;

        //조회용 따로 저장
        EasyToDo easyToDo = new EasyToDo(category, weeklyDto.getDescription(), weeklyDto.getStartDay(),
                weeklyDto.getEndDay(), uuid, EasyType.WEEKLY);
        easyToDo.changeWeek(weeklyDto);
        easyToDoRepository.save(easyToDo);


        while (!currentDateTime.isAfter(endDay)) {
            if (Arrays.asList(weeks).contains(currentDateTime.getDayOfWeek())) {
                ToDo todo = new ToDo(category, weeklyDto.getDescription(),
                        false, currentDateTime, false, uuid);
                todo.changeDate(startDay, endDay);
                toDoRepository.save(todo);
            }
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }


        return easyToDo.getId();
    }

    //-------------------------------------------------------------------------------

    public boolean isWithinDateRange(LocalDateTime targetDate, LocalDateTime startDay, LocalDateTime endDay) {
        return !targetDate.isBefore(startDay) && !targetDate.isAfter(endDay);
    }

    public EasyToDo findEasyToDoById(Long easyToDoId) {
        return easyToDoRepository.findById(easyToDoId).orElseThrow(()
                -> new ToDoNotFoundException("해당하는 간편입력 ToDo가 존재하지 않습니다."));
    }

    public boolean isChangedTodoExist(List<ToDo> toDoList, LocalDateTime targetDate) {
        for (ToDo todo : toDoList) {
            if (todo.getDate().equals(targetDate)) {
                return true;
            }
        }
        return false;
    }


    @Transactional
    public void createNewToDosForDay(EasyToDo easyToDo, CategoryDailyEasyDto dailyDto) {
        LocalDateTime currentDateTime = dailyDto.getStartDay();

        while (!currentDateTime.isAfter(dailyDto.getEndDay())) {
            ToDo todo = new ToDo(easyToDo.getCategory(), dailyDto.getDescription(),
                    false, currentDateTime, false, easyToDo.getEasyMake());
            toDoRepository.save(todo);
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }
    }

    @Transactional
    public void createNewToDosForWeek(EasyToDo easyToDo, CategoryWeeklyEasyDto weeklyDto) {
        Category category = easyToDo.getCategory();
        UUID uuid = easyToDo.getEasyMake();
        DayOfWeek[] weeks = weeklyDto.getWeek();
        LocalDateTime currentDateTime = weeklyDto.getStartDay();

        while (!currentDateTime.isAfter(weeklyDto.getEndDay())) {
            if (Arrays.asList(weeks).contains(currentDateTime.getDayOfWeek())) {
                ToDo todo = new ToDo(category, weeklyDto.getDescription(),
                        false, currentDateTime, false, uuid);
                toDoRepository.save(todo);
            }
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }
    }

    @Transactional
    public void updateToDos(List<ToDo> toDoList, String description, LocalDateTime startDay, LocalDateTime endDay) {
        for (ToDo todo : toDoList) {
            if (!todo.isActive() && isWithinDateRange(todo.getDate(), startDay, endDay)) {
                todo.changeDescription(description);
                toDoRepository.save(todo);
            } else if (!todo.isActive()) {
                toDoRepository.delete(todo);
            }
        }
    }

    @Transactional
    public void deleteInactiveToDos(UUID easyMake) {
        List<ToDo> toDoList = toDoRepository.findAllByEasyMake(easyMake);

        for (ToDo todo : toDoList) {
            if (!todo.isActive()) {
                toDoRepository.delete(todo);
            }
        }
    }


    @Transactional
    public Long modifyDailyEasy(Long easyToDoId, CategoryDailyEasyDto dailyDto) {

        EasyToDo easyToDo = findEasyToDoById(easyToDoId);
//        Category category = easyToDo.getCategory();
//        UUID uuid = easyToDo.getEasyMake();
//        LocalDateTime currentDateTime = dailyDto.getStartDay();

        //EasyToDo 내용 변경
        easyToDo.changeDailyPatch(dailyDto);
        easyToDoRepository.save(easyToDo);

        //기존 비활성화되어있는 투두 삭제
        deleteInactiveToDos(easyToDo.getEasyMake());

        //새로운 투두 생성
        createNewToDosForDay(easyToDo, dailyDto);

        return easyToDoId;
    }


    @Transactional
    public Long modifyWeeklyEasy(Long easyToDoId, CategoryWeeklyEasyDto weeklyDto) {
        EasyToDo easyToDo = findEasyToDoById(easyToDoId);

        //EasyToDo 내용 변경
        easyToDo.changeWeeklyPatch(weeklyDto);
        easyToDoRepository.save(easyToDo);

        //기존 비활성화되어있는 투두 삭제
        deleteInactiveToDos(easyToDo.getEasyMake());

        //새로운 투두 생성
        createNewToDosForWeek(easyToDo, weeklyDto);

        return easyToDoId;
    }

    @Transactional
    public Long modifyMonthlyDtoEasy(Long easyToDoId, CategoryMonthlyEasyDto monthlyDto) {
        EasyToDo easyToDo = findEasyToDoById(easyToDoId);

        //EasyToDo 내용 변경
        easyToDo.changeMonthlyPatch(monthlyDto);
        easyToDoRepository.save(easyToDo);

        //기존 비활성화되어있는 투두 삭제
        deleteInactiveToDos(easyToDo.getEasyMake());

        return easyToDoId;
    }

    @Transactional
    public void deleteEasyToDo(EasyToDo easyToDo) { //간편입력 EasyToDo 엔티티 삭제
        easyToDoRepository.delete(easyToDo);
    }
}
