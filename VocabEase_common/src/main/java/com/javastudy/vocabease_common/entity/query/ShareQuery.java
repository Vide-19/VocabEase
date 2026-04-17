package com.javastudy.vocabease_common.entity.query;

import java.util.List;

/**
 * 笔记表参数
 */
public class ShareQuery extends BaseParam {


	/**
	 * 笔记id
	 */
	private Integer shareId;

	private String[] shareIds;

	private Integer currentId;

	private Integer nextType;

	private String userId;

	/**
	 * 标题
	 */
	private String title;

	private String titleFuzzy;

	/**
	 * 内容
	 */
	private String content;

	private String contentFuzzy;

	private Boolean isQueryContent;

	/**
	 * 笔记类别 0无封面 1横幅 2小图标
	 */
	private Integer coverType;

	/**
	 * 封面路径
	 */
	private String coverPath;

	private String coverPathFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 状态 0未发布 1已发布 2置顶
	 */
	private Integer status;

	private List<Integer> statusList;

	/**
	 * 作者id
	 */
	private String createrId;

	private String createrIdFuzzy;

	/**
	 * 浏览量
	 */
	private Integer readCount;

	/**
	 * 收藏数
	 */
	private Integer collectCount;

	private Integer collectType;

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;

	private Integer relateId;

	private Integer relateType;

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	public Integer getCollectType() {
		return collectType;
	}

	public void setCollectType(Integer collectType) {
		this.collectType = collectType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getRelateId() {
		return relateId;
	}

	public void setRelateId(Integer relateId) {
		this.relateId = relateId;
	}

	public Integer getRelateType() {
		return relateType;
	}

	public void setRelateType(Integer relateType) {
		this.relateType = relateType;
	}

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

	public void setTitleFuzzy(String titleFuzzy){
		this.titleFuzzy = titleFuzzy;
	}

	public String getTitleFuzzy(){
		return this.titleFuzzy;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setContentFuzzy(String contentFuzzy){
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy(){
		return this.contentFuzzy;
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

	public void setCoverPathFuzzy(String coverPathFuzzy){
		this.coverPathFuzzy = coverPathFuzzy;
	}

	public String getCoverPathFuzzy(){
		return this.coverPathFuzzy;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return this.createTime;
	}

	public void setCreateTimeStart(String createTimeStart){
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart(){
		return this.createTimeStart;
	}
	public void setCreateTimeEnd(String createTimeEnd){
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd(){
		return this.createTimeEnd;
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

	public void setCreaterIdFuzzy(String createrIdFuzzy){
		this.createrIdFuzzy = createrIdFuzzy;
	}

	public String getCreaterIdFuzzy(){
		return this.createrIdFuzzy;
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

	public Boolean getQueryContent() {
		return isQueryContent;
	}

	public void setQueryContent(Boolean queryContent) {
		isQueryContent = queryContent;
	}

	public String[] getShareIds() {
		return shareIds;
	}

	public void setShareIds(String[] shareIds) {
		this.shareIds = shareIds;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public Integer getNextType() {
		return nextType;
	}

	public void setNextType(Integer nextType) {
		this.nextType = nextType;
	}
}
