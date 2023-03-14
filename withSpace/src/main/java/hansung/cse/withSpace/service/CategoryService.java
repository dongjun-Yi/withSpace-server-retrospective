package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.space.schedule.Category;
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

    public Optional<Category> findCategory(Long categoryId) {
        Optional<Category> findCategory = categoryRepository.findById(categoryId);
        return findCategory;
    }

    @Transactional
    public Long update(Long id, String title) {
        Optional<Category> findCategory = categoryRepository.findById(id);
        findCategory.get().changeTitle(title);
        return findCategory.get().getId();
    }

    @Transactional
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
