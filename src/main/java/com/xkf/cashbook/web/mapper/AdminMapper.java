package com.xkf.cashbook.web.mapper;

import com.xkf.cashbook.web.dto.ConsumeProportionDTO;
import com.xkf.cashbook.web.dto.LineChartDTO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import com.xkf.cashbook.web.vo.ConsumeReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    @Select("select sum(consume_amount)consumeAmount,consume_category_id consumeCategoryId from c_consume_detail where consume_date = DATE_SUB(curdate(),INTERVAL 1 DAY) GROUP BY\n" +
            "\tconsume_category_id")
    List<ConsumeReportVO> getSumTotalTableByCategory();

    @Select("SELECT sum(consume_amount) FROM `c_consume_detail` where consume_date >=#{date}")
    Double getConsumeAmountByDate(@Param("date") String date);

    @Select("SELECT sum(income_amount) FROM `c_income_detail` where income_date >=#{date}")
    Double getIncomeAmountByDate(@Param("date") String date);

    @Select("SELECT sum(consume_amount)amount,consume_category_id FROM `c_consume_detail` where consume_date >= #{date} group by consume_category_id order by amount desc limit 5")
    List<ConsumeProportionDTO> getMonthlyConsumeProportion(@Param("date") String date);

    @Select("select sum(consume_amount)amount,consume_date date from c_consume_detail group by consume_date HAVING consume_date >= DATE_ADD(now(),INTERVAL #{days} day)")
    List<LineChartDTO> getEveryDayConsumeAmount(int days);
}
