package app.services.interfaces;

import app.entities.Category;
import app.enums.CategoryType;

import java.util.List;

public interface CategoryService {
    void save(Category category);
    List<Category> findAll();
    Category findByCategoryType(CategoryType categoryType);

}
