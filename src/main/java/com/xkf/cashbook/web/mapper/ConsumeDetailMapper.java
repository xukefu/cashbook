package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xukf01
 */
@Mapper
public interface ConsumeDetailMapper {

    int add(ConsumeDetailVO consumeDetailVO);

    @Select("select * from c_consume_detail order by consume_date desc,id desc limit #{size}")
    List<ConsumeDetailVO> getLastDetail(int size);
}
