package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.model.RecipeEntity;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
    List<RecipeEntity> findByCategoryIgnoreCase(String category);

    List<RecipeEntity> findByNameContainingIgnoreCase(String name);
}
