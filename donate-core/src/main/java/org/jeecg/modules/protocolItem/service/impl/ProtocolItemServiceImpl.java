package org.jeecg.modules.protocolItem.service.impl;

import org.jeecg.modules.protocolItem.entity.ProtocolItem;
import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import org.jeecg.modules.protocolItem.mapper.ProtocolOptionMapper;
import org.jeecg.modules.protocolItem.mapper.ProtocolItemMapper;
import org.jeecg.modules.protocolItem.service.IProtocolItemService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 协议项目
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Service
public class ProtocolItemServiceImpl extends ServiceImpl<ProtocolItemMapper, ProtocolItem> implements IProtocolItemService {

	@Autowired
	private ProtocolItemMapper protocolItemMapper;
	@Autowired
	private ProtocolOptionMapper protocolOptionMapper;
	
	@Override
	@Transactional
	public void saveMain(ProtocolItem protocolItem, List<ProtocolOption> protocolOptionList) {
		protocolItemMapper.insert(protocolItem);
		if(protocolOptionList!=null && protocolOptionList.size()>0) {
			for(ProtocolOption entity:protocolOptionList) {
				//外键设置
				entity.setProtolcolItemId(protocolItem.getId());
				protocolOptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(ProtocolItem protocolItem,List<ProtocolOption> protocolOptionList) {
		protocolItemMapper.updateById(protocolItem);
		
		//1.先删除子表数据
		protocolOptionMapper.deleteByMainId(protocolItem.getId());
		
		//2.子表数据重新插入
		if(protocolOptionList!=null && protocolOptionList.size()>0) {
			for(ProtocolOption entity:protocolOptionList) {
				//外键设置
				entity.setProtolcolItemId(protocolItem.getId());
				protocolOptionMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		protocolOptionMapper.deleteByMainId(id);
		protocolItemMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			protocolOptionMapper.deleteByMainId(id.toString());
			protocolItemMapper.deleteById(id);
		}
	}
	
}
