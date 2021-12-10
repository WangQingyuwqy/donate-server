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
 * @Description: 协议项目
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Api(tags="协议项目")
@RestController
@RequestMapping("/protocolItem/protocolItem")
@Slf4j
public class ProtocolItemController {
	@Autowired
	private IProtocolItemService protocolItemService;
	@Autowired
	private IProtocolOptionService protocolOptionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param protocolItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "协议项目-分页列表查询")
	@ApiOperation(value="协议项目-分页列表查询", notes="协议项目-分页列表查询")
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
	 *   添加
	 *
	 * @param protocolItemPage
	 * @return
	 */
	@AutoLog(value = "协议项目-添加")
	@ApiOperation(value="协议项目-添加", notes="协议项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProtocolItemPage protocolItemPage) {
		ProtocolItem protocolItem = new ProtocolItem();
		BeanUtils.copyProperties(protocolItemPage, protocolItem);
		protocolItemService.saveMain(protocolItem, protocolItemPage.getProtocolOptionList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param protocolItemPage
	 * @return
	 */
	@AutoLog(value = "协议项目-编辑")
	@ApiOperation(value="协议项目-编辑", notes="协议项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProtocolItemPage protocolItemPage) {
		ProtocolItem protocolItem = new ProtocolItem();
		BeanUtils.copyProperties(protocolItemPage, protocolItem);
		ProtocolItem protocolItemEntity = protocolItemService.getById(protocolItem.getId());
		if(protocolItemEntity==null) {
			return Result.error("未找到对应数据");
		}
		protocolItemService.updateMain(protocolItem, protocolItemPage.getProtocolOptionList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "协议项目-通过id删除")
	@ApiOperation(value="协议项目-通过id删除", notes="协议项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		protocolItemService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "协议项目-批量删除")
	@ApiOperation(value="协议项目-批量删除", notes="协议项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.protocolItemService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "协议项目-通过id查询")
	@ApiOperation(value="协议项目-通过id查询", notes="协议项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProtocolItem protocolItem = protocolItemService.getById(id);
		if(protocolItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(protocolItem);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "协议选项表通过主表ID查询")
	@ApiOperation(value="协议选项表主表ID查询", notes="协议选项表-通主表ID查询")
	@GetMapping(value = "/queryProtocolOptionByMainId")
	public Result<?> queryProtocolOptionListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ProtocolOption> protocolOptionList = protocolOptionService.selectByMainId(id);
		return Result.OK(protocolOptionList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param protocolItem
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProtocolItem protocolItem) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ProtocolItem> queryWrapper = QueryGenerator.initQueryWrapper(protocolItem, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ProtocolItem> queryList = protocolItemService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<ProtocolItem> protocolItemList = new ArrayList<ProtocolItem>();
      if(oConvertUtils.isEmpty(selections)) {
          protocolItemList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          protocolItemList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ProtocolItemPage> pageList = new ArrayList<ProtocolItemPage>();
      for (ProtocolItem main : protocolItemList) {
          ProtocolItemPage vo = new ProtocolItemPage();
          BeanUtils.copyProperties(main, vo);
          List<ProtocolOption> protocolOptionList = protocolOptionService.selectByMainId(main.getId());
          vo.setProtocolOptionList(protocolOptionList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "协议项目列表");
      mv.addObject(NormalExcelConstants.CLASS, ProtocolItemPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("协议项目数据", "导出人:"+sysUser.getRealname(), "协议项目"));
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
              List<ProtocolItemPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProtocolItemPage.class, params);
              for (ProtocolItemPage page : list) {
                  ProtocolItem po = new ProtocolItem();
                  BeanUtils.copyProperties(page, po);
                  protocolItemService.saveMain(po, page.getProtocolOptionList());
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
