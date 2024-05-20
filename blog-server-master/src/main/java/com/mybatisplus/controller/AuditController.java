package com.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.*;
import com.mybatisplus.mapper.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class AuditController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleReviewMapper articleReviewMapper;
    @Autowired
    UserReviewMapper userReviewMapper;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleCommentMapper articleCommentMapper;
    @Autowired
    CommentCommentMapper commentCommentMapper;
    @Autowired
    LikeRecordMapper likeMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    BookmarkMapper bookmarkMapper;
    @Autowired
    ReadArticleMapper readArticleMapper;


    @AllArgsConstructor
    @Data
    class AuditResponse {
        List<String> articleIDs;
        List<String> users;
    }

    @RequestMapping("/audit")
    public AuditResponse audit(String username) {

        User user = userMapper.selectById(username);
        if (user.getIsAdministrator() != 1) {
            return null;
        }

        QueryWrapper<ArticleReview> articleReviewQueryWrapper = new QueryWrapper<>();
        articleReviewQueryWrapper.eq("administrator_name", username);
        List<ArticleReview> articleReviews = articleReviewMapper.selectList(articleReviewQueryWrapper);
        List<String> articleIDs = articleReviews.stream().map(ArticleReview::getArticleId).toList();

        QueryWrapper<UserReview> userReviewQueryWrapper = new QueryWrapper<>();
        userReviewQueryWrapper.eq("administrator_name", username);
        List<UserReview> userReviews = userReviewMapper.selectList(userReviewQueryWrapper);
        List<String> users = userReviews.stream().map(UserReview::getApplicantName).toList();

        return new AuditResponse(articleIDs, users);
    }

    @RequestMapping("/audit/agreeUser")
    public boolean agreeUser(String admin, String token, String user) {
        Login login = loginMapper.selectById(admin);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

//            User admin1 = userMapper.selectById(admin);
            User user1 = userMapper.selectById(user);
            //新晋管理员
            user1.setIsAdministrator(1);
            userMapper.updateById(user1);

            //删除user_review中的记录
            QueryWrapper<UserReview> userReviewQueryWrapper = new QueryWrapper<>();
            userReviewQueryWrapper.eq("administrator_name", admin).eq("applicant_name", user);
            userReviewMapper.delete(userReviewQueryWrapper);

            return true;
        } else {
            return false;
        }
    }


    @RequestMapping("/audit/refuseUser")
    public boolean refuseUser(String admin, String token, String user) {
        Login login = loginMapper.selectById(admin);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

//            User admin1 = userMapper.selectById(admin);
//            User user1 = userMapper.selectById(user);

            //新晋管理员失败
//            user1.setIsAdministrator(0);
//            userMapper.updateById(user1);

            //新晋管理员失败
            //删除user_review中的记录
            QueryWrapper<UserReview> userReviewQueryWrapper = new QueryWrapper<>();
            userReviewQueryWrapper.eq("administrator_name", admin).eq("applicant_name", user);
            userReviewMapper.delete(userReviewQueryWrapper);

            return true;
        } else {
            return false;
        }
    }


    @RequestMapping("/audit/passArticle")
    public boolean passArticle(String admin, String token, String articleId) {
        Login login = loginMapper.selectById(admin);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);


            QueryWrapper<ArticleReview> articleReviewQueryWrapper = new QueryWrapper<>();
            articleReviewQueryWrapper.eq("administrator_name", admin).eq("article_id", articleId);
            articleReviewMapper.delete(articleReviewQueryWrapper);
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/audit/banArticle")
    public boolean banArticle(String admin, String token, String articleId) {
        Login login = loginMapper.selectById(admin);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            //删除文章
            articleMapper.deleteById(articleId);

            //删除审核记录
            QueryWrapper<ArticleReview> articleReviewQueryWrapper = new QueryWrapper<>();
            articleReviewQueryWrapper.eq("article_id", articleId);
            articleReviewMapper.delete(articleReviewQueryWrapper);

            //删除评论，评论的评论
            QueryWrapper<ArticleComment> articleCommentQueryWrapper = new QueryWrapper<>();
            articleCommentQueryWrapper.eq("article_id", articleId);
            List<ArticleComment> articleComments = articleCommentMapper.selectList(articleCommentQueryWrapper);
            for (ArticleComment articleComment : articleComments) {
                deleteComment(articleComment.getCommentId());
            }
            articleCommentMapper.delete(articleCommentQueryWrapper);
            //删除收藏
            QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
            bookmarkQueryWrapper.eq("article_id", articleId);
            bookmarkMapper.delete(bookmarkQueryWrapper);
            //删除点赞
            QueryWrapper<LikeRecord> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.eq("article_id", articleId);
            likeMapper.delete(likeQueryWrapper);
            //删除阅读
            QueryWrapper<ReadArticle> readArticleQueryWrapper = new QueryWrapper<>();
            readArticleQueryWrapper.eq("article_id", articleId);
            readArticleMapper.delete(readArticleQueryWrapper);


            return true;
        } else {
            return false;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class StatisticsResponse {
        List<Long> newArticleCount;// 近五日的每日新增文章数量
        List<Long> newCommentCount; // 近五日的每日新增评论数量
    }

    @RequestMapping("/statistics")
    public StatisticsResponse statistics() {
        List<Long> newArticleCount = Arrays.asList(0L, 0L, 0L, 0L, 0L);
        List<Long> newCommentCount = Arrays.asList(0L, 0L, 0L, 0L, 0L);

        Date today = getNowDate();

        List<Article> articles = articleMapper.selectList(null);
        for (Article article : articles) {
            int dif = getDaysDifference(article.getTimeStamp(), today);
            if (dif <= 4) {
                newArticleCount.set(4 - dif, newArticleCount.get(4 - dif) + 1);
            }
        }

        List<Comment> comments = commentMapper.selectList(null);
        for (Comment comment : comments) {
            int dif = getDaysDifference(comment.getTimeStamp(), today);
            if (dif <= 4) {
                newCommentCount.set(4 - dif, newCommentCount.get(4 - dif) + 1);
            }
        }

        return new StatisticsResponse(newArticleCount, newCommentCount);
    }

    private void deleteComment(String commentId) {
        commentMapper.deleteById(commentId);
        QueryWrapper<CommentComment> commentCommentQueryWrapper = new QueryWrapper<>();
        commentCommentQueryWrapper.eq("commented_comment_id", commentId);
        List<CommentComment> commentComments = commentCommentMapper.selectList(commentCommentQueryWrapper);
        for (CommentComment commentComment : commentComments) {
            deleteComment(commentComment.getCommentCommentId());
        }
        commentCommentMapper.delete(commentCommentQueryWrapper);
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

    public int getDaysDifference(Date date1, Date date2) {
        // 将Date转换为Calendar
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        resetTime(cal1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        resetTime(cal2);

        // 计算日期差异
        long milliseconds1 = cal1.getTimeInMillis();
        long milliseconds2 = cal2.getTimeInMillis();
        long diff = milliseconds1 - milliseconds2;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return (int) diffDays;
    }

    private void resetTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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
}
