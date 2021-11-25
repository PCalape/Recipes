package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.model.UserLoginModel;

public interface UserRepository extends CrudRepository<UserLoginModel, String> {
}
