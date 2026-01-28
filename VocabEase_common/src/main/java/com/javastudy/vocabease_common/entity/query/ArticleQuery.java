package com.javastudy.vocabease_common.entity.query;


/**
 * 文章表参数
 */
public class ArticleQuery extends BaseParam {


	/**
	 * 文章id
	 */
	private Integer articleId;

	private String[] articleIds;

	private Integer currentId;

	private Integer nextType;

	/**
	 * 标题
	 */
	private String title;

	private String titleFuzzy;

	/**
	 * 难度
	 */
	private Integer level;

	/**
	 * 正文
	 */
	private String body;

	private String bodyFuzzy;

	/**
	 * 翻译
	 */
	private String translation;

	private String translationFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 状态 0未发布 1已发布
	 */
	private Integer status;

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

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;

	private Boolean queryBodyContent;

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

	public void setTitleFuzzy(String titleFuzzy){
		this.titleFuzzy = titleFuzzy;
	}

	public String getTitleFuzzy(){
		return this.titleFuzzy;
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

	public void setBodyFuzzy(String bodyFuzzy){
		this.bodyFuzzy = bodyFuzzy;
	}

	public String getBodyFuzzy(){
		return this.bodyFuzzy;
	}

	public void setTranslation(String translation){
		this.translation = translation;
	}

	public String getTranslation(){
		return this.translation;
	}

	public void setTranslationFuzzy(String translationFuzzy){
		this.translationFuzzy = translationFuzzy;
	}

	public String getTranslationFuzzy(){
		return this.translationFuzzy;
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

	public Boolean getQueryBodyContent() {return queryBodyContent;}

	public void setQueryBodyContent(Boolean queryBodyContent) {this.queryBodyContent = queryBodyContent;}

	public String[] getArticleIds() {return articleIds;}

	public void setArticleIds(String[] articleIds) {
		this.articleIds = articleIds;
	}

	public Integer getCurrentId() {return currentId;}

	public void setCurrentId(Integer currentId) {this.currentId = currentId;}

	public Integer getNextType() {return nextType;}

	public void setNextType(Integer nextType) {this.nextType = nextType;}
}
