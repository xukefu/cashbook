package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IncomeDetailMapper {

    int add(IncomeDetailVO incomeDetailVO);
}
