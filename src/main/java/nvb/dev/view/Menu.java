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
import java.util.Optional;
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

    private Set<Tweet> listOfTweets = new HashSet<>();

    private Scanner input = new Scanner(System.in);

    private static User user = new User();

    public void showMainMenu() {
        System.out.println();
        System.out.println("-----Twitter-----");
        System.out.println();

        System.out.println("1) Signup");
        System.out.println("2) Login");
        System.out.println("3) Exit");

        System.out.println();

        System.out.print("Choose Option : ");
        switch (input.nextInt()) {
            case 1:
                signUp();
                login();
                break;
            case 2:
                login();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Option! Try Again.");
                showMainMenu();
        }
    }

    public void signUp() {
        System.out.println();
        System.out.println("-----Sign Up Page-----");
        System.out.println();

        System.out.println("First Name : ");
        String firstName = input.next();

        System.out.println("Last Name : ");
        String lastName = input.next();

        System.out.println("Username : ");
        String username = input.next();

        System.out.println("Password : ");
        String password = input.next();

        Tweet joinTweet = new Tweet();
        joinTweet.setMessage(username + " have joined twitter.");
        listOfTweets.add(joinTweet);

        User newUser = new User(firstName, lastName, username, password);
        joinTweet.setUser(newUser);

        if (!userService.isValid(newUser)) {
            showMainMenu();
        } else {
            user = newUser;
        }
    }

    public void login() {
        System.out.println();
        System.out.println("-----Login Page-----");
        System.out.println();

        System.out.println("Username : ");
        String username = input.next();

        System.out.println("Password : ");
        String password = input.next();

        try {
            if (userService.findUserByUsername(username).isPresent() &&
                    userService.findByPassword(password).isPresent()) {

                // This code was suggested by SonarLint
                Optional<User> userOptional = userService.findByPassword(password);
                userOptional.ifPresent(value -> user = value);
                showHome();

            }
        } catch (NoResultException e) {
            System.out.println("Invalid username and password!");
            showMainMenu();
        }
    }

    public void showHome() {
        System.out.println();

        while (true) {

            System.out.println("----Home-----");

            Set<Tweet> tweets = new HashSet<>(tweetService.findAll());
            if (tweets.isEmpty()) {

                System.out.println("There are no tweets.");
                System.out.println("Do you want to write a new tweet? y/n : ");

                if (input.next().equals("y")) sendTweet();
                else if (input.next().equals("n")) System.exit(0);

            } else {

                for (Tweet tweet : tweets) {
                    boolean menuFlag = true;
                    while (menuFlag) {
                        System.out.println("*******");
                        System.out.println("Tweet Message : " + tweet.getMessage());
                        System.out.println("Written by : " + tweet.getUser());
                        System.out.println("Number of Likes : " + tweet.getLikeSet().size());
                        tweet.getLikeSet().forEach(System.out::println);
                        System.out.println("Number of Comments : " + tweet.getCommentSet().size());
                        System.out.println("*******");

                        System.out.println();

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
                        System.out.println("14) Test Tweet Length");
                        System.out.println("15) Next Tweet");

                        System.out.println();

                        switch (input.nextInt()) {
                            case 1:
                                int likeFlag = 0;

                                Set<Like> tempLikeList = new HashSet<>(tweet.getLikeSet());

                                for (Like like : tempLikeList) {
                                    if (like.getUser().getUsername().equals(user.getUsername())) {
                                        likeFlag++;
                                    }
                                }

                                if (likeFlag == 0) {
                                    Like newLike = new Like(user.getUsername(), tweet);
                                    newLike.setUser(user);
                                    tempLikeList.add(newLike);
                                    likeService.save(newLike);
                                    tweet.setLikeSet(tempLikeList);
                                    tweetService.update(tweet);
                                } else {
                                    System.out.println("It was liked before.");
                                }
                                System.out.println("Number of Likes : " + tempLikeList.size());
                                break;

                            case 2:
                                boolean statusFlag = false;

                                Set<Like> tempDislikeList = new HashSet<>(tweet.getLikeSet());

                                for (Like dislike : tempDislikeList) {
                                    if (dislike.getUser().getUsername().equals(user.getUsername())) {
                                        tempDislikeList.remove(dislike);
                                        likeService.remove(dislike);
                                        break;
                                    } else {
                                        statusFlag = true;
                                    }
                                }

                                if (statusFlag) System.out.println("User has not liked any posts.");

                                tweet.setLikeSet(tempDislikeList);
                                tweetService.update(tweet);
                                System.out.println("Number of Likes : " + tempDislikeList.size());
                                break;

                            case 3:
                                System.out.println("Comment : ");
                                if (tweet.getCommentSet().isEmpty()) {
                                    System.out.println("No comments found!");
                                } else {
                                    for (Comment comment : tweet.getCommentSet()) {
                                        System.out.println("Comment : " + comment.getMessage());
                                        System.out.println("Written by : " + comment.getUser());
                                    }
                                }
                                break;

                            case 4:
                                int flag = 0;

                                Set<Comment> tempComments = new HashSet<>(tweet.getCommentSet());

                                for (Comment comment : tempComments) {
                                    System.out.println("Number of Likes : " + comment.getLikeSet().size());
                                    System.out.println("Do you wan to like this comment? y/n : ");
                                    if (input.next().equals("y")) {
                                        Set<Like> tempCommentLikes = new HashSet<>(comment.getLikeSet());
                                        for (Like tempLike : tempCommentLikes) {
                                            if (tempLike.getUser().getUsername().equals(user.getUsername())) {
                                                flag++;
                                            }
                                        }

                                        if (flag == 0) {
                                            Like commentLike = new Like(user.getUsername(), tweet);
                                            commentLike.setUser(user);
                                            tempCommentLikes.add(commentLike);
                                            likeService.save(commentLike);
                                            tweet.setLikeSet(tempCommentLikes);
                                            commentService.update(comment);
                                            System.out.println("Number of likes for this comment : " +
                                                    tempCommentLikes.size());
                                        } else System.out.println("You have liked it before.");

                                    } else System.out.println();
                                }
                                break;

                            case 5:
                                int dislikeFlag = 0;
                                Set<Comment> tempCommentList = new HashSet<>(tweet.getCommentSet());
                                for (Comment comment : tempCommentList) {
                                    System.out.println(comment);
                                    System.out.println("Number of Likes : " + comment.getLikeSet().size());
                                    System.out.println("Do you want to dislike? y/n : ");
                                    if (input.next().equals("y")) {
                                        Like dislike = new Like();
                                        Set<Like> tempCommentLike = new HashSet<>(comment.getLikeSet());
                                        for (Like tempLike : tempCommentLike) {
                                            if (tempLike.getUser().getUsername().equals(user.getUsername())) {
                                                dislikeFlag++;
                                                dislike = tempLike;
                                            }
                                        }
                                        if (dislikeFlag > 0) {
                                            tempCommentLike.remove(dislike);
                                            likeService.remove(dislike);
                                            comment.setLikeSet(tempCommentLike);
                                            commentService.update(comment);
                                        } else System.out.println("You have not liked it before.");

                                        System.out.println("Number of Likes : " + tempCommentLike.size());

                                    } else {
                                        System.out.println();
                                    }
                                }
                                break;

                            case 6:
                                Set<Comment> comments = new HashSet<>(tweet.getCommentSet());
                                comments.add(sendComment(tweet));
                                tweet.setCommentSet(comments);
                                tweetService.update(tweet);
                                break;

                            case 7:
                                Set<Comment> commentForEdit = new HashSet<>(tweet.getCommentSet());
                                for (Comment comment : commentForEdit) {
                                    if (comment.getUser().getUsername().equals(user.getUsername())) {
                                        System.out.println(comment);
                                        System.out.println("Do you want to edit? y/n : ");
                                        if (input.next().equals("y")) {
                                            comment.setMessage(editMessage());
                                            commentService.update(comment);
                                            System.out.println("Comment edited successfully!");
                                        }
                                    } else {
                                        System.out.println("There is no comment.");
                                    }
                                }
                                break;

                            case 8:
                                Set<Comment> commentSet = new HashSet<>(tweet.getCommentSet());
                                for (Comment comment : commentSet) {
                                    if (comment.getUser().getUsername().equals(user.getUsername())) {
                                        System.out.println(comment);
                                        System.out.println("Do you want to remove? y/n : ");
                                        if (input.next().equals("y")) {
                                            commentService.remove(comment);
                                            System.out.println("Comment removed successfully!");
                                        }
                                    } else {
                                        System.out.println("You're not writer of the comment.");
                                    }
                                }
                                break;

                            case 9:
                                sendTweet();
                                break;

                            case 10:
                                if (tweet.getUser().getUsername().equals(user.getUsername())) {
                                    tweet.setMessage(editMessage());
                                    tweetService.update(tweet);
                                    System.out.println("Tweet edited successfully!");
                                }
                                break;

                            case 11:
                                if (tweet.getUser().getUsername().equals(user.getUsername())) {
                                    tweetService.remove(tweet);
                                    System.out.println("Tweet removed successfully!");
                                } else {
                                    System.out.println("You're not the owner of this tweet to delete it.");
                                }
                                break;

                            case 12:
                                System.out.println("Enter username : ");
                                try {
                                    userService.findUserByUsername(input.next())
                                            .ifPresent(System.out::println);
                                } catch (NoResultException e) {
                                    System.out.println("Not Found.");
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
                                menuFlag = false;
                                break;

                        }
                    }
                }
            }

        }

    }

    public void showProfile() {
        System.out.println();
        System.out.println("-----Profile-----");
        System.out.println();

        System.out.println("----------");
        System.out.println(user.getUsername());
        System.out.println("----------");

        System.out.println("1) Edit Profile");
        System.out.println("2) Remove Account");
        System.out.println("3) Logout");

        System.out.println();
        System.out.print("Choose Option : ");

        switch (input.nextInt()) {
            case 1:
                editProfile();
                break;
            case 2:
                userService.remove(user);
                System.out.println("Account is removed successfully!");
                user = null;
                showMainMenu();
                break;
            case 3:
                user = null;
                showMainMenu();
                break;
            default:
                System.out.println("Invalid Option");
        }
    }

    public void editProfile() {
        System.out.println();
        System.out.println("-----Edit Profile-----");
        System.out.println();

        System.out.println("First Name : ");
        String firstName = input.next();

        System.out.println("Last Name : ");
        String lastName = input.next();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (!userService.isValid(user)) {
            showMainMenu();
        } else {
            userService.update(user);
            System.out.println("Profile Updated Successfully!");
        }
    }

    public String checkTweetLength() {
        return "a".repeat(270);
    }

    public void sendTweet() {
        System.out.println();
        System.out.println("Writer a message : ");
        String message = input.next();

        Set<Tweet> tweets = new HashSet<>();
        Set<Like> likes = new HashSet<>();
        Set<Comment> comments = new HashSet<>();

        Tweet tweet = new Tweet(message, user);
        tweet.setLikeSet(likes);
        tweet.setCommentSet(comments);

        tweets.add(tweet);

        user.setTweetSet(tweets);

        tweetService.save(tweet);
        userService.update(user);

        showHome();
    }

    public Comment sendComment(Tweet tweet) {
        System.out.println();
        System.out.println("Write a comment : ");
        String message = input.next();

        Set<Like> likes = new HashSet<>();

        Comment comment = new Comment(message, tweet);
        comment.setUser(user);
        comment.setLikeSet(likes);

        commentService.save(comment);
        return comment;
    }

    public String editMessage() {
        System.out.println();
        System.out.println("Type your message : ");
        return input.next();
    }

}