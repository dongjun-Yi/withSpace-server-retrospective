package hansung.cse.withSpace.requestdto.schedule.category;


import hansung.cse.withSpace.domain.space.schedule.PublicSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDto {
    private String title;
    private PublicSetting publicSetting;
    private String color;
}
