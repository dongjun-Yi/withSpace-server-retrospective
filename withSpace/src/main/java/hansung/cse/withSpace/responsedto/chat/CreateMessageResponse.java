package hansung.cse.withSpace.responsedto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageResponse {
    private Long messageId;
    private Integer status;
    private String message;
}
