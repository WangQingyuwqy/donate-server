package org.jeecg.modules.donationItem.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.tasks.smartVerifyTask.service.ISmartVerifyTaskService;
import org.jeecg.modules.tasks.smartVerifyTask.service.SmartVerify;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.donationItem.entity.DonationOption;
import org.jeecg.modules.donationItem.entity.DonationItem;
import org.jeecg.modules.donationItem.vo.DonationItemPage;
import org.jeecg.modules.donationItem.service.IDonationItemService;
import org.jeecg.modules.donationItem.service.IDonationOptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 捐赠项目
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
@Api(tags="捐赠项目")
@RestController
@RequestMapping("/donationItem/donationItem")
@Slf4j
public class DonationItemController {
	@Autowired
	private IDonationItemService donationItemService;
	@Autowired
	private IDonationOptionService donationOptionService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISmartVerifyTaskService smartVerifyTaskService;
	@Autowired
	private SmartVerify smartVerify;
	
	/**
	 * 分页列表查询
	 *
	 * @param donationItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "捐赠项目-分页列表查询")
	@ApiOperation(value="捐赠项目-分页列表查询", notes="捐赠项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DonationItem donationItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DonationItem> queryWrapper = QueryGenerator.initQueryWrapper(donationItem, req.getParameterMap());
		Page<DonationItem> page = new Page<DonationItem>(pageNo, pageSize);
		IPage<DonationItem> pageList = donationItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param donationItemPage
	 * @return
	 */
	@AutoLog(value = "捐赠项目-添加")
	@ApiOperation(value="捐赠项目-添加", notes="捐赠项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DonationItemPage donationItemPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		log.info(orgCode);
		if("".equals(orgCode)) {
			return Result.error("本用户没有操作权限");
		}



		String id = sysBaseAPI.getDepartIdsByOrgCode(orgCode);
//		log.info(id);
//		donationItemPage.setDepartId(id);
		DonationItem donationItem = new DonationItem();
		BeanUtils.copyProperties(donationItemPage, donationItem);
//		log.info(smartSupervision.getId());

//		Boolean isVerify = smartVerifyTypeService.getIsVerifyStatusByType(verifyType);
		donationItemService.saveMain(donationItem, donationItemPage.getDonationOptionList());
		String recordId = donationItem.getId();
		log.info("recordId is"+recordId);
		List<String> roleName = sysBaseAPI.getRolesByUsername(sysUser.getUsername());
		log.info("这里"+String.valueOf(roleName));
		if(roleName.contains("schoolmate_admin")){
			log.info("这里hahaha");
			donationItem.setStatus(1);
			donationItem.setCategory(1);
		}
		else{
			smartVerify.addVerifyRecord(recordId,"捐赠");
			donationItem.setStatus(smartVerify.getFlowStatusById(recordId));
			donationItem.setCategory(2);
		}

		donationItemService.updateById(donationItem);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param donationItemPage
	 * @return
	 */
	@AutoLog(value = "捐赠项目-编辑")
	@ApiOperation(value="捐赠项目-编辑", notes="捐赠项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DonationItemPage donationItemPage) {
		DonationItem donationItem = new DonationItem();
		BeanUtils.copyProperties(donationItemPage, donationItem);
		DonationItem donationItemEntity = donationItemService.getById(donationItem.getId());
		if(donationItemEntity==null) {
			return Result.error("未找到对应数据");
		}
		donationItemService.updateMain(donationItem, donationItemPage.getDonationOptionList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "捐赠项目-通过id删除")
	@ApiOperation(value="捐赠项目-通过id删除", notes="捐赠项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		donationItemService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "捐赠项目-批量删除")
	@ApiOperation(value="捐赠项目-批量删除", notes="捐赠项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.donationItemService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "捐赠项目-通过id查询")
	@ApiOperation(value="捐赠项目-通过id查询", notes="捐赠项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DonationItem donationItem = donationItemService.getById(id);
		if(donationItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(donationItem);

	}
	 @AutoLog(value = "捐赠项目-通过分类id查询")
	 @ApiOperation(value="捐赠项目-通过分类id查询", notes="捐赠项目-通过id查询")
	 @GetMapping(value = "/queryByClassId")
	 public Result<?> queryByClassId(@RequestParam(name="classId",required=true) String classId) {

		 DonationItem donationItem = new DonationItem();
		 QueryWrapper<DonationItem> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("donation_class",classId);

		 List<DonationItem> itemList = donationItemService.list(queryWrapper);

		 return Result.OK(itemList);

	 }

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "捐赠选项通过主表ID查询")
	@ApiOperation(value="捐赠选项主表ID查询", notes="捐赠选项-通主表ID查询")
	@GetMapping(value = "/queryDonationOptionByMainId")
	public Result<?> queryDonationOptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DonationOption> donationOptionList = donationOptionService.selectByMainId(id);
		return Result.OK(donationOptionList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param donationItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DonationItem donationItem) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<DonationItem> queryWrapper = QueryGenerator.initQueryWrapper(donationItem, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<DonationItem> queryList = donationItemService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<DonationItem> donationItemList = new ArrayList<DonationItem>();
      if(oConvertUtils.isEmpty(selections)) {
          donationItemList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          donationItemList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<DonationItemPage> pageList = new ArrayList<DonationItemPage>();
      for (DonationItem main : donationItemList) {
          DonationItemPage vo = new DonationItemPage();
          BeanUtils.copyProperties(main, vo);
          List<DonationOption> donationOptionList = donationOptionService.selectByMainId(main.getId());
          vo.setDonationOptionList(donationOptionList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "捐赠项目列表");
      mv.addObject(NormalExcelConstants.CLASS, DonationItemPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("捐赠项目数据", "导出人:"+sysUser.getRealname(), "捐赠项目"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<DonationItemPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DonationItemPage.class, params);
              for (DonationItemPage page : list) {
                  DonationItem po = new DonationItem();
                  BeanUtils.copyProperties(page, po);
                  donationItemService.saveMain(po, page.getDonationOptionList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
