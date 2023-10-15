package nvb.dev.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import nvb.dev.base.entity.BaseEntity;

@Entity
@Table(name = "tbl_likes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Like extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

}