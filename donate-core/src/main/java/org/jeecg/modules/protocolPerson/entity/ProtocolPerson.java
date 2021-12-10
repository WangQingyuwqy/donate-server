package org.jeecg.modules.protocolPerson.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 协议捐赠方
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Data
@TableName("protocol_person")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="protocol_person对象", description="协议捐赠方")
public class ProtocolPerson implements Serializable {
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
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String phone;
	/**电子邮件*/
	@Excel(name = "电子邮件", width = 15)
    @ApiModelProperty(value = "电子邮件")
    private java.lang.String email;
	/**入学时间*/
	@Excel(name = "入学时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入学时间")
    private java.util.Date studyTime;
	/**学号或工号*/
	@Excel(name = "学号或工号", width = 15)
    @ApiModelProperty(value = "学号或工号")
    private java.lang.String studentNo;
	/**院系*/
	@Excel(name = "院系", width = 15)
    @ApiModelProperty(value = "院系")
    private java.lang.String depart;
	/**是否校友*/
	@Excel(name = "是否校友", width = 15, dicCode = "is_schoolmate")
	@Dict(dicCode = "is_schoolmate")
    @ApiModelProperty(value = "是否校友")
    private java.lang.String isSchoolmate;
	/**专业*/
	@Excel(name = "专业", width = 15)
    @ApiModelProperty(value = "专业")
    private java.lang.String major;
	/**在校职业*/
	@Excel(name = "在校职业", width = 15)
    @ApiModelProperty(value = "在校职业")
    private java.lang.String type;
}
