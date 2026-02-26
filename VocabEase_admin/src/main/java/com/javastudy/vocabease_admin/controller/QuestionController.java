package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.QuestionTypeEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.Item4questionService;
import com.javastudy.vocabease_common.service.QuestionService;
import com.javastudy.vocabease_common.utils.JsonUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题表 Controller
 */
@RestController("questionController")
@RequestMapping("/question")
public class QuestionController extends ABaseController{

	@Resource
	private QuestionService questionService;
	@Resource
	private Item4questionService item4questionService;

	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
	public ResponseVO<PaginationResultVO<Question>> loadDataList(QuestionQuery query){
		query.setOrderBy("question_id desc");
		query.setQueryAnswer(true);
		return getSuccessResponseVO(questionService.findListByPage(query));
	}
	/**
	 * 新增/修改
	 */
	@RequestMapping("/saveQuestion")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_EDIT)
	public ResponseVO<Void> saveQuestion(HttpSession session, @VerifyParam(required = true) Question question,
								String questionItemList) {
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		question.setCreaterId(sessionUserAdminDto.getUserId().toString());
		List<Item4question> item4question = new ArrayList<>();
		// 判断题填空题无需选项；其他题型必须提供选项
		if (!QuestionTypeEnum.TRUE_OR_FALSE.getType().equals(question.getQuestionType())
			|| !QuestionTypeEnum.FILL_IN_THE_BLANK.getType().equals(question.getQuestionType())) {
			if (StringTools.isEmpty(questionItemList))
				throw new BusinessException(ResponseCodeEnum.CODE_400);
			item4question = JsonUtil.convertJsonArray2Object(questionItemList, Item4question.class);
		}
		this.questionService.saveQuestion(question, item4question, sessionUserAdminDto.getSuperAdmin());
		return getSuccessResponseVO(null);
	}
	/**
	 * 加载问题选项
	 */
	@RequestMapping("/loadQuestionItem")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_EDIT)
	public ResponseVO<List<Item4question>> loadQuestionItem(@VerifyParam(required = true) Integer questionId) {
		Item4questionQuery item4questionQuery = new Item4questionQuery();
		item4questionQuery.setQuestionId(questionId);
		item4questionQuery.setOrderBy("sort asc");
		return getSuccessResponseVO(this.item4questionService.findListByParam(item4questionQuery));
	}
	/**
	 * 删除问题
	 */
	@RequestMapping("/deleteQuestion")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DELETE)
	public ResponseVO<Void> deleteQuestion(HttpSession session,
										   @VerifyParam(required = true) Integer questionId) {
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		this.questionService.deleteQuestionBatch(String.valueOf(questionId),
				sessionUserAdminDto.getSuperAdmin() ? null : sessionUserAdminDto.getUserId());
		return getSuccessResponseVO(null);
	}
	/**
	 * 批量删除问题
	 */
	@RequestMapping("/deleteQuestionBatch")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_DELETE_BATCH)
	public ResponseVO<Void> deleteQuestionBatch(@VerifyParam(required = true) String questionIds) {
		this.questionService.deleteQuestionBatch(questionIds,null);
		return getSuccessResponseVO(null);
	}
	/**
	 * 发布
	 */
	@RequestMapping("/postQuestion")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
	public ResponseVO<Void> postQuestion(@VerifyParam(required = true) String questionIds) {
		this.questionService.updateQuestionStatus(questionIds, PostStatusEnum.IS_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 下架
	 */
	@RequestMapping("/cancelPostQuestion")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_POST)
	public ResponseVO<Void> cancelPostQuestion(@VerifyParam(required = true) String questionIds) {
		this.questionService.updateQuestionStatus(questionIds, PostStatusEnum.NO_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 下一题
	 */
	@RequestMapping("/showNextQuestion")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_LIST)
	public ResponseVO<Question> showNextQuestion(QuestionQuery questionQuery,
											   @VerifyParam(required = true) Integer currentId,
												 Integer nextType){
		Question question = this.questionService.showQuestionNext(questionQuery, currentId, nextType);
		return getSuccessResponseVO(question);
	}
	/**
	 * 问题导入
	 */
	@RequestMapping("/importQuestionByExcel")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.QUESTION_IMPORT)
	public ResponseVO<List<ImportErrorItem>> importQuestionByExcel(HttpSession session, MultipartFile file){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		List<ImportErrorItem> errorItemList = this.questionService.importQuestion(sessionUserAdminDto, file);
		return getSuccessResponseVO(errorItemList);
	}
}