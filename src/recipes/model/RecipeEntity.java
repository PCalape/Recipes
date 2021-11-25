package recipes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @NotBlank
    @JsonProperty("category")
    @Column(name = "category")
    private String category;

    @CreationTimestamp
    private LocalDateTime date;

    @NotBlank
    @JsonProperty("description")
    @Column(name = "description")
    private String description;

    @Size(min = 1)
    @NotEmpty
    @JsonProperty("ingredients")
    @Column(name = "ingredients")
    private String[] ingredients;

    @Size(min = 1)
    @NotEmpty
    @JsonProperty("directions")
    @Column(name = "directions")
    private String[] directions;

    private String email;
}
