package org.jeecg.modules.donationItem.service.impl;

import org.jeecg.modules.donationItem.entity.DonationOption;
import org.jeecg.modules.donationItem.mapper.DonationOptionMapper;
import org.jeecg.modules.donationItem.service.IDonationOptionService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 捐赠选项
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
@Service
public class DonationOptionServiceImpl extends ServiceImpl<DonationOptionMapper, DonationOption> implements IDonationOptionService {
	
	@Autowired
	private DonationOptionMapper donationOptionMapper;
	
	@Override
	public List<DonationOption> selectByMainId(String mainId) {
		return donationOptionMapper.selectByMainId(mainId);
	}
}
