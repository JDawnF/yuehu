package com.baichen.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @Program: Article
 * @Author: baichen
 * @Description: 文章类
 * es中最基础的就是文档，文档对应数据库中的行
 * type : 文档存放的类型,对应数据库的表
 * indexName : 类型存放的索引名称，即索引库,对应数据库
 */
@Document(indexName = "yuehu_article", type = "article")
public class Article implements Serializable {
    @Id
    private String id;      //ID
    //    是否索引，就是看该域是否能被搜索，加注解
//    是否分词，就表示搜索的时候是整体匹配还是单词匹配，是的话要加注解
//    是否存储，就是是否在页面上显示，如果要显示就需要在实体类中声明属性
    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;       //标题
    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content; //文章正文
    private String state;   //审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
