package org.jeecg.modules.donationOrder.entity;

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
 * @Description: 捐赠订单
 * @Author: jeecg-boot
 * @Date:   2021-12-05
 * @Version: V1.0
 */
@Data
@TableName("donation_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="donation_order对象", description="捐赠订单")
public class DonationOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**支付时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
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
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private java.lang.String orderNo;
	/**第三方支付订单*/
	@Excel(name = "第三方支付订单", width = 15)
    @ApiModelProperty(value = "第三方支付订单")
    private java.lang.String thirdPayOrder;
	/**捐赠份数*/
	@Excel(name = "捐赠份数", width = 15)
    @ApiModelProperty(value = "捐赠份数")
    private java.lang.String piece;
	/**是否是自由捐*/
	@Excel(name = "是否是自由捐", width = 15)
    @ApiModelProperty(value = "是否是自由捐")
    private java.lang.Integer isFreeDonation;
	/**支付方式*/
	@Excel(name = "支付方式", width = 15)
    @ApiModelProperty(value = "支付方式")
    private java.lang.String payMethod;
	/**支付状态*/
	@Excel(name = "支付状态", width = 15)
    @ApiModelProperty(value = "支付状态")
    private java.lang.Integer status;
	/**支付留言*/
	@Excel(name = "支付留言", width = 15)
    @ApiModelProperty(value = "支付留言")
    private java.lang.String message;
	/**捐赠总额*/
	@Excel(name = "捐赠总额", width = 15)
    @ApiModelProperty(value = "捐赠总额")
    private java.lang.String money;
}
