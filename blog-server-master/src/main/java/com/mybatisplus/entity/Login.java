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
 * @TableName login
 */
@TableName(value ="login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable {
    /**
     * 
     */
    @TableId
    private String userName;

    /**
     * 
     */
    private String token;

    /**
     * 
     */
    private Date timeStamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}