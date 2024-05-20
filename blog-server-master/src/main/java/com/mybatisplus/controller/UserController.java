package com.mybatisplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.*;
import com.mybatisplus.mapper.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/user")
public class UserController {
//    @Autowired
//    UserService userService;

    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    BookmarkMapper bookmarkMapper;
    @Autowired
    FollowMapper followMapper;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    ReadArticleMapper readArticleMapper;
    @Autowired
    UserReviewMapper userReviewMapper;

//    @RequestMapping("/selectAll")
//    public List<User> selectAll() {
//        return userMapper.selectList(null);
//    }
//
//    @RequestMapping("/insert")
//    public String insert(@Param("name") String name) {
//        User user = new User();
//        user.setUserName(name + "h");
//        return userMapper.insert(user) > 0 ? "success" : "fail";
//    }
//
//    @RequestMapping("/hello")
//    public String hello() {
//        return "hello";
//    }


    @AllArgsConstructor
    @Data
    class SearchResponse {
        List<String> articleResult;
        List<String> userResult;
    }

    @RequestMapping("/search")
    public SearchResponse search(String content) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.like("article_title", content);
        List<Article> articles = articleMapper.selectList(articleQueryWrapper);

        List<String> articleResult = new ArrayList<>();
        for (Article article : articles) {
            articleResult.add(article.getArticleId());
        }


        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("user_name", content);
        List<User> users = userMapper.selectList(userQueryWrapper);

        List<String> userResult = new ArrayList<>();
        for (User user : users) {
            userResult.add(user.getUserName());
        }

        return new SearchResponse(articleResult, userResult);
    }


//    @RequestMapping("/signup")
//    public void signup(String user, String password) {
//
//    }

    @AllArgsConstructor
    @Data
    class LoginResponse {
        boolean success;
        String token;
    }

    @RequestMapping("/login")
    public LoginResponse login(String user, String password) {
//        ResponseEntity.ok().body("s");
//        ResponseEntity.status(HttpStatus.OK).body("yes");
//        ResponseEntity responseEntity = new ResponseEntity<>("not hound", HttpStatus.NOT_FOUND);
        User user1 = userMapper.selectById(user);
        if (user1 == null) {
            return new LoginResponse(false, "");
        }
        if (user1.getUserPassword().equals(password)) {
            int rand = (new SecureRandom()).nextInt((int) 1e63);
            String token = String.format("%064d", rand);
            Login login = new Login(user, token, getNowDate());

            if (loginMapper.selectById(login) != null) {
                loginMapper.updateById(login);
            } else {
                loginMapper.insert(login);
            }
            return new LoginResponse(true, token);

        } else {
            return new LoginResponse(false, "");
        }

    }

    public Date getNowDate() {
        // 获取当前时区
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");

        // 获取当前时间的 Calendar 对象，并设置时区
        Calendar calendar = Calendar.getInstance(timeZone);

        // 获取当前北京时间
        Date beijingTime = calendar.getTime();

        return beijingTime;
    }


    @AllArgsConstructor
    @Data
    class UserResponse {
        boolean isAdmin;
        List<String> followingUsers;
        List<String> authoredArticleIDs;
        List<String> bookmarkedArticleIDs;
    }

    @RequestMapping("/user")
    public UserResponse getUser(String username) {
        User user = userMapper.selectById(username);
        if (user == null) {
            return null;
        }
        boolean isAdmin = user.getIsAdministrator() == 1;

        QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper.eq("follower_name", username);
        List<Follow> follows = followMapper.selectList(followQueryWrapper);
        List<String> followingUsers = follows.stream().map(Follow::getFollowedName).toList();


        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("author_name", username);
        List<Article> articles = articleMapper.selectList(articleQueryWrapper);
        List<String> authoredArticleIDs = articles.stream().map(Article::getArticleId).toList();


        QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
        bookmarkQueryWrapper.eq("user_name", username);
        List<Bookmark> bookmarks = bookmarkMapper.selectList(bookmarkQueryWrapper);
        List<String> bookmarkedArticleIDs = bookmarks.stream().map(Bookmark::getArticleId).toList();

        return new UserResponse(isAdmin, followingUsers, authoredArticleIDs, bookmarkedArticleIDs);
    }

    @AllArgsConstructor
    @Data
    class ProfileResponse {
        String avatarURL;
        Long readCount;
        Long fanCount;
        String bio;
        boolean following;
    }

    @RequestMapping("/user/profile")
    public ProfileResponse getUserProfile(String username, String currentUsername) {
        User user = userMapper.selectById(username);
        String avatarURL = user.getUserAvatar();
        String bio = user.getUserSignature();

//        QueryWrapper<ReadArticle> readArticleQueryWrapper = new QueryWrapper<>();
//        readArticleQueryWrapper.eq("readerName", username);
//        Long readCount = readArticleMapper.selectCount(readArticleQueryWrapper);
//
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("author_name", username);
        List<Article> articles = articleMapper.selectList(articleQueryWrapper);
        Long readCount = 0L;
        for (Article article : articles) {
            QueryWrapper<ReadArticle> readArticleQueryWrapper = new QueryWrapper<>();
            readArticleQueryWrapper.eq("article_id", article.getArticleId());
            readCount += readArticleMapper.selectCount(readArticleQueryWrapper);
        }


        QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper.eq("followed_name", username);
        List<Follow> follows = followMapper.selectList(followQueryWrapper);
        Long fanCount = (long) follows.size();

        boolean following;
        if (currentUsername.isEmpty() || currentUsername.equals(username)) {
            following = false;
        } else {
            following = follows.stream().map(Follow::getFollowerName).toList().contains(currentUsername);
        }

        return new ProfileResponse(avatarURL, readCount, fanCount, bio, following);
    }


    private boolean isWithinTenMinutes(Date date) {
        // 获取当前时间的 Instant
        Instant currentInstant = Instant.now();

        // 获取要比较的 Date 对象的 Instant
        Instant targetInstant = date.toInstant();

        // 计算两个 Instant 之间的差值（Duration）
        Duration duration = java.time.Duration.between(targetInstant, currentInstant);

        // 判断差值是否在十分钟之内
        return duration.toMinutes() <= 10;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class EditRequest {
        String username;
        String token;
        String avatarURL;
        String bio;
    }

    @PostMapping("/user/edit")
    public boolean userEdit(
//            @RequestHeader String username,
//            @RequestHeader String token,
            @RequestBody EditRequest editRequest) {
        String username = editRequest.getUsername();
        String token = editRequest.getToken();

        Login login = loginMapper.selectById(username);
        if (login == null) {
            return false;
        }
//        isWithinTenMinutes(login.getTimeStamp());

        User user = userMapper.selectById(username);
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            User newUser = new User(username, user.getUserPassword(), user.getIsAdministrator(), editRequest.getBio(), editRequest.getAvatarURL());
            userMapper.updateById(newUser);
            return true;
        } else {
            return false;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class SignupRequest {
        String username;
        String password;
        List<String> userFollow;
    }


    @PostMapping("/signup")
    public void signup(/*@RequestHeader String username,
                       @RequestHeader String password,*/
                       @RequestBody SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();

        if (userMapper.selectById(username) != null) {
            return;
        }

        User user = new User();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setIsAdministrator(0);
        user.setUserAvatar("http://localhost:3000/images/default.png");

        userMapper.insert(user);

        List<String> userFollow = signupRequest.getUserFollow();
        for (String str : userFollow) {
            Follow follow = new Follow();
            follow.setFollowedName(str);
            follow.setFollowerName(username);
            followMapper.insert(follow);
        }

    }


    @RequestMapping("/user/applyAdmin")
    public boolean applyAdmin(String username, String token) {
        User user = userMapper.selectById(username);
        if (user == null) {
            return false;
        }
        Login login = loginMapper.selectById(username);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            if (user.getIsAdministrator() == 1) {//本人已经是管理员
                return true;
            }

            //已经申请过了,返回
            QueryWrapper<UserReview> userReviewQueryWrapper = new QueryWrapper<>();
            userReviewQueryWrapper.eq("applicant_name", username);
            if (userReviewMapper.exists(userReviewQueryWrapper)) {
                return true;
            }


            //寻找合适的审核员
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("is_administrator", 1);
            List<User> auditors = userMapper.selectList(userQueryWrapper);

            List<Long> auditorWorks = new ArrayList<>();

            if (auditors.isEmpty()) {//无管理员
                return true;
            }

            //已经找到所有审核员
            for (User auditor : auditors) {
                userReviewQueryWrapper = new QueryWrapper<>();
                userReviewQueryWrapper.eq("administrator_name", auditor.getUserName());
                Long work = userReviewMapper.selectCount(userReviewQueryWrapper);
                auditorWorks.add(work);
            }
            Long minWork = Collections.min(auditorWorks);
            int index = auditorWorks.indexOf(minWork);
            //其中工作量最低的一个
            User auditor = auditors.get(index);

            //插入user review表
            UserReview userReview = new UserReview();
            userReview.setAdministratorName(auditor.getUserName());
            userReview.setApplicantName(username);
            userReview.setTimeStamp(getNowDate());
            userReviewMapper.insert(userReview);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/user/checkValid")
    public boolean checkValid(String username, String token) {
        User user = userMapper.selectById(username);
        if (user == null) {
            return false;
        }
        Login login = loginMapper.selectById(username);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping("/user/checkAdmin")
    public boolean checkAdmin(String username) {
        User user = userMapper.selectById(username);
        if (user == null) {
            return false;
        }
        return user.getIsAdministrator() == 1;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class ReleaseRequest {
        String username;
        String token;
        boolean announcement;
        String title;
        String content;
    }

    @PostMapping("/article/release")
    public boolean release(/*@RequestHeader String username,
                           @RequestHeader String token,*/
                           @RequestBody ReleaseRequest releaseRequest) {
        String username = releaseRequest.getUsername();
        String token = releaseRequest.getToken();

        if (checkValid(username, token)) {
//            Login login = loginMapper.selectById(username);
//            login.setTimeStamp(getNowDate());

            Article article = new Article();
            article.setAuthorName(username);
            article.setArticleContent(releaseRequest.getContent());
            article.setArticleTitle(releaseRequest.getTitle());
            article.setIsAnnouncement(releaseRequest.isAnnouncement() ? 1 : 0);
            article.setTimeStamp(getNowDate());
            articleMapper.insert(article);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/user/follow")
    public boolean follow(String user, String token, String followUser) {

        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
            followQueryWrapper.eq("followed_name", followUser).eq("follower_name", user);
            if (followMapper.exists(followQueryWrapper)) {
                return true;
            }

            Follow follow = new Follow();
            follow.setFollowedName(followUser);
            follow.setFollowerName(user);
            followMapper.insert(follow);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/user/unfollow")
    public boolean unfollow(String user, String token, String followUser) {

        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
            followQueryWrapper.eq("followed_name", followUser).eq("follower_name", user);
            followMapper.delete(followQueryWrapper);

            return true;
        } else {
            return false;
        }
    }

    @AllArgsConstructor
    @Data
    class RecommendResponse {
        List<String> recommendedUsers;
        List<String> recommendedUserAvatars;
    }

    @RequestMapping("/signup/recommend")
    public RecommendResponse recommend() {
        int num = 7;
        List<User> allUsers = new ArrayList<>(userMapper.selectList(null));
        Collections.shuffle(allUsers);
        List<User> selectedUsers = allUsers.subList(0, Math.min(num, allUsers.size()));

        List<String> recommendedUsers = selectedUsers.stream().map(User::getUserName).toList();
        List<String> recommendedUserAvatars = selectedUsers.stream().map(User::getUserAvatar).toList();

        return new RecommendResponse(recommendedUsers, recommendedUserAvatars);
    }

    @RequestMapping("/signup/exists")
    public boolean userExists(String user) {
        return userMapper.selectById(user) != null;
    }

    @RequestMapping("/article/read")
    public void readArticle(String id, String user) {
        ReadArticle readArticle = new ReadArticle();
        readArticle.setArticleId(id);
        readArticle.setReaderName(user);
        readArticle.setTimeStamp(getNowDate());
        readArticleMapper.insert(readArticle);
    }

    @RequestMapping("/logout")
    public void logout(String user) {
        loginMapper.deleteById(user);
    }
}
