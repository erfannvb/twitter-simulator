package nvb.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nvb.dev.base.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_tweets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tweet extends BaseEntity<Long> {

    @Max(value = 280, message = "tweet cannot be more than 280 characters")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "tweet")
    private Set<Like> likeSet = new HashSet<>();

    @OneToMany(mappedBy = "tweet")
    private Set<Comment> commentSet = new HashSet<>();

    public Tweet(String message, User user) {
        this.message = message;
        this.user = user;
    }
}
