package hansung.cse.withSpace.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.schedule.QToDo;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.exception.schedule.ScheduleNotFoundException;
import hansung.cse.withSpace.repository.ScheduleRepository;
import hansung.cse.withSpace.repository.ToDoRepository;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryDto;
import hansung.cse.withSpace.responsedto.schedule.easy.EasyCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        return schedule.getCategories().stream()
                .map(category -> {
                    ToDo todo = null;
                    if(!category.getEasyToDo().isEmpty()){
                        UUID firstToDoUuid = category.getEasyToDo().get(0);
                        todo = findFirstByEasyMake(firstToDoUuid);
                        // 해당 uuid를 가지고 있는 첫번째! 투두를 가져옴
                    }
                    return new EasyCategory(category, todo);
                })
                .collect(Collectors.toList());
    }
    private ToDo findFirstByEasyMake(UUID uuid) {
        QToDo toDo = QToDo.toDo;
        return jpaQueryFactory
                .selectFrom(toDo)
                .where(toDo.easyMake.eq(uuid))
                .limit(1)
                .fetchOne();
    }

}
