package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.exception.schedule.ScheduleNotFoundException;
import hansung.cse.withSpace.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long makeSchedule(Schedule schedule) {
        Schedule saveSchedule = scheduleRepository.save(schedule);
        return saveSchedule.getId();
    }

    public Schedule findSchedule(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow(()
                -> new ScheduleNotFoundException("해당하는 스케줄이 존재하지 않습니다."));
        return findSchedule;
    }

}
