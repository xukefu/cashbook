package com.xkf.cashbook.mysql.mapper;

import com.xkf.cashbook.pojo.dto.ConsumeProportionDTO;
import com.xkf.cashbook.pojo.dto.LineChartDTO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import com.xkf.cashbook.pojo.vo.ConsumeReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select ifnull(sum(consume_amount),0)consumeAmount,consume_category_id consumeCategoryId from c_consume_detail where consume_date = DATE_SUB(curdate(),INTERVAL 1 DAY) GROUP BY consume_category_id")
    List<ConsumeReportVO> getSumTotalTableByCategory();

    @Select("SELECT ifnull(sum(consume_amount),0) FROM `c_consume_detail` where consume_date >=#{startDate} and consume_date<= #{endDate}")
    Double getConsumeAmountByDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Select("SELECT ifnull(sum(income_amount),0) FROM `c_income_detail` where income_date >=#{date}")
    Double getIncomeAmountByDate(@Param("date") String date);

    @Select("SELECT ifnull(sum(consume_amount),0)amount,consume_category_id FROM `c_consume_detail` where consume_date >=#{startDate} and consume_date<= #{endDate} group by consume_category_id order by amount desc limit 5")
    List<ConsumeProportionDTO> getMonthlyConsumeProportion(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Select("select ifnull(sum(consume_amount),0)amount,consume_date date from c_consume_detail group by consume_date HAVING consume_date >= DATE_ADD(now(),INTERVAL #{days} day)")
    List<LineChartDTO> getEveryDayConsumeAmount(int days);

}
