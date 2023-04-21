package app.services;

import app.entities.Category;
import app.enums.CategoryType;
import app.repositories.CategoryRepository;
import app.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category findByCategoryType(CategoryType categoryType) {
        return categoryRepository
                .findByCategoryType(categoryType)
                .orElseThrow(() -> new RuntimeException("Передан несуществующий CategoryType"));
    }

    @Override
    public List<Category> findAll() {

        return categoryRepository.findAll();
    }
}
