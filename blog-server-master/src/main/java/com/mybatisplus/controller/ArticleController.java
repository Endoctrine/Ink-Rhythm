package com.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.*;
import com.mybatisplus.mapper.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class ArticleController {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleCommentMapper articleCommentMapper;
    @Autowired
    ReadArticleMapper readArticleMapper;
    @Autowired
    LikeRecordMapper likeMapper;
    @Autowired
    BookmarkMapper bookmarkMapper;
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentCommentMapper commentCommentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleReviewMapper articleReviewMapper;

    @AllArgsConstructor
    @Data
    static class ArticleResponse {
        String articleTitle;
        String author;
        String releaseTime;
        Long viewCount;
        Long likeCount;
        Long bookmarkCount;
        String articleContent;
    }

    @RequestMapping("/article")
    public ArticleResponse getArticle(String id) {
        Article article = articleMapper.selectById(id);

        if (article == null) {
            return null;
        }

        String articleTitle = article.getArticleTitle();
        String author = article.getAuthorName();
        String releaseTime = dateToString(article.getTimeStamp());

        QueryWrapper<ReadArticle> readArticleWrapper = new QueryWrapper<>();
        readArticleWrapper.eq("article_id", id);
        Long viewCount = readArticleMapper.selectCount(readArticleWrapper);

        QueryWrapper<LikeRecord> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("article_id", id);
        Long likeCount = likeMapper.selectCount(likeQueryWrapper);
//        Long likeCount = 0L;

        QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
        bookmarkQueryWrapper.eq("article_id", id);
        Long bookCount = bookmarkMapper.selectCount(bookmarkQueryWrapper);

        String articleContent = article.getArticleContent();
        return new ArticleResponse(articleTitle, author, releaseTime, viewCount, likeCount,
            bookCount, articleContent);
    }

    private String dateToString(Date date) {
        // 创建 SimpleDateFormat 对象，指定日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 使用 SimpleDateFormat 将 Date 转换为字符串
        return dateFormat.format(date);
    }

    @RequestMapping("/articleCommentSection")
    public List<String> getArticleComments(String id) {
        QueryWrapper<ArticleComment> articleCommentQueryWrapper = new QueryWrapper<>();
        articleCommentQueryWrapper.eq("article_id", id);
        List<ArticleComment> articleComments =
            articleCommentMapper.selectList(articleCommentQueryWrapper);
        List<String> commentIds;
        commentIds = articleComments.stream().map(ArticleComment::getCommentId).toList();
        return commentIds;
    }


    @AllArgsConstructor
    @Data
    class HomeResponse {
        List<String> announcementIDs;
        List<String> hotIDs;
        List<String> recommendIDs;
    }

    @RequestMapping("/home")
    public HomeResponse getHome(String user) {
        List<Article> articles = articleMapper.selectList(null);
        List<String> announcementIDs = new ArrayList<>();
        List<String> hotIDs = new ArrayList<>();
        List<String> recommendIDs = new ArrayList<>();

        for (Article article : articles) {
            if (article.getIsAnnouncement() == 1 && announcementIDs.size() < 3) {
                announcementIDs.add(article.getArticleId());
            }
        }

        Collections.shuffle(articles);

        for (Article article : articles) {
            if (hotIDs.size() < 3) {
                hotIDs.add(article.getArticleId());
            }
        }

        Collections.shuffle(articles);

        for (Article article : articles) {
            if (recommendIDs.size() < 3) {
                recommendIDs.add(article.getArticleId());
            }
        }

        return new HomeResponse(announcementIDs, hotIDs, recommendIDs);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class CommentArticleRequest {
        String user;
        String token;
        String articleId;
        String commentContent;
    }

    @PostMapping("/article/commentArticle")
    public boolean commentArticle(/*@RequestHeader String user,
                                  @RequestHeader String token,
                                  @RequestHeader String articleId,*/
        @RequestBody CommentArticleRequest comment) {
        String user = comment.getUser();
        String token = comment.getToken();
        String articleId = comment.getArticleId();


        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            //新建评论
            Comment newComment = new Comment();
            newComment.setCommentId(UUID.randomUUID().toString().substring(0, 32));
            newComment.setCommentContent(comment.getCommentContent());
            newComment.setCommenterName(user);
            newComment.setTimeStamp(getNowDate());
            commentMapper.insert(newComment);

            //找到commentId
//            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
//            commentQueryWrapper.eq("comment_content", newComment.getCommentContent()).eq("commenter_name", newComment.getCommenterName());
//            commentQueryWrapper.eq("time_stamp", newComment.getTimeStamp());
//            String commentId = commentMapper.selectList(commentQueryWrapper).get(0).getCommentId();
            String commentId = newComment.getCommentId();

            //新建文章评论
            ArticleComment articleComment = new ArticleComment();
            articleComment.setArticleId(articleId);
            articleComment.setCommentId(commentId);
            articleCommentMapper.insert(articleComment);

            return true;
        } else {
            return false;
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class CommentCommentRequest {
        String user;
        String token;
        String commentId;
        String commentContent;
    }

    @PostMapping("/article/commentComment")
    public boolean commentComment(/*@RequestHeader String user,
                                  @RequestHeader String token,
                                  @RequestHeader String commentId,*/
        @RequestBody CommentCommentRequest comment) {
        String user = comment.getUser();
        String token = comment.getToken();
        String commentId = comment.getCommentId();

        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }
        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            //新建评论
            Comment newComment = new Comment();
            newComment.setCommentId(UUID.randomUUID().toString().substring(0, 32));
            newComment.setCommentContent(comment.getCommentContent());
            newComment.setCommenterName(user);
            newComment.setTimeStamp(getNowDate());
            commentMapper.insert(newComment);

            //找到commentId
//            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
//            commentQueryWrapper.eq("comment_content", newComment.getCommentContent()).eq("commenter_name", newComment.getCommenterName());
//            String newCommentId = commentMapper.selectList(commentQueryWrapper).get(0).getCommentId();
            String newCommentId = newComment.getCommentId();

            //新建评论评论
            CommentComment commentComment = new CommentComment();
            commentComment.setCommentId(newCommentId);
            commentComment.setCommentedCommentId(commentId);
            commentCommentMapper.insert(commentComment);

            return true;
        } else {
            return false;
        }
    }

    @AllArgsConstructor
    @Data
    class FeedbackInfoResponse {
        boolean isAdmin; // 代表user是否是管理员
        boolean liked; // 代表user是否点赞了文章
        boolean bookmarked; // 代表user是否收藏了文章
    }

    @RequestMapping("/article/feedbackInfo")
    public FeedbackInfoResponse feedbackInfo(String articleId, String user) {
        User user1 = userMapper.selectById(user);
        boolean isAdmin = user1.getIsAdministrator() == 1;

        QueryWrapper<LikeRecord> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("liker_name", user).eq("article_id", articleId);
        boolean liked = likeMapper.exists(likeQueryWrapper);


        QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
        bookmarkQueryWrapper.eq("user_name", user).eq("article_id", articleId);
        boolean bookmarked = bookmarkMapper.exists(bookmarkQueryWrapper);

        return new FeedbackInfoResponse(isAdmin, liked, bookmarked);
    }

    @RequestMapping("/article/like")
    public boolean like(String articleId, String user, String token) {
        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            QueryWrapper<LikeRecord> likeRecordQueryWrapper = new QueryWrapper<>();
            likeRecordQueryWrapper.eq("article_id", articleId).eq("liker_name", user);
            if (likeMapper.exists(likeRecordQueryWrapper)) {
                return true;
            }

            LikeRecord like = new LikeRecord();
            like.setArticleId(articleId);
            like.setLikerName(user);
            like.setTimeStamp(getNowDate());
            likeMapper.insert(like);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/article/dislike")
    public boolean dislike(String articleId, String user, String token) {
        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            QueryWrapper<LikeRecord> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.eq("liker_name", user).eq("article_id", articleId);
            likeMapper.delete(likeQueryWrapper);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/article/bookmark")
    public boolean bookmark(String articleId, String user, String token) {
        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            Bookmark bookmark = new Bookmark();
            bookmark.setArticleId(articleId);
            bookmark.setUserName(user);
            bookmark.setTimeStamp(getNowDate());
            bookmarkMapper.insert(bookmark);

            return true;
        } else {
            return false;
        }
    }


    @RequestMapping("/article/unBookmark")
    public boolean nuBookmark(String articleId, String user, String token) {
        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
            bookmarkQueryWrapper.eq("user_name", user).eq("article_id", articleId);
            bookmarkMapper.delete(bookmarkQueryWrapper);

            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/article/delete")
    public boolean delete(String articleId, String user, String token) {
        Login login = loginMapper.selectById(user);
        if (login == null) {
            return false;
        }

        if (login.getToken().equals(token) && isWithinTenMinutes(login.getTimeStamp())) {
            login.setTimeStamp(getNowDate());
            loginMapper.updateById(login);

            Article article = articleMapper.selectById(articleId);
            if (article == null) {
                return false;
            }
            if (!user.equals(article.getAuthorName())) {
                return false;
            }

//            articleMapper.deleteById(articleId);
            articleMapper.deleteArticle(articleId);
//
//
//            //删除审核记录
//            QueryWrapper<ArticleReview> articleReviewQueryWrapper = new QueryWrapper<>();
//            articleReviewQueryWrapper.eq("article_id", articleId);
//            articleReviewMapper.delete(articleReviewQueryWrapper);
//            //删除评论，评论的评论
//            QueryWrapper<ArticleComment> articleCommentQueryWrapper = new QueryWrapper<>();
//            articleCommentQueryWrapper.eq("article_id", articleId);
//            List<ArticleComment> articleComments = articleCommentMapper.selectList(articleCommentQueryWrapper);
//            for (ArticleComment articleComment : articleComments) {
//                deleteComment(articleComment.getCommentId());
//            }
//            articleCommentMapper.delete(articleCommentQueryWrapper);
//            //删除收藏
//            QueryWrapper<Bookmark> bookmarkQueryWrapper = new QueryWrapper<>();
//            bookmarkQueryWrapper.eq("article_id", articleId);
//            bookmarkMapper.delete(bookmarkQueryWrapper);
//            //删除点赞
//            QueryWrapper<LikeRecord> likeQueryWrapper = new QueryWrapper<>();
//            likeQueryWrapper.eq("article_id", articleId);
//            likeMapper.delete(likeQueryWrapper);
//            //删除阅读
//            QueryWrapper<ReadArticle> readArticleQueryWrapper = new QueryWrapper<>();
//            readArticleQueryWrapper.eq("article_id", articleId);
//            readArticleMapper.delete(readArticleQueryWrapper);


            return true;
        } else {
            return false;
        }
    }

    private void deleteComment(String commentId) {
        commentMapper.deleteById(commentId);
        QueryWrapper<CommentComment> commentCommentQueryWrapper = new QueryWrapper<>();
        commentCommentQueryWrapper.eq("commented_comment_id", commentId);
        List<CommentComment> commentComments =
            commentCommentMapper.selectList(commentCommentQueryWrapper);
        for (CommentComment commentComment : commentComments) {
            deleteComment(commentComment.getCommentCommentId());
        }
        commentCommentMapper.delete(commentCommentQueryWrapper);
    }

    @RequestMapping("/article/report")
    public void report(String articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return;
        }

        //检查是否已经被举报分配
        QueryWrapper<ArticleReview> articleReviewQueryWrapper = new QueryWrapper<>();
        articleReviewQueryWrapper.eq("article_id", articleId);
        if (articleReviewMapper.exists(articleReviewQueryWrapper)) {
            return;//已被处理，返回
        }


        //寻找合适的审核员
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("is_administrator", 1);
        List<User> auditors = userMapper.selectList(userQueryWrapper);

        List<Long> auditorWorks = new ArrayList<>();

        //已经找到所有审核员
        for (User auditor : auditors) {
            QueryWrapper<ArticleReview> articleReviewQueryWrapper1 = new QueryWrapper<>();
            articleReviewQueryWrapper1.eq("administrator_name", auditor.getUserName());
            Long work = articleReviewMapper.selectCount(articleReviewQueryWrapper1);
            auditorWorks.add(work);
        }
        Long minWork = Collections.min(auditorWorks);
        int index = auditorWorks.indexOf(minWork);
        //其中工作量最低的一个
        User auditor = auditors.get(index);


        //没被举报过，插入举报表
        ArticleReview articleReview = new ArticleReview();
        articleReview.setArticleId(articleId);
        articleReview.setTimeStamp(getNowDate());
        articleReview.setAdministratorName(auditor.getUserName());
        articleReviewMapper.insert(articleReview);
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
