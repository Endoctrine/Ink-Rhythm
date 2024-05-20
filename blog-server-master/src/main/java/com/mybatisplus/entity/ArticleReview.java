package com.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName article_review
 */
@TableName(value ="article_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReview implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String articleReviewId;

    /**
     * 
     */
    private String administratorName;

    /**
     * 
     */
    private String articleId;

    /**
     * 
     */
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}