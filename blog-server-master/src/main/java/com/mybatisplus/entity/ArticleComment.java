package com.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName article_comment
 */
@TableName(value ="article_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String articleCommentId;

    /**
     * 
     */
    private String articleId;

    /**
     * 
     */
    private String commentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}