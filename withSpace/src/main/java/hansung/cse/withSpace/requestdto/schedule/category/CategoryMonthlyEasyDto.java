package hansung.cse.withSpace.requestdto.schedule.category;

import hansung.cse.withSpace.validation.schedule.ValidDayOfMonth;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMonthlyEasyDto {
    @NotNull(message = "제목이 누락되었습니다.")
    private String description;
    @NotNull(message = "시작 날짜가 누락되었습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDay;
    @NotNull(message = "종료 날짜가 누락되었습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDay;

    //@NotNull(message = "반복 날짜가 누락되었습니다.")
    @ValidDayOfMonth
    private Integer[] day;

    private boolean lastDay;
}
