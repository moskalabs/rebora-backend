package moska.rebora.Common.Repository;

import moska.rebora.Common.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getCategoryByCategoryName(String categoryName);

    List<Category> getCategoriesByCategoryNameIn(String[] categoryName);
}
