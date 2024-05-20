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
 * @TableName article
 */
@TableName(value ="article")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String articleId;

    /**
     * 
     */
    private String articleTitle;

    /**
     * 
     */
    private String articleContent;

    /**
     * 
     */
    private String authorName;

    /**
     * 
     */
    private Integer isAnnouncement;

    /**
     * 
     */
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}