package com.xkf.cashbook.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.mysql.model.ConsumeCategoryDO;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface ConsumeCategoryMapper extends BaseMapper<ConsumeCategoryDO> {

    @Select("select * from c_consume_category where id in(select category_id FROM c_family_category where family_id = 0)\n" +
            "\tOR id in(select fc.category_id from c_family_category fc where fc.family_id = #{familyId}) order by id ")
    List<ConsumeCategoryDO> getAll(Long familyId);

    @Select("select category_name from c_consume_category where id = #{consumeCategoryId}")
    String getCategoryNameById(@Param("consumeCategoryId") Long consumeCategoryId);

}
