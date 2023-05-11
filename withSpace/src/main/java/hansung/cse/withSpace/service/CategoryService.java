package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.exception.category.CategoryActiveException;
import hansung.cse.withSpace.exception.category.CategoryNotFoundException;
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
    public String makeDailyEasy(Long categoryId, CategoryDailyEasyDto dailyDto) { //매일반복 투두 생성

        Category category = findCategory(categoryId);

        LocalDateTime startDay = dailyDto.getStartDay();
        LocalDateTime endDay = dailyDto.getEndDay();
        LocalDateTime currentDateTime = startDay;

        UUID uuid = UUID.randomUUID();

        // 첫 번째 ToDo 객체 생성
//        ToDo todo = new ToDo(category, dailyDto.getDescription(),
//                false, currentDateTime, false, uuid);
//        todo.changeDate(startDay, endDay);

        EasyToDo easyToDo = new EasyToDo(category, dailyDto.getDescription(),
                false, currentDateTime, false, uuid);
        easyToDo.changeDate(dailyDto.getStartDay(), dailyDto.getEndDay());
        category.getEasyToDoList().add(easyToDo);
        easyToDoRepository.save(easyToDo);


        while (!currentDateTime.isAfter(endDay)) {
            ToDo todo = new ToDo(category, dailyDto.getDescription(),
                    false, currentDateTime, false, uuid);
            toDoRepository.save(todo);
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }

//        category.getEasyToDo().add(uuid); //카테고리에 간편입력 uuid추가

        return uuid.toString();
    }
    @Transactional
    public String makeWeeklyEasy(Long categoryId, CategoryWeeklyEasyDto weeklyDto) {
        Category category = findCategory(categoryId);

        LocalDateTime startDay = weeklyDto.getStartDay();
        LocalDateTime endDay = weeklyDto.getEndDay();
        DayOfWeek[] weeks = weeklyDto.getWeek();

        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = startDay;

        // 첫 번째 ToDo 객체 생성
//        if (Arrays.asList(weeks).contains(currentDateTime.getDayOfWeek())) {
//            ToDo todo = new ToDo(category, weeklyDto.getDescription(),
//                    false, currentDateTime, false, uuid);
//            todo.changeDate(startDay, endDay);
//            category.getEasyToDoList().add(todo);
//            toDoRepository.save(todo);
//        }
//
//        // 현재 시간을 다음 날로 이동
//        currentDateTime = currentDateTime.plusDays(1);

        EasyToDo easyToDo = new EasyToDo(category, weeklyDto.getDescription(),
                false, currentDateTime, false, uuid);
        easyToDo.changeDate(weeklyDto.getStartDay(), weeklyDto.getEndDay());
        category.getEasyToDoList().add(easyToDo);
        easyToDoRepository.save(easyToDo);

        while (!currentDateTime.isAfter(endDay)) {
            if (Arrays.asList(weeks).contains(currentDateTime.getDayOfWeek())) {
                ToDo todo = new ToDo(category, weeklyDto.getDescription(),
                        false, currentDateTime, false, uuid);
                toDoRepository.save(todo);
            }
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }

//        category.getEasyToDo().add(uuid); //카테고리에 간편입력 uuid추가

        return uuid.toString();
    }

    @Transactional
    public String makeMonthlyDtoEasy(Long categoryId, CategoryMonthlyEasyDto monthlyDto) {

        Category category = findCategory(categoryId);

        LocalDateTime startDay = monthlyDto.getStartDay();
        LocalDateTime endDay = monthlyDto.getEndDay();
        Integer[] days = monthlyDto.getDay();
        boolean isLastDay = monthlyDto.isLastDay();

        List<Integer> daysList = Arrays.asList(days);
        LocalDateTime currentDateTime = startDay;

        UUID uuid = UUID.randomUUID();

        // 첫 번째 ToDo 객체 생성
//        if (daysList.contains(currentDateTime.getDayOfMonth())) {
//            ToDo todo = new ToDo(category, monthlyDto.getDescription(),
//                    false, currentDateTime, false, uuid);
//            todo.changeDate(startDay, endDay);
//            category.getEasyToDoList().add(todo);
//            toDoRepository.save(todo);
//        }
//        if (isLastDay && currentDateTime.getDayOfMonth() == currentDateTime.toLocalDate().lengthOfMonth()) {
//            ToDo todo = new ToDo(category, monthlyDto.getDescription(),
//                    false, currentDateTime, false, uuid);
//            todo.changeDate(startDay, endDay);
//            category.getEasyToDoList().add(todo);
//            toDoRepository.save(todo);
//        }


        EasyToDo easyToDo = new EasyToDo(category, monthlyDto.getDescription(),
                false, currentDateTime, false, uuid);
        easyToDo.changeDate(monthlyDto.getStartDay(), monthlyDto.getEndDay());
        category.getEasyToDoList().add(easyToDo);
        easyToDoRepository.save(easyToDo);

        // 현재 시간을 다음 날로 이동
        //currentDateTime = currentDateTime.plusDays(1);

        while (!currentDateTime.isAfter(endDay)) {
            if (daysList.contains(currentDateTime.getDayOfMonth())) {
                ToDo todo = new ToDo(category, monthlyDto.getDescription(),
                        false, currentDateTime, false, uuid);
                toDoRepository.save(todo);
            }
            if (isLastDay && currentDateTime.getDayOfMonth() == currentDateTime.toLocalDate().lengthOfMonth()) {
                ToDo todo = new ToDo(category, monthlyDto.getDescription(),
                        false, currentDateTime, false, uuid);
                toDoRepository.save(todo);
            }
            currentDateTime = currentDateTime.plusDays(1); //다음 날로 이동
        }

//        category.getEasyToDo().add(uuid); //카테고리에 간편입력 uuid추가

        return uuid.toString();
    }
}
