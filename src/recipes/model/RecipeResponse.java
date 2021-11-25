package recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private String name;
    private String category;
    private LocalDateTime date;
    private String description;
    private String[] ingredients;
    private String[] directions;
}
