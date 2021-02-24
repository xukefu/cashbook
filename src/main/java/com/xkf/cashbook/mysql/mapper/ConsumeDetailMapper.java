package com.xkf.cashbook.mysql.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface ConsumeDetailMapper extends BaseMapper<ConsumeDetailDO> {

    @Select("select * from c_consume_detail order by consume_date desc,id desc limit #{size}")
    List<ConsumeDetailVO> getLastDetail(int size);

    @Select("select ifnull(sum(consume_amount),0) from c_consume_detail ${ew.customSqlSegment}")
    Double selectTotalConsumeAmount(@Param(Constants.WRAPPER) QueryWrapper<ConsumeDetailDO> wrapper);
}
