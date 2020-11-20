package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IncomeDetailMapper {

    int add(IncomeDetailVO incomeDetailVO);

    @Select("select * from c_income_detail order by income_date desc,id desc limit #{size}")
    List<IncomeDetailVO> getLastDetail(@Param("size") int size);
}
