package com.xkf.cashbook.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.web.mysql.IncomeCategoryDO;
import com.xkf.cashbook.web.vo.IncomeCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IncomeCategoryMapper extends BaseMapper<IncomeCategoryDO> {

    @Select("select * from c_income_category order by id")
    List<IncomeCategoryVO> getAll();
}
