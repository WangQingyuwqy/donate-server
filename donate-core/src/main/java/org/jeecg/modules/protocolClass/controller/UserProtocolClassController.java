package org.jeecg.modules.protocolClass.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.protocolClass.entity.ProtocolClass;
import org.jeecg.modules.protocolClass.service.IProtocolClassService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 协议项目分类(用户登录)
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Api(tags="协议项目分类(用户登录)")
@RestController
@RequestMapping("/user/protocolClass")
@Slf4j
public class UserProtocolClassController extends JeecgController<ProtocolClass, IProtocolClassService> {
    @Autowired
    private IProtocolClassService protocolClassService;

    /**
     * 分页列表查询
     *
     * @param protocolClass
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "协议项目分类-分页列表查询")
    @ApiOperation(value="协议项目分类-分页列表查询", notes="协议项目分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ProtocolClass protocolClass,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ProtocolClass> queryWrapper = QueryGenerator.initQueryWrapper(protocolClass, req.getParameterMap());
        Page<ProtocolClass> page = new Page<ProtocolClass>(pageNo, pageSize);
        IPage<ProtocolClass> pageList = protocolClassService.page(page, queryWrapper);
        return Result.OK(pageList);
    }



    /**
     *  编辑
     *
     * @param protocolClass
     * @return
     */
    @AutoLog(value = "协议项目分类-编辑")
    @ApiOperation(value="协议项目分类-编辑", notes="协议项目分类-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ProtocolClass protocolClass) {
        protocolClassService.updateById(protocolClass);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "协议项目分类-通过id删除")
    @ApiOperation(value="协议项目分类-通过id删除", notes="协议项目分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        protocolClassService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "协议项目分类-批量删除")
    @ApiOperation(value="协议项目分类-批量删除", notes="协议项目分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.protocolClassService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "协议项目分类-通过id查询")
    @ApiOperation(value="协议项目分类-通过id查询", notes="协议项目分类-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        ProtocolClass protocolClass = protocolClassService.getById(id);
        if(protocolClass==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(protocolClass);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param protocolClass
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProtocolClass protocolClass) {
        return super.exportXls(request, protocolClass, ProtocolClass.class, "协议项目分类");
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
        return super.importExcel(request, response, ProtocolClass.class);
    }

}
