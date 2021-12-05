package org.jeecg.modules.donationItem.service.impl;

import org.jeecg.modules.donationItem.entity.DonationItem;
import org.jeecg.modules.donationItem.entity.DonationOption;
import org.jeecg.modules.donationItem.mapper.DonationOptionMapper;
import org.jeecg.modules.donationItem.mapper.DonationItemMapper;
import org.jeecg.modules.donationItem.service.IDonationItemService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 捐赠项目
 * @Author: jeecg-boot
 * @Date:   2021-12-05
 * @Version: V1.0
 */
@Service
public class DonationItemServiceImpl extends ServiceImpl<DonationItemMapper, DonationItem> implements IDonationItemService {

	@Autowired
	private DonationItemMapper donationItemMapper;
	@Autowired
	private DonationOptionMapper donationOptionMapper;
	
	@Override
	@Transactional
	public void saveMain(DonationItem donationItem, List<DonationOption> donationOptionList) {
		donationItemMapper.insert(donationItem);
		if(donationOptionList!=null && donationOptionList.size()>0) {
			for(DonationOption entity:donationOptionList) {
				//外键设置
				entity.setDonationItemId(donationItem.getId());
				donationOptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(DonationItem donationItem,List<DonationOption> donationOptionList) {
		donationItemMapper.updateById(donationItem);
		
		//1.先删除子表数据
		donationOptionMapper.deleteByMainId(donationItem.getId());
		
		//2.子表数据重新插入
		if(donationOptionList!=null && donationOptionList.size()>0) {
			for(DonationOption entity:donationOptionList) {
				//外键设置
				entity.setDonationItemId(donationItem.getId());
				donationOptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		donationOptionMapper.deleteByMainId(id);
		donationItemMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			donationOptionMapper.deleteByMainId(id.toString());
			donationItemMapper.deleteById(id);
		}
	}
	
}
