package hansung.cse.withSpace.controller.scheduler;

import hansung.cse.withSpace.requestdto.schedule.category.CategoryInactiveDto;
import hansung.cse.withSpace.requestdto.schedule.category.CategoryUpdateDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryBasicResponse;
import hansung.cse.withSpace.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private static final int SUCCESS_CODE = 200;
    private final CategoryService categoryService;

    @PatchMapping("/category/{categoryId}") //카테고리 수정
    public ResponseEntity<CategoryBasicResponse> changeCategoryTitle(@PathVariable("categoryId") Long categoryId,
                                                                     @RequestBody CategoryUpdateDto categoryUpdateDto,
                                                                     HttpServletRequest request) {
        Long updateCategoryId = categoryService.update(categoryId, categoryUpdateDto);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(updateCategoryId, SUCCESS_CODE,
                "카테고리가 수정되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------

    @PatchMapping("/category/{categoryId}/inactive") //카테고리 비활성화
    public ResponseEntity<CategoryBasicResponse> inActiveCategory(@PathVariable("categoryId") Long categoryId,
                                                                  @Valid @RequestBody CategoryInactiveDto categoryInactiveDto,
                                                                  HttpServletRequest request) {
        Long updateCategoryId = categoryService.inActiveCategory(categoryId, categoryInactiveDto);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(updateCategoryId, SUCCESS_CODE,
                "카테고리가 비활성화 되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }

    @PatchMapping("/category/{categoryId}/active") //카테고리 활성화
    public ResponseEntity<CategoryBasicResponse> activeCategory(@PathVariable("categoryId") Long categoryId,
                                                                HttpServletRequest request) {
        Long updateCategoryId = categoryService.activeCategory(categoryId);
        CategoryBasicResponse categoryBasicResponse = new CategoryBasicResponse(updateCategoryId, SUCCESS_CODE,
                "카테고리가 활성화 되었습니다.");
        return new ResponseEntity<>(categoryBasicResponse, HttpStatus.OK);
    }
}