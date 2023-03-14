package hansung.cse.withSpace.responsedto.space.page.block;

import hansung.cse.withSpace.domain.space.Block;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateBlockResponse {
    private Long blockId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String content;

    public UpdateBlockResponse(Block block) {
        this.updatedAt = block.getUpdatedAt();
        this.content = block.getContent();
    }
}
