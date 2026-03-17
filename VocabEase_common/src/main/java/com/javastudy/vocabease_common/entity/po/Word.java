package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 单词表
 */
public class Word implements Serializable {

	@Serial
	private static final long serialVersionUID = -4699624100947003708L;
	/**
	 * 单词ID
	 */
	private Integer wordId;

	/**
	 * 单词本身（英文）
	 */
	private String word;

	/**
	 * 音标（如 /ˈæpl/）
	 */
	private String phonetic;

	/**
	 * 词性（n./v./adj.）
	 */
	private String partOfSpeech;

	/**
	 * 中文释义（主释义，可存JSON或分表）
	 */
	private String definition;

	/**
	 * 例句（英文 + 中文，可用分隔符或JSON）
	 */
	private String exampleSentence;

	/**
	 * 发音音频URL（可为空）
	 */
	private String audioUrl;

	/**
	 * 配图URL（用于视觉记忆）
	 */
	private String imageUrl;

	/**
	 * 难度
	 */
	private Integer level;

	/**
	 * 状态：0-禁用，1-启用
	 */
	private Integer status;

	/**
	 * 创建者ID（管理员）
	 */
	private String creatorId;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;

	private Boolean isCollect;

	private Integer collectId;

	private String categoryName;

	private Integer categoryId;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setWordId(Integer wordId){
		this.wordId = wordId;
	}

	public Integer getWordId(){
		return this.wordId;
	}

	public void setWord(String word){
		this.word = word;
	}

	public String getWord(){
		return this.word;
	}

	public void setPhonetic(String phonetic){
		this.phonetic = phonetic;
	}

	public String getPhonetic(){
		return this.phonetic;
	}

	public void setPartOfSpeech(String partOfSpeech){
		this.partOfSpeech = partOfSpeech;
	}

	public String getPartOfSpeech(){
		return this.partOfSpeech;
	}

	public void setDefinition(String definition){
		this.definition = definition;
	}

	public String getDefinition(){
		return this.definition;
	}

	public void setExampleSentence(String exampleSentence){
		this.exampleSentence = exampleSentence;
	}

	public String getExampleSentence(){
		return this.exampleSentence;
	}

	public void setAudioUrl(String audioUrl){
		this.audioUrl = audioUrl;
	}

	public String getAudioUrl(){
		return this.audioUrl;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return this.imageUrl;
	}

	public void setLevel(Integer level){
		this.level = level;
	}

	public Integer getLevel(){
		return this.level;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreatorId(String creatorId){
		this.creatorId = creatorId;
	}

	public String getCreatorId(){
		return this.creatorId;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
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
		return "单词ID:"+(wordId == null ? "空" : wordId)+"，单词本身（英文）:"+(word == null ? "空" : word)+"，音标（如 /ˈæpl/）:"+(phonetic == null ? "空" : phonetic)+"，词性（n./v./adj.）:"+(partOfSpeech == null ? "空" : partOfSpeech)+"，中文释义（主释义，可存JSON或分表）:"+(definition == null ? "空" : definition)+"，例句（英文 + 中文，可用分隔符或JSON）:"+(exampleSentence == null ? "空" : exampleSentence)+"，发音音频URL（可为空）:"+(audioUrl == null ? "空" : audioUrl)+"，配图URL（用于视觉记忆）:"+(imageUrl == null ? "空" : imageUrl)+"，难度:"+(level == null ? "空" : level)+"，状态：0-禁用，1-启用:"+(status == null ? "空" : status)+"，创建者ID（管理员）:"+(creatorId == null ? "空" : creatorId)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，更新时间:"+(updateTime == null ? "空" : DateUtil.format(updateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，发布类型 0内部 1外部:"+(postType == null ? "空" : postType);
	}
}
