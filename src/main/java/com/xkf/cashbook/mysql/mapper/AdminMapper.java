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

    @Select("select ifnull(sum(ccd.consume_amount),0)consumeAmount,ccd.consume_category_id consumeCategoryId from c_consume_detail ccd " +
            "inner join c_user cu on ccd.consume_by = cu.id " +
            "where ccd.consume_date = DATE_SUB(curdate(),INTERVAL 1 DAY) and cu.family_id = #{familyId} " +
            "GROUP BY ccd.consume_category_id")
    List<ConsumeReportVO> getSumTotalTableByCategory(Long familyId);

    @Select("SELECT ifnull(sum(ccd.consume_amount),0) FROM `c_consume_detail` ccd " +
            "inner join c_user cu on ccd.consume_by = cu.id " +
            "where cu.family_id = #{familyId} and ccd.consume_date >=#{startDate} and ccd.consume_date<= #{endDate}")
    Double getConsumeAmountByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("familyId") Long familyId);

    @Select("SELECT ifnull(sum(cid.income_amount),0) FROM `c_income_detail`cid " +
            "inner join c_user cu on cid.income_by = cu.id " +
            "where cu.family_id = #{familyId} and cid.income_date >=#{date}")
    Double getIncomeAmountByDate(@Param("date") String date,@Param("familyId")Long familyId);

    @Select("SELECT ifnull(sum(consume_amount),0)amount,consume_category_id FROM `c_consume_detail` " +
            "where consume_date >=#{startDate} and consume_date<= #{endDate} " +
            "group by consume_category_id order by amount desc limit 5")
    List<ConsumeProportionDTO> getMonthlyConsumeProportion(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,@Param("familyId")Long familyId);

    @Select("select ifnull(sum(ccd.consume_amount),0)amount,ccd.consume_date date from c_consume_detail ccd inner join c_user cu on ccd.consume_by = cu.id where cu.family_id = #{familyId} group by consume_date HAVING consume_date >= DATE_ADD(now(),INTERVAL #{days} day)")
    List<LineChartDTO> getEveryDayConsumeAmount(int days,@Param("familyId")Long familyId);

}
