package org.jeecg.modules.protocolItem.service;

import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 协议选项表
 * @Author: jeecg-boot
 * @Date:   2021-12-10
 * @Version: V1.0
 */
public interface IProtocolOptionService extends IService<ProtocolOption> {

	public List<ProtocolOption> selectByMainId(String mainId);
}
