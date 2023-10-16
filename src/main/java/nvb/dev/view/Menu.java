package nvb.dev.view;

import jakarta.persistence.NoResultException;
import nvb.dev.base.repository.util.HibernateUtil;
import nvb.dev.entity.Comment;
import nvb.dev.entity.Like;
import nvb.dev.entity.Tweet;
import nvb.dev.entity.User;
import nvb.dev.repository.CommentRepository;
import nvb.dev.repository.LikeRepository;
import nvb.dev.repository.TweetRepository;
import nvb.dev.repository.UserRepository;
import nvb.dev.repository.impl.CommentRepositoryImpl;
import nvb.dev.repository.impl.LikeRepositoryImpl;
import nvb.dev.repository.impl.TweetRepositoryImpl;
import nvb.dev.repository.impl.UserRepositoryImpl;
import nvb.dev.service.CommentService;
import nvb.dev.service.LikeService;
import nvb.dev.service.TweetService;
import nvb.dev.service.UserService;
import nvb.dev.service.impl.CommentServiceImpl;
import nvb.dev.service.impl.LikeServiceImpl;
import nvb.dev.service.impl.TweetServiceImpl;
import nvb.dev.service.impl.UserServiceImpl;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Menu {

    private final Session session = HibernateUtil.getSessionFactory().openSession();

    private final CommentRepository commentRepository = new CommentRepositoryImpl(session);
    private final LikeRepository likeRepository = new LikeRepositoryImpl(session);
    private final TweetRepository tweetRepository = new TweetRepositoryImpl(session);
    private final UserRepository userRepository = new UserRepositoryImpl(session);

    private final CommentService commentService = new CommentServiceImpl(session, commentRepository);
    private final LikeService likeService = new LikeServiceImpl(session, likeRepository);
    private final TweetService tweetService = new TweetServiceImpl(session, tweetRepository);
    private final UserService userService = new UserServiceImpl(session, userRepository);

    private Set<Tweet> tweetSet = new HashSet<>();

    private Scanner input = new Scanner(System.in);

    public static User user = new User();

    public void showMainMenu() {
        System.out.println("-----Twitter-----");
        System.out.println();
        System.out.println("1) Signup");
        System.out.println("2) Login");
        System.out.println("3) Exit");
        switch (input.nextInt()) {
            case 1:
                signup();
                login();
                break;
            case 2:
                login();
                break;
            case 3:
                System.exit(0);
        }
    }

    public void signup() {
        System.out.println("Enter First Name : ");
        String firstName = input.next();
        System.out.println("Enter Last Name : ");
        String lastName = input.next();
        System.out.println("Enter Username : ");
        String username = input.next();
        System.out.println("Enter Password : ");
        String password = input.next();
        Tweet tweet = new Tweet();
        tweet.setMessage(username + " have joined twitter.");
        tweetSet.add(tweet);
        User newUser = new User(firstName, lastName, username, password);
        tweet.setUser(newUser);
        if (!userService.isValid(newUser)) {
            showMainMenu();
        } else {
            user = newUser;
        }
    }

    public void login() {
        System.out.println("-----Login-----");
        System.out.println("Enter Username : ");
        String username = input.next();
        System.out.println("Enter Password : ");
        String password = input.next();
        try {
            if (userService.findUserByUsername(username).isPresent() &&
                    userService.findByPassword(password).isPresent()) {
                user = userService.findByPassword(password).get();
                showHome();
            }
        } catch (NoResultException e) {
            System.out.println("Password is not correct.");
            showMainMenu();
        }
    }

    public void editProfile() {
        System.out.println("-----Edit Profile-----");
        System.out.println("Enter New First Name : ");
        String firstName = input.next();
        System.out.println("Enter New Last Name");
        String lastName = input.next();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (!userService.isValid(user)) {
            showMainMenu();
        } else {
            userService.update(user);
            System.out.println("User Profile Updated Successfully.");
        }
    }

    public void showProfile() {
        System.out.println("-----Profile-----");
        System.out.println("**********");
        System.out.println(user);
        System.out.println("**********");
        System.out.println("1) Edit Profile");
        System.out.println("2) Remove Account");
        System.out.println("3) Logout");
        switch (input.nextInt()) {
            case 1:
                editProfile();
                break;
            case 2:
                userService.remove(user);
                System.out.println("Accounted removed successfully.");
                user = null;
                showMainMenu();
                break;
            case 3:
                user = null;
                showMainMenu();
                break;
        }
    }

    public void postTweet() {
        System.out.println("Write your message...");
        String message = input.next();
        Set<Tweet> tweets = new HashSet<>();
        Set<Like> likeSet = new HashSet<>();
        Set<Comment> commentSet = new HashSet<>();
        Tweet tweet = new Tweet(message, user);
        tweet.setLikeSet(likeSet);
        tweet.setCommentSet(commentSet);
        tweets.add(tweet);
        user.setTweetSet(tweets);
        tweetService.save(tweet);
        userService.update(user);
        showHome();
    }

    public Comment postComment(Tweet tweet) {
        System.out.println("Write your comment...");
        String message = input.next();
        Set<Like> likeSet = new HashSet<>();
        Comment comment = new Comment(message, tweet);
        comment.setUser(user);
        comment.setLikeSet(likeSet);
        commentService.save(comment);
        return comment;
    }

    public String editMessage() {
        System.out.println("Write your message...");
        return input.next();
    }

    public String checkTweetLength() {
        return "a".repeat(270);
    }

    private void showHome() {
        while (true) {
            System.out.println("-----Home-----");
            Set<Tweet> tweets = new HashSet<>(tweetService.findAll());
            if (tweets.isEmpty()) {
                System.out.println("Do you want to write a tweet? Y or N : ");
                if (input.next().equals("y")) postTweet();
                else if (input.next().equals("n")) System.exit(0);
            } else {
                for (Tweet tweet : tweets) {
                    boolean menuFlag = true;
                    System.out.println("**********");
                    System.out.println("Tweet message : " + tweet.getMessage());
                    System.out.println("Tweet Writer : " + tweet.getUser());
                    System.out.println("Likes : " + tweet.getLikeSet().size());
                    tweet.getLikeSet().forEach(System.out::println);
                    System.out.println();
                    System.out.println("Number of comments : " + tweet.getCommentSet().size());
                    System.out.println("**********");
                    System.out.println("1) Like");
                    System.out.println("2) Dislike");
                    System.out.println("3) Show Comment");
                    System.out.println("4) Like Comment");
                    System.out.println("5) Dislike Comment");
                    System.out.println("6) Write Comment");
                    System.out.println("7) Edit Comment");
                    System.out.println("8) Remove Comment");
                    System.out.println("9) Write Tweet");
                    System.out.println("10) Edit Tweet");
                    System.out.println("11) Remove Tweet");
                    System.out.println("12) Search User");
                    System.out.println("13) Show Profile");
                    System.out.println("14) Tweet Length");
                    System.out.println("15) Next Tweet");
                    switch (input.nextInt()) {
                        case 1:
                            int flagLike = 0;
                            Set<Like> tempLikeList = new HashSet<>(tweet.getLikeSet());
                            for (Like like : tempLikeList) {
                                if (like.getUser().getUsername().equals(user.getUsername())) {
                                    flagLike++;
                                }
                            }
                            if (flagLike == 0) {
                                Like like = new Like(user.getUsername(), tweet);
                                like.setUser(user);
                                tempLikeList.add(like);
                                likeService.save(like);
                                tweet.setLikeSet(tempLikeList);
                                tweetService.update(tweet);
                            } else {
                                System.out.println("Cannot be liked again.");
                            }
                            System.out.println("Likes : " + tempLikeList.size());
                            break;
                        case 2:
                            boolean flagStatus = false;
                            Set<Like> temporaryListUnLike = new HashSet<>(tweet.getLikeSet());
                            for (Like unlike : temporaryListUnLike
                            ) {
                                if (unlike.getUser().getUsername().equals(user.getUsername())) {
                                    temporaryListUnLike.remove(unlike);
                                    likeService.remove(unlike);
                                    break;
                                } else flagStatus = true;
                            }
                            if (flagStatus) System.out.println("user not any liked in past");
                            tweet.setLikeSet(temporaryListUnLike);
                            tweetService.update(tweet);
                            System.out.println("like number is: " + temporaryListUnLike.size());
                            break;
                        case 3:
                            System.out.println("comment: ");
                            if (tweet.getCommentSet().size() == 0) {
                                System.out.println("no comments found for this tweet");
                            } else {
                                for (Comment comment : tweet.getCommentSet()
                                ) {
                                    System.out.println("comment message is:  " + comment.getMessage());
                                    System.out.println("this comment is written with: " + comment.getUser());
                                }
                            }
                            break;
                        case 4:
                            int flag = 0;
                            Set<Comment> temporaryComments = new HashSet<>(tweet.getCommentSet());
                            for (Comment comment : temporaryComments
                            ) {
                                System.out.println(comment);
                                System.out.println("like number of this comment is: " + comment.getLikeSet().size());
                                System.out.println("do you want like it? yes->y no->n");
                                if (input.next().equals("y")) {
                                    Set<Like> temporaryCommentsLike = new HashSet<>(comment.getLikeSet());
                                    for (Like temporaryLike : temporaryCommentsLike
                                    ) {
                                        if (temporaryLike.getUser().getUsername().equals(user.getUsername())) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        Like likeOfComment = new Like(user.getUsername(), comment);
                                        likeOfComment.setUser(user);
                                        temporaryCommentsLike.add(likeOfComment);
                                        likeService.save(likeOfComment);
                                        tweet.setLikeSet(temporaryCommentsLike);
                                        commentService.update(comment);
                                        System.out.println("like number of this comment is: " + temporaryCommentsLike.size());
                                    } else System.out.println("you liked in past and can not liked again");

                                } else System.out.println();
                            }
                            break;
                        case 5:
                            int flagUnlike = 0;
                            Set<Comment> temporaryCommentSet = new HashSet<>(tweet.getCommentSet());
                            for (Comment comment : temporaryCommentSet
                            ) {
                                System.out.println(comment);
                                System.out.println("like number of this comment is: " + comment.getLikeSet().size());
                                System.out.println("do you want unlike it? yes->y no->n");
                                if (input.next().equals("y")) {
                                    Like unlike = new Like();
                                    Set<Like> temporaryCommentsLike = new HashSet<>(comment.getLikeSet());
                                    for (Like temporaryLike : temporaryCommentsLike
                                    ) {
                                        if (temporaryLike.getUser().getUsername().equals(user.getUsername())) {
                                            flagUnlike++;
                                            unlike = temporaryLike;
                                        }
                                    }
                                    if (flagUnlike > 0) {
                                        temporaryCommentsLike.remove(unlike);
                                        likeService.remove(unlike);
                                        comment.setLikeSet(temporaryCommentsLike);
                                        commentService.update(comment);
                                    } else System.out.println("you do not like before that unlike now");

                                    System.out.println("like number of this comment is: " + temporaryCommentsLike.size());
                                } else System.out.println();
                            }
                            break;
                        case 6:
                            Set<Comment> comments = new HashSet<>(tweet.getCommentSet());
                            comments.add(postComment(tweet));
                            tweet.setCommentSet(comments);
                            tweetService.update(tweet);
                            break;
                        case 7:
                            Set<Comment> commentSetForEdit = new HashSet<>(tweet.getCommentSet());
                            for (Comment comment : commentSetForEdit
                            ) {
                                if (comment.getUser().getUsername().equals(user.getUsername())) {
                                    System.out.println(comment);
                                    System.out.println("do you want edit it? yes->y or no->n :");
                                    if (input.next().equals("y")) {
                                        comment.setMessage(editMessage());
                                        commentService.update(comment);
                                        System.out.println("Comment Edited.....");
                                    }
                                } else System.out.println("no have comment");
                            }
                            break;
                        case 8:
                            Set<Comment> commentSet = new HashSet<>(tweet.getCommentSet());
                            for (Comment comment : commentSet
                            ) {
                                if (comment.getUser().getUsername().equals(user.getUsername())) {
                                    System.out.println(comment);
                                    System.out.println("do you want remove it? yes->y or no->n :");
                                    if (input.next().equals("y")) {
                                        commentService.remove(comment);
                                        System.out.println("comment removed.....");

                                    }
                                } else System.out.println("you can not remove because you are not owner");
                            }
                            break;
                        case 9:
                            postTweet();
                            break;
                        case 10:
                            if (tweet.getUser().getUsername().equals(user.getUsername())) {
                                tweet.setMessage(editMessage());
                                tweetService.update(tweet);
                                System.out.println("tweet is Edited.....");
                            }
                            break;
                        case 11:
                            if (tweet.getUser().getUsername().equals(user.getUsername())) {
                                tweetService.remove(tweet);
                                System.out.println("tweet is removed.....");
                            } else System.out.println("you can not remove this tweet because you are not owner");
                            break;
                        case 12:
                            System.out.println("please type username");
                            try {
                                userService.findUserByUsername(input.next()).ifPresent(System.out::println);
                            } catch (NoResultException e) {
                                System.out.println("person with this username not found");
                            }
                            break;
                        case 13:
                            showProfile();
                            break;
                        case 14:
                            String message = checkTweetLength();
                            Set<Tweet> tweetSet = new HashSet<>();
                            Tweet validTweet = new Tweet(message, user);
                            tweetSet.add(validTweet);
                            user.setTweetSet(tweetSet);
                            tweetService.isValid(validTweet);
                            userService.update(user);
                            break;
                        case 15:
                            System.out.println();
                            menuFlag = false;
                            break;
                    }
                }
            }
        }
    }
}