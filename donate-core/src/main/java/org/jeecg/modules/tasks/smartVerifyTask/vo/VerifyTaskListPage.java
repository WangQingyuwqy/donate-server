package org.jeecg.modules.tasks.smartVerifyTask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: sike
 * @Date: 2021/11/8 0:51
 * @Version: V1.0
 */

@Data
@ApiModel(value = "审核对象",description = "审核对象")
public class VerifyTaskListPage {
    /**主键*/
    @ApiModelProperty(value = "主键")
    private String flowNo;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;

    @ApiModelProperty(value = "审核类型")
    private String taskType;

    @ApiModelProperty(value = "填报人")
    private String fillPerson;

    /**所属部门*/
    @ApiModelProperty(value = "填报部门")
    private String fillDepart;

    @ApiModelProperty(value = "审批状态")
    private Integer flowStatus;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private java.util.Date auditTime;

    @ApiModelProperty(value = "审核部门")
    private String auditDepart;

    @ApiModelProperty(value = "审核人审核状态")
    private Integer auditStatus;

    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @Excel(name = "单位类型", width = 15)
    @ApiModelProperty(value = "单位类型")
    private String departType;

    /**说明*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "说明")
    private String remark;

}
