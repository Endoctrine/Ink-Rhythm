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
 * @TableName follow
 */
@TableName(value ="follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String followId;

    /**
     * 
     */
    private String followedName;

    /**
     * 
     */
    private String followerName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}