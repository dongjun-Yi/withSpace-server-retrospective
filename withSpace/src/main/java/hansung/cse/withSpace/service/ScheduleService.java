package hansung.cse.withSpace.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.schedule.*;
import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.exception.schedule.ScheduleNotFoundException;
import hansung.cse.withSpace.repository.ScheduleRepository;
import hansung.cse.withSpace.repository.ToDoRepository;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryDto;
import hansung.cse.withSpace.responsedto.schedule.easy.EasyCategory;
import hansung.cse.withSpace.responsedto.schedule.easy.EasyToDoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ToDoRepository toDoRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public Long makeSchedule(Space space) {
        Schedule schedule = new Schedule(space);
        Schedule saveSchedule = scheduleRepository.save(schedule);
        return saveSchedule.getId();
    }

    public Schedule findSchedule(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow(()
                -> new ScheduleNotFoundException("해당하는 스케줄이 존재하지 않습니다."));
        return findSchedule;
    }

    public List<EasyCategory> findEasyToDo(Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        List<EasyCategory> easyCategories = new ArrayList<>();
        for (Category category : schedule.getCategories()) {
            //List<EasyToDo> todos = category.getEasyToDoList();
            EasyCategory easyCategory = new EasyCategory(category);
            easyCategories.add(easyCategory);
        }
        return easyCategories;
    }

//    private ToDo findFirstByEasyMake(UUID uuid) {
//        QToDo toDo = QToDo.toDo;
//        ToDo findToDo = jpaQueryFactory
//                .selectFrom(toDo)
//                .where(toDo.easyMake.eq(uuid))
//                .limit(1)
//                .fetchOne();
//        assert findToDo != null;
//        System.out.println("findToDo.getStart() = " + findToDo.getStart());
//        return findToDo;
//    }

}
