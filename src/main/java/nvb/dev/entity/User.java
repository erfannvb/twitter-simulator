package nvb.dev.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private int age;

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
