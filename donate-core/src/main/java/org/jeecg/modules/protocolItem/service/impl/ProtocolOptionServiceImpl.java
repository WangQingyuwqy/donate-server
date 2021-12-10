package org.jeecg.modules.protocolItem.service.impl;

import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import org.jeecg.modules.protocolItem.mapper.ProtocolOptionMapper;
import org.jeecg.modules.protocolItem.service.IProtocolOptionService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 协议选项表
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
@Service
public class ProtocolOptionServiceImpl extends ServiceImpl<ProtocolOptionMapper, ProtocolOption> implements IProtocolOptionService {
	
	@Autowired
	private ProtocolOptionMapper protocolOptionMapper;
	
	@Override
	public List<ProtocolOption> selectByMainId(String mainId) {
		return protocolOptionMapper.selectByMainId(mainId);
	}
}
