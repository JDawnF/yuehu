package com.baichen.base.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Program: Label
 * @Author: baichen
 * @Description: 标签实体类
 */
@Entity
@Table(name = "tb_label")
public class Label implements Serializable {
    @Id
    private String id; //标签ID（主键）
    @Column(name = "labelname")     // 如果与数据库表中的名称不一致时，要指定对应的列名，请求时的json中也要改为labelName
    private String labelName;//标签名称
    private String state;//状态 0：无效  1：有效
    private Integer count;//数量
    private String recommend;//是否推荐 0：不推荐  1：推荐
    private Integer fans;//粉丝数

    public Label() {
    }

    public Label(String id, String labelName, String state, Integer count, String recommend, Integer fans) {
        this.id = id;
        this.labelName = labelName;
        this.state = state;
        this.count = count;
        this.recommend = recommend;
        this.fans = fans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }
}
