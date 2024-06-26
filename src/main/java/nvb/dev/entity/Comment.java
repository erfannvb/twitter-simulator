package nvb.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nvb.dev.base.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment extends BaseEntity<Long> {

    private String message;

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment")
    private Set<Like> likeSet = new HashSet<>();

    public Comment(String message, Tweet tweet, User user) {
        this.message = message;
        this.tweet = tweet;
        this.user = user;
    }

    public Comment(String message, Tweet tweet) {
        this.message = message;
        this.tweet = tweet;
    }
}
