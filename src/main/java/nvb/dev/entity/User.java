package nvb.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import nvb.dev.base.entity.BaseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends BaseEntity<Long> {

    @NotNull(message = "firstName must not be null")
    @NotEmpty(message = "firstName must not be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "lastName must not be null")
    @NotEmpty(message = "lastName must not be empty")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "username must not be null")
    @NotEmpty(message = "username must not be empty")
    private String username;

    @NotNull(message = "password must not be null")
    @NotEmpty(message = "password must not be empty")
    private String password;

    @Positive(message = "age must be a positive value")
    @Min(value = 15, message = "age must be at least 15")
    @NotNull(message = "age must not be null")
    private int age;

    @NotNull(message = "birthDate must not be null")
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user")
    private Set<Tweet> tweetSet = new HashSet<>();

    public User(String firstName, String lastName, String username,
                String password, int age, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.age = age;
        this.birthDate = birthDate;
    }
}
