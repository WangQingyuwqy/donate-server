package org.jeecg.modules.donationItem.mapper;

import java.util.List;
import org.jeecg.modules.donationItem.entity.DonationOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 捐赠选项
 * @Author: jeecg-boot
 * @Date:   2021-12-15
 * @Version: V1.0
 */
public interface DonationOptionMapper extends BaseMapper<DonationOption> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DonationOption> selectByMainId(@Param("mainId") String mainId);
}
