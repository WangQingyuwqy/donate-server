package org.jeecg.modules.protocolItem.controller;

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
import org.jeecg.modules.donationItem.entity.DonationItem;
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
import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import org.jeecg.modules.protocolItem.entity.ProtocolItem;
import org.jeecg.modules.protocolItem.vo.ProtocolItemPage;
import org.jeecg.modules.protocolItem.service.IProtocolItemService;
import org.jeecg.modules.protocolItem.service.IProtocolOptionService;
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
 * @Date:   2021-12-11
 * @Version: V1.0
 */
@Api(tags="????????????")
@RestController
@RequestMapping("/protocolItem/protocolItem")
@Slf4j
public class ProtocolItemController {
	@Autowired
	private IProtocolItemService protocolItemService;
	@Autowired
	private IProtocolOptionService protocolOptionService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;

	/**
	 * ??????????????????
	 *
	 * @param protocolItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProtocolItem protocolItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProtocolItem> queryWrapper = QueryGenerator.initQueryWrapper(protocolItem, req.getParameterMap());
		Page<ProtocolItem> page = new Page<ProtocolItem>(pageNo, pageSize);
		IPage<ProtocolItem> pageList = protocolItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   ??????
	 *
	 * @param protocolItemPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProtocolItemPage protocolItemPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		log.info(orgCode);
		if("".equals(orgCode)) {
			return Result.error("???????????????????????????");
		}

		String id = sysBaseAPI.getDepartIdsByOrgCode(orgCode);
		ProtocolItem protocolItem=new ProtocolItem();

		BeanUtils.copyProperties(protocolItemPage, protocolItem);

		protocolItemService.saveMain(protocolItem, protocolItemPage.getProtocolOptionList());
		String recordId = protocolItem.getId();
		log.info("recordId is"+recordId);
		List<String> roleName = sysBaseAPI.getRolesByUsername(sysUser.getUsername());
		log.info("??????"+String.valueOf(roleName));
		/*if(roleName.contains("schoolmate_admin")){
			log.info("??????hahaha");
			protocolItem.setStatus(1);
			protocolItem.setCategory(1);
		}
		else{
			protocolItem.setStatus(1);
			protocolItem.setCategory(2);
		}*/

		protocolItemService.updateById(protocolItem);

		return Result.OK("???????????????");
	}

	/**
	 *  ??????
	 *
	 * @param protocolItemPage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProtocolItemPage protocolItemPage) {
		ProtocolItem protocolItem = new ProtocolItem();
		BeanUtils.copyProperties(protocolItemPage, protocolItem);
		ProtocolItem protocolItemEntity = protocolItemService.getById(protocolItem.getId());
		if(protocolItemEntity==null) {
			return Result.error("?????????????????????");
		}
		protocolItemService.updateMain(protocolItem, protocolItemPage.getProtocolOptionList());
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
		protocolItemService.delMain(id);
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
		this.protocolItemService.delBatchMain(Arrays.asList(ids.split(",")));
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
		ProtocolItem protocolItem = protocolItemService.getById(id);
		if(protocolItem==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(protocolItem);

	}

	 @AutoLog(value = "????????????-????????????id??????")
	 @ApiOperation(value="????????????-????????????id??????", notes="????????????-??????id??????")
	 @GetMapping(value = "/queryByClassId")
	 public Result<?> queryByClassId(@RequestParam(name="classId",required=true) String classId) {

		 ProtocolItem protocolItem = new ProtocolItem();
		 QueryWrapper<ProtocolItem> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("protocol_class",classId);

		 List<ProtocolItem> itemList = protocolItemService.list(queryWrapper);

		 return Result.OK(itemList);

	 }

	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????????????????ID??????")
	@ApiOperation(value="?????????????????????ID??????", notes="???????????????-?????????ID??????")
	@GetMapping(value = "/queryProtocolOptionByMainId")
	public Result<?> queryProtocolOptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ProtocolOption> protocolOptionList = protocolOptionService.selectByMainId(id);
		return Result.OK(protocolOptionList);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param protocolItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProtocolItem protocolItem) {
      // Step.1 ??????????????????????????????
      QueryWrapper<ProtocolItem> queryWrapper = QueryGenerator.initQueryWrapper(protocolItem, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<ProtocolItem> queryList = protocolItemService.list(queryWrapper);
      // ??????????????????
      String selections = request.getParameter("selections");
      List<ProtocolItem> protocolItemList = new ArrayList<ProtocolItem>();
      if(oConvertUtils.isEmpty(selections)) {
          protocolItemList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          protocolItemList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 ??????pageList
      List<ProtocolItemPage> pageList = new ArrayList<ProtocolItemPage>();
      for (ProtocolItem main : protocolItemList) {
          ProtocolItemPage vo = new ProtocolItemPage();
          BeanUtils.copyProperties(main, vo);
          List<ProtocolOption> protocolOptionList = protocolOptionService.selectByMainId(main.getId());
          vo.setProtocolOptionList(protocolOptionList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
      mv.addObject(NormalExcelConstants.CLASS, ProtocolItemPage.class);
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
              List<ProtocolItemPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProtocolItemPage.class, params);
              for (ProtocolItemPage page : list) {
                  ProtocolItem po = new ProtocolItem();
                  BeanUtils.copyProperties(page, po);
                  protocolItemService.saveMain(po, page.getProtocolOptionList());
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
