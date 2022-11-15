package moska.rebora.Common.Repository;

import moska.rebora.Common.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
