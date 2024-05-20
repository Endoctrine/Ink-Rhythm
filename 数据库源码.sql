create database blog;
use blog;

create table blog.article
(
    article_id      varchar(32) charset utf8mb3 not null
        primary key,
    article_title   varchar(64) charset utf8mb3 null,
    article_content mediumtext                  null,
    author_name     varchar(64) charset utf8mb3 null,
    is_announcement tinyint(1)                  null,
    time_stamp      timestamp                   null
);

create table blog.article_comment
(
    article_comment_id varchar(128) charset utf8mb3 not null
        primary key,
    article_id         varchar(32) charset utf8mb3  not null,
    comment_id         varchar(32) charset utf8mb3  not null
);

create definer = root@localhost trigger blog.before_delete_article_comment
    before delete
    on blog.article_comment
    for each row
BEGIN
    -- 调用存储过程2，传递 comment_id 参数
    CALL delete_comment_and_related_comment(OLD.comment_id);
END;

create table blog.article_review
(
    article_review_id  varchar(128) charset utf8mb3 not null
        primary key,
    administrator_name varchar(64) charset utf8mb3  not null,
    article_id         varchar(32) charset utf8mb3  not null,
    time_stamp         timestamp                    null
);

create table blog.bookmark
(
    bookmark_id varchar(128) charset utf8mb3 not null
        primary key,
    user_name   varchar(64) charset utf8mb3  not null,
    article_id  varchar(32) charset utf8mb3  not null,
    time_stamp  timestamp                    null
);

create table blog.comment
(
    comment_id      varchar(32) charset utf8mb3  not null
        primary key,
    comment_content varchar(512) charset utf8mb3 null,
    commenter_name  varchar(64) charset utf8mb3  null,
    time_stamp      timestamp                    null
);

create table blog.comment_comment
(
    comment_comment_id   varchar(128) charset utf8mb3 not null
        primary key,
    commented_comment_id varchar(32) charset utf8mb3  not null,
    comment_id           varchar(32) charset utf8mb3  not null
);

create definer = root@localhost trigger blog.before_delete_comment_comment
    before delete
    on blog.comment_comment
    for each row
BEGIN
    -- 调用存储过程2，传递 comment_id 参数
    CALL delete_comment(OLD.comment_id);
END;

create table blog.follow
(
    follow_id     varchar(128) charset utf8mb3 not null
        primary key,
    followed_name varchar(64) charset utf8mb3  not null,
    follower_name varchar(64) charset utf8mb3  not null
);

create table blog.like_record
(
    like_id    varchar(128) charset utf8mb3 not null
        primary key,
    liker_name varchar(64) charset utf8mb3  not null,
    article_id varchar(32) charset utf8mb3  not null,
    time_stamp timestamp                    null
);

create table blog.login
(
    user_name  varchar(64) charset utf8mb3 not null
        primary key,
    token      varchar(64) charset utf8mb3 null,
    time_stamp timestamp                   null
);

create table blog.read_article
(
    read_article_id varchar(128) charset utf8mb3 not null
        primary key,
    reader_name     varchar(64) charset utf8mb3  not null,
    article_id      varchar(32) charset utf8mb3  not null,
    time_stamp      timestamp                    null
);

create table blog.user
(
    user_name        varchar(64) charset utf8mb3   not null
        primary key,
    user_password    varchar(64) charset utf8mb3 check ( length(user_password) = 64 ),
    is_administrator tinyint(1)                    null,
    user_signature   varchar(64) charset utf8mb3   null,
    user_avatar      varchar(1024) charset utf8mb3 null
);

create table blog.user_review
(
    user_review_id     varchar(128) charset utf8mb3 not null
        primary key,
    administrator_name varchar(64) charset utf8mb3  not null,
    applicant_name     varchar(64) charset utf8mb3  not null,
    time_stamp         timestamp                    null
);

create
    definer = root@localhost procedure blog.delete_article_with_related_data(IN p_article_id varchar(32))
BEGIN
    -- 删除文章评论记录
    DELETE FROM article_comment WHERE article_id = p_article_id;

    -- 删除文章点赞记录
    DELETE FROM like_record WHERE article_id = p_article_id;

    -- 删除文章收藏记录
    DELETE FROM bookmark WHERE article_id = p_article_id;

    -- 删除文章记录
    DELETE FROM article WHERE article_id = p_article_id;

    -- 删除审核记录
    DELETE FROM article_review WHERE article_id = p_article_id;

    -- 删除阅读记录
    DELETE FROM read_article WHERE article_id = p_article_id;
    -- 如果需要返回成功标志，你可以使用下面的语句
    -- SELECT ROW_COUNT() > 0 AS success_flag;
END;

create
    definer = root@localhost procedure blog.delete_comment(IN p_comment_id varchar(32))
BEGIN
    -- 删除评论
    DELETE FROM comment WHERE comment_id = p_comment_id;

    -- 如果需要返回成功标志，你可以使用下面的语句
    -- SELECT ROW_COUNT() > 0 AS success_flag;

END;

create
    definer = root@localhost procedure blog.delete_comment_and_related_comment(IN p_comment_id varchar(32))
BEGIN
    -- 删除评论的评论记录
    DELETE FROM comment_comment WHERE commented_comment_id = p_comment_id;

    -- 删除评论
    DELETE FROM comment WHERE comment_id = p_comment_id;

    -- 如果需要返回成功标志，你可以使用下面的语句
    -- SELECT ROW_COUNT() > 0 AS success_flag;

END;

INSERT INTO user(user_name, user_password, is_administrator, user_signature, user_avatar)
values ('Admin',
        '472c0cfedafb178378227ecf8c75381751c60ce087af7b0bed776367adae6a94',
        TRUE,
        ':)',
        'http://localhost:3000/images/default.png');