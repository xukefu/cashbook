package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.vo.ConsumeCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface ConsumeCategoryMapper {

    @Select("select * from c_consume_category")
    List<ConsumeCategoryVO> getAll();

    @Select("select category_name from c_consume_category where id = #{consumeCategoryId}")
    String getCategoryNameById(@Param("consumeCategoryId") Long consumeCategoryId);
}
