package recipes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginModel {
    @Id
    @Email(regexp = ".+@.+\\..+")
    @NotBlank
    @JsonProperty("email")
    @Column(name = "email")
    private String email;

    @Size(min = 8)
    @NotBlank
    @JsonProperty("password")
    @Column(name = "password")
    private String password;
}
