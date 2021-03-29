package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkf.cashbook.common.constant.UserStatus;
import com.xkf.cashbook.common.utils.PeriodsUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.constant.Period;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.ConsumeDetailDTO;
import com.xkf.cashbook.pojo.dto.ConsumeDetailPageDTO;
import com.xkf.cashbook.mysql.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.mysql.mapper.ConsumeDetailMapper;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.dto.UserSelectDTO;
import com.xkf.cashbook.service.IConsumeDetailService;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConsumeDetailServiceImpl extends ServiceImpl<ConsumeDetailMapper, ConsumeDetailDO> implements IConsumeDetailService {

    @Resource
    private ConsumeDetailMapper consumeDetailMapper;

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Resource
    private UserService userService;

    @Override
    public Result pageDetail(ConsumeDetailPageVO consumeDetailPageVO, Long familyId) {
        //校验权限
        List<UserSelectDTO> users = userService.selectUsersByFamilyId(familyId);
        if (CollectionUtil.isEmpty(users)) {
            return ResultGenerator.genFailResult("家庭信息有误,未查询到成员信息");
        }
        List<Long> userIds = users.stream().map(UserSelectDTO::getId).collect(Collectors.toList());
        QueryWrapper<ConsumeDetailDO> wrapper = new QueryWrapper<>();
        //时间倒序
        wrapper.lambda()
                .orderByDesc(ConsumeDetailDO::getConsumeDate)
                .orderByDesc(ConsumeDetailDO::getId);
        //获取查询时间段
        Period period = PeriodsUtil.getPeriodByType(consumeDetailPageVO.getConsumeDateType());
        if (!Objects.isNull(period)) {
            consumeDetailPageVO.setConsumeStartDate(period.getStartDate());
            consumeDetailPageVO.setConsumeEndDate(period.getEndDate());
        }
        //查询条件
        if (StringUtils.isEmpty(consumeDetailPageVO.getConsumeBy())) {
            return ResultGenerator.genFailResult("参数有误,消费人不能空");
        }
        //0查家庭所有
        if (consumeDetailPageVO.getConsumeBy() == 0) {
            log.info("消费详情:consumeBy 为0 查询所有");
            //用户id
            wrapper.lambda()
                    .in(ConsumeDetailDO::getConsumeBy, userIds);
        } else {
            //校验
            if (!userIds.contains(consumeDetailPageVO.getConsumeBy())) {
                return ResultGenerator.genFailResult("您没有权限查询此用户的记录");
            }
            log.info("消费详情:consumeBy 不为0 查询:{}", consumeDetailPageVO.getConsumeBy());
            wrapper.lambda()
                    .eq(ConsumeDetailDO::getConsumeBy, consumeDetailPageVO.getConsumeBy());
        }
        if (!StringUtils.isEmpty(consumeDetailPageVO.getConsumeWay())) {
            wrapper.lambda()
                    .eq(ConsumeDetailDO::getConsumeWay, consumeDetailPageVO.getConsumeWay());
        }
        if (consumeDetailPageVO.getConsumeCategoryId() != null) {
            wrapper.lambda()
                    .eq(ConsumeDetailDO::getConsumeCategoryId, consumeDetailPageVO.getConsumeCategoryId());
        }
        if (consumeDetailPageVO.getConsumeStartDate() != null) {
            wrapper.lambda()
                    .ge(ConsumeDetailDO::getConsumeDate, consumeDetailPageVO.getConsumeStartDate());
        }
        if (consumeDetailPageVO.getConsumeEndDate() != null) {
            wrapper.lambda()
                    .le(ConsumeDetailDO::getConsumeDate, consumeDetailPageVO.getConsumeEndDate());
        }
        //条件下的总金额
        Double totalConsumeAmount = consumeDetailMapper.selectTotalConsumeAmount(wrapper);
        Page<ConsumeDetailDO> page = new Page<>(consumeDetailPageVO.getCurrentPage(), consumeDetailPageVO.getPageSize());
        Page<ConsumeDetailDO> consumeDetailPage = consumeDetailMapper.selectPage(page, wrapper);

        Double subtotalConsumeAmount = consumeDetailPage.getRecords().stream().mapToDouble(ConsumeDetailDO::getConsumeAmount).sum();
        BigDecimal subtotalConsumeAmountDecimal = new BigDecimal(subtotalConsumeAmount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);

        List<ConsumeDetailDTO> consumeDetails = consumeDetailPage.getRecords().stream()
                .map(consumeDetailDO -> BeanUtil.copyProperties(consumeDetailDO, ConsumeDetailDTO.class))
                .collect(Collectors.toList());
        setCategoryName(consumeDetails);
        setNickName(consumeDetails);

        ConsumeDetailDTO subTotalConsumeDetailDTO = new ConsumeDetailDTO();
        subTotalConsumeDetailDTO.setConsumeBy(-1L);
        subTotalConsumeDetailDTO.setConsumeAmount(subtotalConsumeAmountDecimal.doubleValue());
        subTotalConsumeDetailDTO.setConsumeCategoryName("-");

        ConsumeDetailDTO totalConsumeDetailDTO = new ConsumeDetailDTO();
        totalConsumeDetailDTO.setConsumeBy(0L);
        totalConsumeDetailDTO.setConsumeAmount(totalConsumeAmount);
        totalConsumeDetailDTO.setConsumeCategoryName("-");

        consumeDetails.add(subTotalConsumeDetailDTO);
        consumeDetails.add(totalConsumeDetailDTO);

        return ResultGenerator.genSuccessResult(new ConsumeDetailPageDTO(consumeDetailPage.getTotal(), consumeDetails));
    }

    private void setNickName(List<ConsumeDetailDTO> consumeDetails) {
        if (CollectionUtil.isEmpty(consumeDetails)) {
            return;
        }

        List<Long> userIds = consumeDetails.stream().map(ConsumeDetailDTO::getConsumeBy).collect(Collectors.toList());
        List<UserDTO> users = userService.selectUsersByIds(userIds);
        if (CollectionUtil.isEmpty(users)) {
            return;
        }
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));
        for (ConsumeDetailDTO consumeDetailDTO : consumeDetails) {
            if (!userMap.containsKey(consumeDetailDTO.getConsumeBy())) {
                continue;
            }
            String nickName = userMap.get(consumeDetailDTO.getConsumeBy()).get(0).getNickName();
            consumeDetailDTO.setNickName(nickName);
        }
    }

    private void setCategoryName(List<ConsumeDetailDTO> consumeDetails) {
        if (!CollectionUtils.isEmpty(consumeDetails)) {
            for (ConsumeDetailDTO consumeDetailDTO : consumeDetails) {
                String categoryName = consumeCategoryMapper.getCategoryNameById(consumeDetailDTO.getConsumeCategoryId());
                consumeDetailDTO.setConsumeCategoryName(categoryName);
            }
        }
    }
}
