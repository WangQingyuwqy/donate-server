package org.jeecg.modules.donationItem.service;

import org.jeecg.modules.donationItem.entity.DonationOption;
import org.jeecg.modules.donationItem.entity.DonationItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 捐赠项目
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
public interface IDonationItemService extends IService<DonationItem> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(DonationItem donationItem,List<DonationOption> donationOptionList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DonationItem donationItem,List<DonationOption> donationOptionList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
