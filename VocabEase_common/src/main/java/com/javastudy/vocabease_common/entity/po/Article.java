package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 文章表
 */
public class Article implements Serializable {


	@Serial
	private static final long serialVersionUID = -2588439492124489337L;
	/**
	 * 文章id
	 */
	private Integer articleId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 难度
	 */
	private Integer level;

	/**
	 * 正文
	 */
	private String body;

	/**
	 * 翻译
	 */
	private String translation;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 状态 0未发布 1已发布
	 */
	private Integer status;

	/**
	 * 作者id
	 */
	private Integer createrId;

	/**
	 * 浏览量
	 */
	private Integer readCount;

	/**
	 * 收藏数
	 */
	private Integer collectCount;

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;


	public void setArticleId(Integer articleId){
		this.articleId = articleId;
	}

	public Integer getArticleId(){
		return this.articleId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return this.title;
	}

	public void setLevel(Integer level){
		this.level = level;
	}

	public Integer getLevel(){
		return this.level;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return this.body;
	}

	public void setTranslation(String translation){
		this.translation = translation;
	}

	public String getTranslation(){
		return this.translation;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreaterId(Integer createrId){
		this.createrId = createrId;
	}

	public Integer getCreaterId(){return this.createrId;}

	public void setReadCount(Integer readCount){
		this.readCount = readCount;
	}

	public Integer getReadCount(){
		return this.readCount;
	}

	public void setCollectCount(Integer collectCount){
		this.collectCount = collectCount;
	}

	public Integer getCollectCount(){
		return this.collectCount;
	}

	public void setPostType(Integer postType){
		this.postType = postType;
	}

	public Integer getPostType(){
		return this.postType;
	}

	@Override
	public String toString (){
		return "文章id:"+(articleId == null ? "空" : articleId)+"，标题:"+(title == null ? "空" : title)+"，难度:"+(level == null ? "空" : level)+"，正文:"+(body == null ? "空" : body)+"，翻译:"+(translation == null ? "空" : translation)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，状态 0未发布 1已发布:"+(status == null ? "空" : status)+"，作者id:"+(createrId == null ? "空" : createrId)+"，浏览量:"+(readCount == null ? "空" : readCount)+"，收藏数:"+(collectCount == null ? "空" : collectCount)+"，发布类型 0内部 1外部:"+(postType == null ? "空" : postType);
	}
}
