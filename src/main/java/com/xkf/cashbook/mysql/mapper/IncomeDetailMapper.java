package com.xkf.cashbook.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.mysql.model.IncomeDetailDO;
import com.xkf.cashbook.pojo.vo.IncomeDetailVO;
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
