package com.xkf.cashbook.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.web.mysql.IncomeDetailDO;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface IncomeDetailMapper extends BaseMapper<IncomeDetailDO> {

    @Select("select * from c_income_detail order by income_date desc,id desc limit #{size}")
    List<IncomeDetailVO> getLastDetail(@Param("size") int size);
}
