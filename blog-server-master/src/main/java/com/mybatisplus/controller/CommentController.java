package com.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.Article;
import com.mybatisplus.entity.ArticleComment;
import com.mybatisplus.entity.Comment;
import com.mybatisplus.entity.CommentComment;
import com.mybatisplus.mapper.ArticleMapper;
import com.mybatisplus.mapper.CommentCommentMapper;
import com.mybatisplus.mapper.CommentMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentCommentMapper commentCommentMapper;
    @Autowired
    ArticleMapper articleMapper;

    @AllArgsConstructor
    @Data
    class GetComment {
        String user;
        String commentContent;
        List<GetComment> subCommentObjList;
    }

    @RequestMapping("/articleComment")
    public GetComment getComment(String id) {
        Comment comment = commentMapper.selectById(id);
        String user = comment.getCommenterName();
        String commentContent = comment.getCommentContent();


        QueryWrapper<CommentComment> commentCommentQueryWrapper = new QueryWrapper<>();
        commentCommentQueryWrapper.eq("commented_comment_id", id);
        List<CommentComment> commentComments = commentCommentMapper.selectList(commentCommentQueryWrapper);

        List<GetComment> subCommentObjList = new ArrayList<>();
        for (CommentComment commentComment : commentComments) {
            subCommentObjList.add(getCommentComment(commentComment.getCommentId()));
        }
//        List<GetComment> subCommentObjList = subCommentObjList;

        return new GetComment(user, commentContent, subCommentObjList);
    }

    private GetComment getCommentComment(String id) {
        Comment comment = commentMapper.selectById(id);
        String user = comment.getCommenterName();
        String commentContent = comment.getCommentContent();

//        List<GetComment> subCommentObjList = new ArrayList<>();

        return new GetComment(user, commentContent, new ArrayList<>());
    }




}
