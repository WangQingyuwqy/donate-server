package org.jeecg.modules.protocolItem.mapper;

import java.util.List;
import org.jeecg.modules.protocolItem.entity.ProtocolOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 协议选项表
 * @Author: jeecg-boot
 * @Date:   2021-12-11
 * @Version: V1.0
 */
public interface ProtocolOptionMapper extends BaseMapper<ProtocolOption> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProtocolOption> selectByMainId(@Param("mainId") String mainId);
}
