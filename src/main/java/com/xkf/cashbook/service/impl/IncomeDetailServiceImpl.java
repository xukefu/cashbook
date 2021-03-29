package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.pojo.dto.*;
import com.xkf.cashbook.mysql.mapper.IncomeCategoryMapper;
import com.xkf.cashbook.mysql.mapper.IncomeDetailMapper;
import com.xkf.cashbook.mysql.model.IncomeCategoryDO;
import com.xkf.cashbook.mysql.model.IncomeDetailDO;
import com.xkf.cashbook.service.IIncomeDetailService;
import com.xkf.cashbook.pojo.vo.IncomeDetailPageVO;
import com.xkf.cashbook.pojo.vo.IncomeDetailVO;
import com.xkf.cashbook.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xukf01
 */
@Service
public class IncomeDetailServiceImpl extends ServiceImpl<IncomeDetailMapper,IncomeDetailDO> implements IIncomeDetailService {
    @Resource
    private IncomeDetailMapper incomeDetailMapper;

    @Resource
    private IncomeCategoryMapper incomeCategoryMapper;

    @Resource
    private UserService userService;

    @Override
    @Transactional
    public Result add(IncomeDetailVO incomeDetailVO) {
        incomeDetailVO.setRecordBy(incomeDetailVO.getIncomeBy());
        incomeDetailVO.setRecordDate(LocalDateTime.now());
        IncomeDetailDO incomeDetailDO = BeanUtil.copyProperties(incomeDetailVO, IncomeDetailDO.class);
        int add = incomeDetailMapper.insert(incomeDetailDO);
        if (add == 1) {
            return ResultGenerator.genSuccessResult("记录成功!", null);
        }
        return ResultGenerator.genFailResult();
    }

    @Override
    public Result pageDetail(IncomeDetailPageVO incomeDetailPageVO,Long familyId) {
        List<UserSelectDTO> users = userService.selectUsersByFamilyId(familyId);
        if (CollectionUtil.isEmpty(users)){
            return ResultGenerator.genFailResult("家庭信息有误,未查询到成员信息");
        }
        List<Long> userIds = users.stream().map(UserSelectDTO::getId).collect(Collectors.toList());
        QueryWrapper<IncomeDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(IncomeDetailDO::getIncomeBy,userIds);
        //时间倒序
        wrapper.lambda()
                .orderByDesc(IncomeDetailDO::getIncomeDate)
                .orderByDesc(IncomeDetailDO::getId);

        Page<IncomeDetailDO> page = new Page<>(incomeDetailPageVO.getCurrentPage(), incomeDetailPageVO.getPageSize());
        Page<IncomeDetailDO> incomeDetailPage = incomeDetailMapper.selectPage(page, wrapper);
        if (incomeDetailPage.getTotal() == 0){
            return ResultGenerator.genSuccessResult();
        }
        List<IncomeDetailDTO> incomeDetails = incomeDetailPage.getRecords().stream()
                .map(incomeDetailDO ->
                        BeanUtil.copyProperties(incomeDetailDO, IncomeDetailDTO.class))
                .collect(Collectors.toList());
        setCategoryName(incomeDetails);
        setNickName(incomeDetails);
        return ResultGenerator.genSuccessResult(new IncomeDetailPageDTO(incomeDetailPage.getTotal(), incomeDetails));
    }

    private void setNickName(List<IncomeDetailDTO> incomeDetails) {
        if (CollectionUtil.isEmpty(incomeDetails)) {
            return;
        }
        List<Long> userIds = incomeDetails.stream().map(IncomeDetailDTO::getIncomeBy).collect(Collectors.toList());
        List<UserDTO> users = userService.selectUsersByIds(userIds);
        if (CollectionUtil.isEmpty(users)) {
            return;
        }
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));
        for (IncomeDetailDTO incomeDetailDTO : incomeDetails) {
            if (!userMap.containsKey(incomeDetailDTO.getIncomeBy())) {
                continue;
            }
            String nickName = userMap.get(incomeDetailDTO.getIncomeBy()).get(0).getNickName();
            incomeDetailDTO.setNickName(nickName);
        }
    }

    private void setCategoryName(List<IncomeDetailDTO> incomeDetails) {
        if (!CollectionUtils.isEmpty(incomeDetails)) {
            List<Long> categoryIds = incomeDetails.stream().map(IncomeDetailDTO::getIncomeCategoryId)
                    .collect(Collectors.toList());
            QueryWrapper<IncomeCategoryDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(IncomeCategoryDO::getId,categoryIds);
            List<IncomeCategoryDO> incomeCategoryDos = incomeCategoryMapper.selectList(wrapper);
            Map<Long, List<IncomeCategoryDO>> categoryMap = incomeCategoryDos.stream().collect(Collectors.groupingBy(IncomeCategoryDO::getId));
            incomeDetails.forEach(incomeDetailDTO -> incomeDetailDTO.setIncomeCategoryName(categoryMap.get(incomeDetailDTO.getIncomeCategoryId()).get(0).getCategoryName()));
        }
    }
}
