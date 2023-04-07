package hansung.cse.withSpace.requestdto.chat;

import lombok.Data;

@Data
public class CreateMessageRequestDto {
    private Long senderId;
    private String content;
}
