package com.javastudy.vocabease_common.entity.query;

/**
 * 用户错题本表参数
 */
public class AppWrongWordQuery extends BaseParam {


	/**
	 * 
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 单词ID
	 */
	private Integer wordId;

	/**
	 * 错误次数
	 */
	private Integer wrongCount;

	/**
	 * 最后错误时间
	 */
	private String lastWrongTime;

	private String lastWrongTimeStart;

	private String lastWrongTimeEnd;

	/**
	 * 
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 最后错误时间
	 */
	private String lastReviewTime;

	/**
	 * 状态：0-禁用，1-启用
	 */
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLastReviewTime() {
		return lastReviewTime;
	}

	public void setLastReviewTime(String lastReviewTime) {
		this.lastReviewTime = lastReviewTime;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
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

	public void setWordId(Integer wordId){
		this.wordId = wordId;
	}

	public Integer getWordId(){
		return this.wordId;
	}

	public void setWrongCount(Integer wrongCount){
		this.wrongCount = wrongCount;
	}

	public Integer getWrongCount(){
		return this.wrongCount;
	}

	public void setLastWrongTime(String lastWrongTime){
		this.lastWrongTime = lastWrongTime;
	}

	public String getLastWrongTime(){
		return this.lastWrongTime;
	}

	public void setLastWrongTimeStart(String lastWrongTimeStart){
		this.lastWrongTimeStart = lastWrongTimeStart;
	}

	public String getLastWrongTimeStart(){
		return this.lastWrongTimeStart;
	}
	public void setLastWrongTimeEnd(String lastWrongTimeEnd){
		this.lastWrongTimeEnd = lastWrongTimeEnd;
	}

	public String getLastWrongTimeEnd(){
		return this.lastWrongTimeEnd;
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

}
