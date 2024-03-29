package com.xkf.cashbook.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.web.mysql.ConsumeCategoryDO;
import com.xkf.cashbook.web.vo.ConsumeCategoryVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface ConsumeCategoryMapper extends BaseMapper<ConsumeCategoryDO> {

    @Select("select * from c_consume_category order by id")
    List<ConsumeCategoryVO> getAll();

    @Select("select category_name from c_consume_category where id = #{consumeCategoryId}")
    String getCategoryNameById(@Param("consumeCategoryId") Long consumeCategoryId);

}
