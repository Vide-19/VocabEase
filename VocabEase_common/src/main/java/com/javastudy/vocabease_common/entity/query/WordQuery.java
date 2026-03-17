package com.javastudy.vocabease_common.entity.query;

/**
 * 单词表参数
 */
public class WordQuery extends BaseParam {


	/**
	 * 单词ID
	 */
	private Integer wordId;

	private String[] wordIds;

	private Integer currentId;

	private Integer nextType;

	/**
	 * 单词本身（英文）
	 */
	private String word;

	private String wordFuzzy;

	/**
	 * 音标（如 /ˈæpl/）
	 */
	private String phonetic;

	private String phoneticFuzzy;

	/**
	 * 词性（n./v./adj.）
	 */
	private String partOfSpeech;

	private String partOfSpeechFuzzy;

	/**
	 * 中文释义（主释义，可存JSON或分表）
	 */
	private String definition;

	private String definitionFuzzy;

	/**
	 * 例句（英文 + 中文，可用分隔符或JSON）
	 */
	private String exampleSentence;

	private String exampleSentenceFuzzy;

	/**
	 * 发音音频URL（可为空）
	 */
	private String audioUrl;

	private String audioUrlFuzzy;

	/**
	 * 配图URL（用于视觉记忆）
	 */
	private String imageUrl;

	private String imageUrlFuzzy;

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

	private String creatorIdFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 更新时间
	 */
	private String updateTime;

	private String updateTimeStart;

	private String updateTimeEnd;

	/**
	 * 发布类型 0内部 1外部
	 */
	private Integer postType;

	private Integer categoryId;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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

	public void setWordFuzzy(String wordFuzzy){
		this.wordFuzzy = wordFuzzy;
	}

	public String getWordFuzzy(){
		return this.wordFuzzy;
	}

	public void setPhonetic(String phonetic){
		this.phonetic = phonetic;
	}

	public String getPhonetic(){
		return this.phonetic;
	}

	public void setPhoneticFuzzy(String phoneticFuzzy){
		this.phoneticFuzzy = phoneticFuzzy;
	}

	public String getPhoneticFuzzy(){
		return this.phoneticFuzzy;
	}

	public void setPartOfSpeech(String partOfSpeech){
		this.partOfSpeech = partOfSpeech;
	}

	public String getPartOfSpeech(){
		return this.partOfSpeech;
	}

	public void setPartOfSpeechFuzzy(String partOfSpeechFuzzy){
		this.partOfSpeechFuzzy = partOfSpeechFuzzy;
	}

	public String getPartOfSpeechFuzzy(){
		return this.partOfSpeechFuzzy;
	}

	public void setDefinition(String definition){
		this.definition = definition;
	}

	public String getDefinition(){
		return this.definition;
	}

	public void setDefinitionFuzzy(String definitionFuzzy){
		this.definitionFuzzy = definitionFuzzy;
	}

	public String getDefinitionFuzzy(){
		return this.definitionFuzzy;
	}

	public void setExampleSentence(String exampleSentence){
		this.exampleSentence = exampleSentence;
	}

	public String getExampleSentence(){
		return this.exampleSentence;
	}

	public void setExampleSentenceFuzzy(String exampleSentenceFuzzy){
		this.exampleSentenceFuzzy = exampleSentenceFuzzy;
	}

	public String getExampleSentenceFuzzy(){
		return this.exampleSentenceFuzzy;
	}

	public void setAudioUrl(String audioUrl){
		this.audioUrl = audioUrl;
	}

	public String getAudioUrl(){
		return this.audioUrl;
	}

	public void setAudioUrlFuzzy(String audioUrlFuzzy){
		this.audioUrlFuzzy = audioUrlFuzzy;
	}

	public String getAudioUrlFuzzy(){
		return this.audioUrlFuzzy;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return this.imageUrl;
	}

	public void setImageUrlFuzzy(String imageUrlFuzzy){
		this.imageUrlFuzzy = imageUrlFuzzy;
	}

	public String getImageUrlFuzzy(){
		return this.imageUrlFuzzy;
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

	public void setCreatorIdFuzzy(String creatorIdFuzzy){
		this.creatorIdFuzzy = creatorIdFuzzy;
	}

	public String getCreatorIdFuzzy(){
		return this.creatorIdFuzzy;
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

	public void setUpdateTime(String updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateTime(){
		return this.updateTime;
	}

	public void setUpdateTimeStart(String updateTimeStart){
		this.updateTimeStart = updateTimeStart;
	}

	public String getUpdateTimeStart(){
		return this.updateTimeStart;
	}
	public void setUpdateTimeEnd(String updateTimeEnd){
		this.updateTimeEnd = updateTimeEnd;
	}

	public String getUpdateTimeEnd(){
		return this.updateTimeEnd;
	}

	public void setPostType(Integer postType){
		this.postType = postType;
	}

	public Integer getPostType(){
		return this.postType;
	}

	public String[] getWordIds() {return wordIds;}

	public void setWordIds(String[] wordIds) {this.wordIds = wordIds;}

	public Integer getCurrentId() {return currentId;}

	public void setCurrentId(Integer currentId) {this.currentId = currentId;}

	public Integer getNextType() {return nextType;}

	public void setNextType(Integer nextType) {this.nextType = nextType;}
}
