package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.exception.chat.RoomNotFoundException;
import hansung.cse.withSpace.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional
    public Long makeMessage(Member sender, Room room, String content) {
        Message message = new Message(sender, room, content);
        messageRepository.save(message);
        return sender.getId();
    }


//    public Message findOne(Long messageId) {
//        return messageRepository.findById(messageId)
//                .orElseThrow(() -> new MessageNotFoundException("메세지를 찾을 수 없습니다."));
//    }
}
