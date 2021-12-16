package org.jeecg.modules.donationItem.service;

import org.jeecg.modules.donationItem.entity.DonationOption;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 捐赠选项
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
public interface IDonationOptionService extends IService<DonationOption> {

	public List<DonationOption> selectByMainId(String mainId);
}
