package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.exception.category.CategoryNotFoundException;
import hansung.cse.withSpace.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long makeCategory(Category category) {
        Category save = categoryRepository.save(category);
        return save.getId();
    }

    public Category findCategory(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(()
                -> new CategoryNotFoundException("해당하는 카테고리가 존재하지 않습니다."));
        return findCategory;
    }

    @Transactional
    public Long update(Long id, String title) {
        Category findCategory = findCategory(id);
        findCategory.changeTitle(title);
        return findCategory.getId();
    }

    @Transactional
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
