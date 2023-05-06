package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.TrashCan;
import hansung.cse.withSpace.exception.TrashCan.TrashCanNotFoundException;
import hansung.cse.withSpace.exception.space.SpaceNotFoundException;
import hansung.cse.withSpace.repository.TrashCanRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TrashCanService {
    private final TrashCanRepository trashCanRepository;

    public TrashCan findOne(Long TrashCanId) {
        return trashCanRepository.findById(TrashCanId).orElseThrow(()
                -> new TrashCanNotFoundException("해당하는 휴지통이 존재하지 않습니다."));
    }
    @Transactional
    public TrashCan makeTrashCan(Space space) {

        TrashCan trashCan = new TrashCan(space);
        trashCanRepository.save(trashCan);

        return trashCan;
    }
}
