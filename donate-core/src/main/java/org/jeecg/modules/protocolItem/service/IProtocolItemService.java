package org.jeecg.modules.protocolItem.service;

import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import org.jeecg.modules.protocolItem.entity.ProtocolItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 协议项目
 * @Author: jeecg-boot
 * @Date:   2021-12-11
 * @Version: V1.0
 */
public interface IProtocolItemService extends IService<ProtocolItem> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ProtocolItem protocolItem,List<ProtocolOption> protocolOptionList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ProtocolItem protocolItem,List<ProtocolOption> protocolOptionList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
