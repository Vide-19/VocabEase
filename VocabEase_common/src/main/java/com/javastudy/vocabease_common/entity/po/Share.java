package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 笔记表
 */
public class Share implements Serializable {


	@Serial
	private static final long serialVersionUID = 1419545595435022457L;
	/**
	 * 笔记id
	 */
	private Integer shareId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 笔记类别 0无封面 1横幅 2小图标
	 */
	private Integer coverType;

	/**
	 * 封面路径
	 */
	private String coverPath;

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
	private String createrId;

	/**
	 * 浏览量
	 */
	private Integer readCount;

	/**
	 * 收藏数
	 */
	private Integer collectCount;

	private Boolean isCollect;

	private Integer collectId;

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;


	public void setShareId(Integer shareId){
		this.shareId = shareId;
	}

	public Integer getShareId(){
		return this.shareId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return this.title;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setCoverType(Integer coverType){
		this.coverType = coverType;
	}

	public Integer getCoverType(){
		return this.coverType;
	}

	public void setCoverPath(String coverPath){
		this.coverPath = coverPath;
	}

	public String getCoverPath(){
		return this.coverPath;
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

	public void setCreaterId(String createrId){
		this.createrId = createrId;
	}

	public String getCreaterId(){
		return this.createrId;
	}

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

	public Boolean getCollect() {
		return isCollect;
	}

	public void setCollect(Boolean collect) {
		isCollect = collect;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	@Override
	public String toString (){
		return "笔记id:"+(shareId == null ? "空" : shareId)+"，标题:"+(title == null ? "空" : title)+"，内容:"+(content == null ? "空" : content)+"，笔记类别 0无封面 1横幅 2小图标:"+(coverType == null ? "空" : coverType)+"，封面路径:"+(coverPath == null ? "空" : coverPath)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，状态 0未发布 1已发布:"+(status == null ? "空" : status)+"，作者id:"+(createrId == null ? "空" : createrId)+"，浏览量:"+(readCount == null ? "空" : readCount)+"，收藏数:"+(collectCount == null ? "空" : collectCount)+"，发布类型 0内部 1外部:"+(postType == null ? "空" : postType);
	}
}
