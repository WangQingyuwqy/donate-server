package org.jeecg.modules.tasks.smartVerifyTask.controller;

import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import net.sf.saxon.expr.Component;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.tasks.smartVerifyDetail.entity.SmartVerifyDetail;
import org.jeecg.modules.tasks.smartVerifyDetail.service.ISmartVerifyDetailService;
import org.jeecg.modules.tasks.smartVerifyTask.entity.SmartVerifyTask;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.SmartVerifyTaskMapper;
import org.jeecg.modules.tasks.smartVerifyTask.mapper.VerifyTaskListPageMapper;
import org.jeecg.modules.tasks.smartVerifyTask.service.ISmartVerifyTaskService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.tasks.smartVerifyTask.vo.VerifyTaskListPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 审核任务表
 * @Author: jeecg-boot
 * @Date:   2021-11-04
 * @Version: V1.0
 */
@Api(tags="审核任务表")
@RestController
@RequestMapping("/tasks/smartVerifyTask")
@Slf4j
public class SmartVerifyTaskController extends JeecgController<SmartVerifyTask, ISmartVerifyTaskService> {
	@Autowired
	private ISmartVerifyTaskService smartVerifyTaskService;

	@Resource
	private SmartVerifyTaskMapper smartVerifyTaskMapper;

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Resource
	private VerifyTaskListPageMapper verifyTaskListPageMapper;

	@Autowired
	private ISmartVerifyDetailService smartVerifyDetailService;



	
	/**
	 * 分页列表查询
	 *
	 * @param verifyTaskListPage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核任务表-分页列表查询")
	@ApiOperation(value="审核任务表-分页列表查询", notes="审核任务表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VerifyTaskListPage verifyTaskListPage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

//		log.info(sysUser.getDepartIds());
		String userDepartId = sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode());
		log.info("这里"+userDepartId);

		MPJLambdaWrapper<VerifyTaskListPage> mpjLambdaWrapper = new MPJLambdaWrapper<VerifyTaskListPage>();
		mpjLambdaWrapper.selectAll(SmartVerifyTask.class)
				.select(SmartVerifyDetail::getFlowNo,SmartVerifyDetail::getAuditDepart,SmartVerifyDetail::getAuditPerson,
						SmartVerifyDetail::getAuditStatus, SmartVerifyDetail::getAuditTime, SmartVerifyDetail::getRemark)
				.innerJoin(SmartVerifyDetail.class, SmartVerifyDetail::getFlowNo, SmartVerifyTask::getFlowNo)
				.eq(SmartVerifyDetail::getAuditStatus, 2)
				.eq(SmartVerifyDetail::getAuditDepart, userDepartId)
				.orderByDesc(SmartVerifyTask::getCreateTime);

		Page<VerifyTaskListPage> page = new Page<VerifyTaskListPage>(pageNo, pageSize);
		IPage<VerifyTaskListPage> pageList = smartVerifyTaskMapper.selectJoinPage(page, VerifyTaskListPage.class,
				mpjLambdaWrapper);


		// log.info(sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode()));

		return Result.OK(pageList);
	}

	 @AutoLog(value = "审核任务表-分页列表查询")
	 @ApiOperation(value="审核任务表-分页列表查询", notes="审核任务表-分页列表查询")
	 @GetMapping(value = "/myTaskList")
	 public Result<?> queryMyTaskPageList(SmartVerifyTask smartVerifyTask,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String userName = sysUser.getRealname();
//		log.info(sysUser.getDepartIds());
//		 String userDepartId = sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode());
		 QueryWrapper<SmartVerifyTask> queryWrapper = QueryGenerator.initQueryWrapper(smartVerifyTask, req.getParameterMap());
		 queryWrapper.eq("fill_person",userName);
		 queryWrapper.orderByDesc("create_time");

		 Page<SmartVerifyTask> page = new Page<>(pageNo, pageSize);
		 IPage<SmartVerifyTask> pageList = smartVerifyTaskService.page(page,queryWrapper);

		 // log.info(sysBaseAPI.getDepartIdsByOrgCode(sysUser.getOrgCode()));
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param smartVerifyTask
	 * @return
	 */
	@AutoLog(value = "审核任务表-添加")
	@ApiOperation(value="审核任务表-添加", notes="审核任务表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVerifyTask smartVerifyTask) {
		smartVerifyTaskService.save(smartVerifyTask);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVerifyTask
	 * @return
	 */
	@AutoLog(value = "审核任务表-编辑")
	@ApiOperation(value="审核任务表-编辑", notes="审核任务表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVerifyTask smartVerifyTask) {
		smartVerifyTaskService.updateById(smartVerifyTask);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务表-通过id删除")
	@ApiOperation(value="审核任务表-通过id删除", notes="审核任务表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartVerifyTaskService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核任务表-批量删除")
	@ApiOperation(value="审核任务表-批量删除", notes="审核任务表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVerifyTaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核任务表-通过id查询")
	@ApiOperation(value="审核任务表-通过id查询", notes="审核任务表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVerifyTask smartVerifyTask = smartVerifyTaskService.getById(id);
		if(smartVerifyTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVerifyTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartVerifyTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVerifyTask smartVerifyTask) {
        return super.exportXls(request, smartVerifyTask, SmartVerifyTask.class, "审核任务表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartVerifyTask.class);
    }

	@AutoLog(value = "审核任务表-编辑")
	@ApiOperation(value="审核任务表-编辑", notes="审核任务表-编辑")
	@PutMapping(value = "/updateStatus")
	public Result<?> updateStatus(@RequestBody SmartVerifyDetail smartVerifyDetail) {
		String flowNo = smartVerifyDetail.getFlowNo();
	 log.info(String.valueOf(smartVerifyDetail));
	 // 获取当前审批用户id及部门，并设置到审批详表中
	 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
	 String userName = sysUser.getUsername();
	 smartVerifyDetail.setAuditPerson(sysUser.getRealname());
	 String orgCode = sysUser.getOrgCode();
	 String userDepartId = sysBaseAPI.getDepartIdsByOrgCode(orgCode);
	 log.info(userDepartId);
	 // 根据flowNo和depart查询到此记录
	 QueryWrapper<SmartVerifyDetail> queryWrapper = new QueryWrapper<>();
	 queryWrapper.eq("flow_no",smartVerifyDetail.getFlowNo())
			 .eq("audit_depart",userDepartId);
	 // 更新审核人审核状态
	 smartVerifyDetailService.update(smartVerifyDetail,queryWrapper);
	 // 在审核主表中找到记录
	 QueryWrapper<SmartVerifyTask> queryWrapper2 = new QueryWrapper<>();
	 queryWrapper2.eq("flow_no",smartVerifyDetail.getFlowNo());
	 // 查找出填报记录的用户名和任务类型
	 String fillPersonUserName = smartVerifyTaskService.getOne(queryWrapper2).getCreateBy();
	 String fillPersonUserPhone = sysBaseAPI.getUserByName(fillPersonUserName).getPhone();
	 String taskType = smartVerifyTaskService.getOne(queryWrapper2).getTaskType();
	 // 如果审核人给了通过
	 if(smartVerifyDetail.getAuditStatus() == 3){
		 // 根据flowNo查询
		 QueryWrapper<SmartVerifyDetail> queryWrapper1 = new QueryWrapper<>();
		 queryWrapper1.eq("flow_no",smartVerifyDetail.getFlowNo())
				 .eq("audit_status",1);
		 // 若没有待审核的记录了，则审核通过
		 if (smartVerifyDetailService.count(queryWrapper1) == 0){
			 SmartVerifyTask smartVerifyTask = new SmartVerifyTask();
			 smartVerifyTask.setFlowStatus(1);
			 smartVerifyTaskService.update(smartVerifyTask,queryWrapper2);
			 //同时给各个记录表更新状态
//			 syncVerify(flowNo, taskType, "1");
			 // 给填报人发送消息
//			 log.info(fillPersonUserName);

			 MessageDTO messageDTO = new MessageDTO();
			 messageDTO.setFromUser(userName);
			 messageDTO.setToUser(fillPersonUserName);
			 messageDTO.setTitle("审核已通过");
			 messageDTO.setContent("您的【"+taskType+"】审核已通过");
			 messageDTO.setCategory("1");
			 sysBaseAPI.sendSysAnnouncement(messageDTO);
//			 DySmsHelper.sendSms(userName,"test","审核已通过","您的【"+taskType+"】审核已通过",fillPersonUserName,fillPersonUserPhone);
			 return Result.OK("更新成功");
		 }
		 // 否则更新下一级管理员的审核状态为2
		 else {
			 SmartVerifyDetail smartVerifyDetail1 = new SmartVerifyDetail();
			 smartVerifyDetail1.setAuditStatus(2);
			 smartVerifyDetailService.update(smartVerifyDetail1,queryWrapper1);
		 }
	 }
	 // 如果设置了驳回
	 else if (smartVerifyDetail.getAuditStatus() == 4) {
		 SmartVerifyTask smartVerifyTask1 = new SmartVerifyTask();
		 smartVerifyTask1.setFlowStatus(0);
		 smartVerifyTaskService.update(smartVerifyTask1,queryWrapper2);
//		 syncVerify(flowNo,taskType, "0");
		 // 给填报人发送消息
		 MessageDTO messageDTO = new MessageDTO();
		 messageDTO.setFromUser(userName);
		 messageDTO.setToUser(fillPersonUserName);
		 messageDTO.setTitle("审核未通过");
		 messageDTO.setContent("您的【"+taskType+"】审核未通过，请重新填报");
		 messageDTO.setCategory("1");
		 sysBaseAPI.sendSysAnnouncement(messageDTO);
//		 DySmsHelper.sendSms(userName,"test","审核未通过","您的【"+taskType+"】审核未通过，请重新填报",fillPersonUserName,fillPersonUserPhone);
		 return Result.OK("更新成功");
	 }
	 return Result.OK("更新成功");
	}


}
