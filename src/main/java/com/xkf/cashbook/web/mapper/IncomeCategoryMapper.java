package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.vo.IncomeCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IncomeCategoryMapper {

    @Select("select * from c_income_category")
    List<IncomeCategoryVO> getAll();
}
