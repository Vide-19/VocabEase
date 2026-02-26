package com.javastudy.vocabease_common.entity.query;


import java.util.List;

/**
 * 用户收藏表参数
 */
public class AppCollectQuery extends BaseParam {


	/**
	 * 收藏id
	 */
	private Integer collectId;

	private Integer nextType;

	private Integer currentId;

	/**
	 * 用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 对象id
	 */
	private String objectId;

	private String objectIdFuzzy;

	private List<String> objectIdList;

	/**
	 * 类型 0分享收藏 1单词收藏 2文章收藏 3问题收藏
	 */
	private Integer collectType;


	/**
	 * 收藏时间
	 */
	private String collectTime;

	private String collectTimeStart;

	private String collectTimeEnd;

	public void setCollectId(Integer collectId){
		this.collectId = collectId;
	}

	public Integer getCollectId(){
		return this.collectId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
	}

	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	public String getObjectId(){
		return this.objectId;
	}

	public void setObjectIdFuzzy(String objectIdFuzzy){
		this.objectIdFuzzy = objectIdFuzzy;
	}

	public String getObjectIdFuzzy(){
		return this.objectIdFuzzy;
	}

	public void setCollectType(Integer collectType){
		this.collectType = collectType;
	}

	public Integer getCollectType(){
		return this.collectType;
	}

	public void setCollectTime(String collectTime){
		this.collectTime = collectTime;
	}

	public String getCollectTime(){
		return this.collectTime;
	}

	public void setCollectTimeStart(String collectTimeStart){
		this.collectTimeStart = collectTimeStart;
	}

	public String getCollectTimeStart(){
		return this.collectTimeStart;
	}

	public void setCollectTimeEnd(String collectTimeEnd){
		this.collectTimeEnd = collectTimeEnd;
	}

	public String getCollectTimeEnd(){
		return this.collectTimeEnd;
	}

	public List<String> getObjectIdList() {
		return objectIdList;
	}

	public void setObjectIdList(List<String> objectIdList) {
		this.objectIdList = objectIdList;
	}

	public Integer getNextType() {
		return nextType;
	}

	public void setNextType(Integer nextType) {
		this.nextType = nextType;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}
}
