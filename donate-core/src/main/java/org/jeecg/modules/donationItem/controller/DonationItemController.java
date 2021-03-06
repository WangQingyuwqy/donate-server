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
 * @Description: ????????????
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
@Api(tags="????????????")
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
	 * ??????????????????
	 *
	 * @param donationItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
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
	 *   ??????
	 *
	 * @param donationItemPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DonationItemPage donationItemPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		log.info(orgCode);
		if("".equals(orgCode)) {
			return Result.error("???????????????????????????");
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
		log.info("??????"+String.valueOf(roleName));
		if(roleName.contains("schoolmate_admin")){
			log.info("??????hahaha");
			donationItem.setStatus(1);
			donationItem.setCategory(1);
		}
		else{
			smartVerify.addVerifyRecord(recordId,"??????");
			donationItem.setStatus(smartVerify.getFlowStatusById(recordId));
			donationItem.setCategory(2);
		}

		donationItemService.updateById(donationItem);

		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param donationItemPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DonationItemPage donationItemPage) {
		DonationItem donationItem = new DonationItem();
		BeanUtils.copyProperties(donationItemPage, donationItem);
		DonationItem donationItemEntity = donationItemService.getById(donationItem.getId());
		if(donationItemEntity==null) {
			return Result.error("?????????????????????");
		}
		donationItemService.updateMain(donationItem, donationItemPage.getDonationOptionList());
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		donationItemService.delMain(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.donationItemService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DonationItem donationItem = donationItemService.getById(id);
		if(donationItem==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(donationItem);

	}
	 @AutoLog(value = "????????????-????????????id??????")
	 @ApiOperation(value="????????????-????????????id??????", notes="????????????-??????id??????")
	 @GetMapping(value = "/queryByClassId")
	 public Result<?> queryByClassId(@RequestParam(name="classId",required=true) String classId) {

		 DonationItem donationItem = new DonationItem();
		 QueryWrapper<DonationItem> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("donation_class",classId);

		 List<DonationItem> itemList = donationItemService.list(queryWrapper);

		 return Result.OK(itemList);

	 }

	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????????????????ID??????")
	@ApiOperation(value="??????????????????ID??????", notes="????????????-?????????ID??????")
	@GetMapping(value = "/queryDonationOptionByMainId")
	public Result<?> queryDonationOptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<DonationOption> donationOptionList = donationOptionService.selectByMainId(id);
		return Result.OK(donationOptionList);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param donationItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DonationItem donationItem) {
      // Step.1 ??????????????????????????????
      QueryWrapper<DonationItem> queryWrapper = QueryGenerator.initQueryWrapper(donationItem, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<DonationItem> queryList = donationItemService.list(queryWrapper);
      // ??????????????????
      String selections = request.getParameter("selections");
      List<DonationItem> donationItemList = new ArrayList<DonationItem>();
      if(oConvertUtils.isEmpty(selections)) {
          donationItemList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          donationItemList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 ??????pageList
      List<DonationItemPage> pageList = new ArrayList<DonationItemPage>();
      for (DonationItem main : donationItemList) {
          DonationItemPage vo = new DonationItemPage();
          BeanUtils.copyProperties(main, vo);
          List<DonationOption> donationOptionList = donationOptionService.selectByMainId(main.getId());
          vo.setDonationOptionList(donationOptionList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
      mv.addObject(NormalExcelConstants.CLASS, DonationItemPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:"+sysUser.getRealname(), "????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * ??????excel????????????
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
          MultipartFile file = entity.getValue();// ????????????????????????
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
              return Result.OK("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("?????????????????????");
    }

}
