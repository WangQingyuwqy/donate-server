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
 * @Description: 协议项目（用户登录）
 * @Author: jeecg-boot
 * @Date:   2021-12-11
 * @Version: V1.0
 */
@Api(tags="协议项目(用户登录)")
@RestController
@RequestMapping("/user/protocolItem")
@Slf4j
public class UserProtocolItemController {
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

    @AutoLog(value = "协议项目-通过分类id查询")
    @ApiOperation(value="协议项目-通过分类id查询", notes="协议项目-通过id查询")
    @GetMapping(value = "/queryByClassId")
    public Result<?> queryByClassId(@RequestParam(name="classId",required=true) String classId) {

        ProtocolItem protocolItem = new ProtocolItem();
        QueryWrapper<ProtocolItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("protocol_class",classId);

        List<ProtocolItem> itemList = protocolItemService.list(queryWrapper);

        return Result.OK(itemList);

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


}
