package com.mybatisplus.mapper;

import com.mybatisplus.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

/**
* @author 11
* @description 针对表【article】的数据库操作Mapper
* @createDate 2023-12-21 17:44:34
* @Entity com.mybatisplus.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("CALL delete_article_with_related_data(#{article_id, mode=IN, jdbcType=NVARCHAR})")
    void deleteArticle(@Param("article_id") String article_id);
}




