package org.jeecg.modules.protocolItem.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 协议项目
 * @Author: jeecg-boot
 * @Date:   2021-12-11
 * @Version: V1.0
 */
@ApiModel(value="protocol_item对象", description="协议项目")
@Data
@TableName("protocol_item")
public class ProtocolItem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String name;
	/**项目图片*/
	@Excel(name = "项目图片", width = 15)
    @ApiModelProperty(value = "项目图片")
    private java.lang.String picture;
    /**项目简介*/
    @Excel(name = "项目简介", width = 15)
    @ApiModelProperty(value = "项目简介")
    private java.lang.String itemDesc;
	/**项目详情*/
	@Excel(name = "项目详情", width = 15)
    @ApiModelProperty(value = "项目详情")
    private java.lang.String detail;
	/**捐赠故事*/
	@Excel(name = "捐赠故事", width = 15)
    @ApiModelProperty(value = "捐赠故事")
    private java.lang.String story;
	/**常见问题*/
	@Excel(name = "常见问题", width = 15)
    @ApiModelProperty(value = "常见问题")
    private java.lang.String question;
    /**支出情况*/
    @Excel(name = "支出情况", width = 15)
    @ApiModelProperty(value = "支出情况")
    private java.lang.String cost;
	/**协议项目分类*/
	@Excel(name = "协议项目分类", width = 15, dictTable = "protocol_class", dicText = "name", dicCode = "id")
    @Dict(dictTable = "protocol_class", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "协议项目分类")
    private java.lang.String protocolClass;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15,dicCode = "del_flag")
    @TableLogic
    private Integer delFlag;
	/**项目状态*/
    @Dict(dicCode = "protocol_status")
	@Excel(name = "项目状态", width = 15)
    @ApiModelProperty(value = "项目状态")
    private java.lang.Integer status;
	/**项目类别*/
	@Excel(name = "项目类别", width = 15, dicCode = "donation_category")
    @Dict(dicCode = "donation_category")
    @ApiModelProperty(value = "项目类别")
    private java.lang.Integer category;
	/**上传附件*/
	/*@Excel(name = "上传附件", width = 15)*/
    @ApiModelProperty(value = "上传附件")
    private java.lang.String description;
	/**项目到款情况*/
	@Excel(name = "项目到款情况", width = 15, dicCode = "money_situation")
    @Dict(dicCode = "money_situation")
    @ApiModelProperty(value = "项目到款情况")
    private java.lang.String getSituation;
	/**项目到账金额*/
	@Excel(name = "项目到账金额", width = 15)
    @ApiModelProperty(value = "项目到账金额")
    private java.lang.Integer getMoney;
	/**项目到账时间*/
	@Excel(name = "项目到账时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "项目到账时间")
    private java.util.Date getTime;
}
