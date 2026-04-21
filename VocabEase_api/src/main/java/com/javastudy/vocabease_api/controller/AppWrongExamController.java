package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppWrongExam;
import com.javastudy.vocabease_common.entity.query.AppWrongExamQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppWrongExamService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 我的错题本表 Controller
 */
@RestController("appWrongExamController")
@RequestMapping("/appWrongExam")
public class AppWrongExamController extends ABaseController{

	@Resource
	private AppWrongExamService appWrongExamService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadWrongQuestion")
	public ResponseVO<List<AppWrongExam>> loadDataList(@RequestHeader(value = "token", required = false) String token){
		AppAccountDto dto = getTokenUserAdminDto(token);
		if (dto == null)
			return getSuccessResponseVO(null);
		AppWrongExamQuery query = new AppWrongExamQuery();
		query.setUserId(dto.getUserId());
		query.setOrderBy("exam_id desc");
		List<AppWrongExam> examList = this.appWrongExamService.findListByParam(query);
		return getSuccessResponseVO(examList);
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO<Void> add(AppWrongExam bean) {
		appWrongExamService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO<Void> addBatch(@RequestBody List<AppWrongExam> listBean) {
		appWrongExamService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO<Void> addOrUpdateBatch(@RequestBody List<AppWrongExam> listBean) {
		appWrongExamService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Id查询对象
	 */
	@RequestMapping("/getAppWrongExamById")
	public ResponseVO<AppWrongExam> getAppWrongExamById(Integer id) {
		return getSuccessResponseVO(appWrongExamService.getAppWrongExamById(id));
	}

	/**
	 * 根据Id修改对象
	 */
	@RequestMapping("/updateAppWrongExamById")
	public ResponseVO<Void> updateAppWrongExamById(AppWrongExam bean,Integer id) {
		appWrongExamService.updateAppWrongExamById(bean,id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Id删除
	 */
	@RequestMapping("/deleteAppWrongExamById")
	public ResponseVO<Void> deleteAppWrongExamById(@RequestHeader(value = "token", required = false) String token,
												   @VerifyParam(required = true) String id) {
		AppAccountDto dto = getTokenUserAdminDto(token);
		if (dto == null)
			return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401),null);
		this.appWrongExamService.deleteAppWrongExamById(id);
		return getSuccessResponseVO(null);
	}
}