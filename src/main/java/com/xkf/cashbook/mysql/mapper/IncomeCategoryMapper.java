package com.xkf.cashbook.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.mysql.model.IncomeCategoryDO;
import com.xkf.cashbook.pojo.vo.IncomeCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IncomeCategoryMapper extends BaseMapper<IncomeCategoryDO> {

    @Select("select * from c_income_category order by id")
    List<IncomeCategoryVO> getAll();
}
