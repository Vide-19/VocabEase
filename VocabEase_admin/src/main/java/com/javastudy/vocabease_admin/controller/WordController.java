package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.WordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 单词表 Controller
 */
@RestController("wordController")
@RequestMapping("/word")
public class WordController extends ABaseController{

	@Resource
	private WordService wordService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_LIST)
	public ResponseVO loadDataList(WordQuery query){
		query.setOrderBy("word_id desc");
		return getSuccessResponseVO(wordService.findListByPage(query));
	}
	/**
	 * 新增单词
	 */
	@RequestMapping("/saveWord")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_EDIT)
	public ResponseVO<Void> saveWord(HttpSession session, Word word) {
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		word.setCreatorId(sessionUserAdminDto.getUserId().toString());
		wordService.saveWord(word, sessionUserAdminDto.getSuperAdmin());
		return getSuccessResponseVO(null);
	}
	/**
	 * 删除单词
	 */
	@RequestMapping("/deleteWord")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_DELETE)
	public ResponseVO<Void> deleteWord(HttpSession session,
										  @VerifyParam(required = true) Integer wordId){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		this.wordService.deleteWordByWordIds(String.valueOf(wordId), sessionUserAdminDto.getSuperAdmin()?null:sessionUserAdminDto.getUserId());
		return getSuccessResponseVO(null);
	}
	/**
	 * 批量删除单词
	 */
	@RequestMapping("/deleteWordBatch")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_DELETE_BATCH)
	public ResponseVO<Void> deleteWordBatch(@VerifyParam(required = true) String wordIds){
		this.wordService.deleteWordByWordIds(wordIds, null);
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章发布
	 */
	@RequestMapping("/postWord")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_POST)
	public ResponseVO<Void> postWord(@VerifyParam(required = true) String wordIds, SessionStatus sessionStatus){
		this.wordService.updateWordStatus(wordIds, PostStatusEnum.IS_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章下架
	 */
	@RequestMapping("/cancelPostWord")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_POST)
	public ResponseVO<Void> cancelPostWord(@VerifyParam(required = true) String wordIds,
											  SessionStatus sessionStatus){
		this.wordService.updateWordStatus(wordIds, PostStatusEnum.NO_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章导入
	 */
	@RequestMapping("/importWordByExcel")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_IMPORT)
	public ResponseVO importWordByExcel(HttpSession session, MultipartFile file){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		List<ImportErrorItem> errorItemList = this.wordService.importWord(sessionUserAdminDto, file);
		return getSuccessResponseVO(errorItemList);
	}
	/**
	 * 下一篇
	 */
	@RequestMapping("/showNextWord")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.WORD_LIST)
	public ResponseVO<Word> showNextWord(WordQuery wordQuery,
											   @VerifyParam(required = true) Integer currentId, Integer nextType){
		Word word = this.wordService.showWordNext(wordQuery, currentId, nextType);
		return getSuccessResponseVO(word);
	}
}