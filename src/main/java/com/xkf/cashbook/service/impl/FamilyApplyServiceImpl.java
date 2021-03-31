package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ApplyStatus;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.mapper.FamilyApplyMapper;
import com.xkf.cashbook.mysql.model.FamilyApplyDO;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.FamilyApplyDTO;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyApproveVO;
import com.xkf.cashbook.service.IFamilyApplyService;
import com.xkf.cashbook.service.IFamilyService;
import com.xkf.cashbook.service.UserService;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xukf01
 */
@Service
public class FamilyApplyServiceImpl extends ServiceImpl<FamilyApplyMapper, FamilyApplyDO> implements IFamilyApplyService {

    @Resource
    private FamilyApplyMapper familyApplyMapper;

    @Resource
    private UserService userService;

    @Resource
    private IFamilyService familyService;

    @Override
    public Result selectListByFamilyId(Long familyId, LocalDateTime familyCreateTime) {

        QueryWrapper<FamilyApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(FamilyApplyDO::getApplyFamilyId, familyId)
                .orderByAsc(FamilyApplyDO::getApplyStatus)
                .orderByDesc(FamilyApplyDO::getApplyTime);
        List<FamilyApplyDO> familyApplyDOS = familyApplyMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(familyApplyDOS)) {
            return ResultGenerator.genSuccessResult();
        }
        //获取用户昵称
        Set<Long> userIds = familyApplyDOS.stream()
                .map(FamilyApplyDO::getApplyUserId)
                .collect(Collectors.toSet());
        QueryWrapper<UserDO> userWrapper = new QueryWrapper<>();
        userWrapper.lambda()
                .in(UserDO::getId, userIds);
        List<UserDO> users = userService.list(userWrapper);
        Map<Long, List<UserDO>> userMap = users.stream().collect(Collectors.groupingBy(UserDO::getId));

        List<FamilyApplyDTO> familyApplyDTOS = familyApplyDOS.stream()
                .map(familyApplyDO -> {
                    FamilyApplyDTO familyApplyDTO = BeanUtil.copyProperties(familyApplyDO, FamilyApplyDTO.class);
                    familyApplyDTO.setApplyTime(familyApplyDO.getApplyTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    if (userMap.containsKey(familyApplyDTO.getApplyUserId())) {
                        String nickName = userMap.get(familyApplyDTO.getApplyUserId()).get(0).getNickName();
                        familyApplyDTO.setApplyUserNickName(nickName);
                        boolean isFamilyOwner = familyCreateTime.isBefore(familyApplyDO.getApplyTime());
                        familyApplyDTO.setFamilyOwner(isFamilyOwner);
                    }
                    return familyApplyDTO;
                })
                .collect(Collectors.toList());
        return ResultGenerator.genSuccessResult("", familyApplyDTOS);
    }

    @Override
    public List<FamilyApplyDTO> selectInitListByFamilyId(FamilyApplyVO familyApplyVO) {
        //校验是否已初始的申请
        QueryWrapper<FamilyApplyDO> familyApplyWrapper = new QueryWrapper<>();
        familyApplyWrapper.lambda()
                .eq(FamilyApplyDO::getApplyUserId, familyApplyVO.getApplyUserId())
                .eq(FamilyApplyDO::getApplyFamilyName, familyApplyVO.getApplyFamilyName())
                .in(FamilyApplyDO::getApplyStatus, Lists.newArrayList(ApplyStatus.INIT.getStatus()));
        List<FamilyApplyDO> familyApplys = familyApplyMapper.selectList(familyApplyWrapper);
        return familyApplys.stream().map(familyApplyDO -> BeanUtil.copyProperties(familyApplyDO, FamilyApplyDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(FamilyApplyDO familyApplyDO) {
        return familyApplyMapper.insert(familyApplyDO);
    }

    @Override
    @Transactional
    public Result approve(FamilyApproveVO familyApproveVO) {
        QueryWrapper<FamilyApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(FamilyApplyDO::getApproveCode, familyApproveVO.getApproveCode())
                .and(r -> r.eq(FamilyApplyDO::getApplyStatus, ApplyStatus.INIT.getStatus())
                        .or(j -> j.eq(FamilyApplyDO::getApplyStatus, ApplyStatus.IS_READ.getStatus())));
        FamilyApplyDO familyApplyDO = new FamilyApplyDO();
        familyApplyDO
                .setApplyStatus(familyApproveVO.getApproveStatus())
                .setApproveTime(LocalDateTime.now());

        FamilyApplyDO familyApply = familyApplyMapper.selectOne(wrapper);
        if (Objects.isNull(familyApply)) {
            return ResultGenerator.genFailResult("审核信息有误!");
        }
        //判断亲友是否已有家庭
        FamilyDTO familyDTO = familyService.selectByPhoneNumber(familyApply.getApplyPhoneNumber());
        if (!Objects.isNull(familyDTO)) {
            return ResultGenerator.genFailResult("亲友已加入其他家庭");
        }
        int update = familyApplyMapper.update(familyApplyDO, wrapper);

        if (ApplyStatus.ACTIVE.getStatus().equals(familyApproveVO.getApproveStatus()) && update == 1) {
            return userService.updateUserStatus(familyApply.getApplyFamilyId(), familyApply.getApplyPhoneNumber());
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result markRead(FamilyApproveVO familyApproveVO) {
        QueryWrapper<FamilyApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(FamilyApplyDO::getApplyStatus, ApplyStatus.INIT.getStatus())
                .eq(FamilyApplyDO::getApproveCode, familyApproveVO.getApproveCode());
        FamilyApplyDO familyApplyDO = new FamilyApplyDO();
        familyApplyDO.setApplyStatus(ApplyStatus.IS_READ.getStatus());
        int update = familyApplyMapper.update(familyApplyDO, wrapper);
        if (update == 1) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("已读失败");
    }

    @Override
    public Result selectListByApplyStatus(List<Integer> applyStatus, String phoneNumber, LocalDateTime familyCreateTime,Long familyId) {
        QueryWrapper<FamilyApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(FamilyApplyDO::getApplyPhoneNumber, phoneNumber)
                .in(FamilyApplyDO::getApplyStatus, applyStatus)
                //查询自己是成员的时候发起的申请，所以不能是自己的家庭id
                .ne(FamilyApplyDO::getApplyFamilyId,familyId);
        List<FamilyApplyDO> familyApplyDOS = familyApplyMapper.selectList(wrapper);
        List<FamilyApplyDTO> familyApplyDTOS = familyApplyDOS.stream()
                .map(familyApplyDO -> {
                    FamilyApplyDTO familyApplyDTO = BeanUtil.copyProperties(familyApplyDO, FamilyApplyDTO.class);
                    familyApplyDTO.setApplyTime(familyApplyDO.getApplyTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    boolean isFamilyOwner = familyCreateTime.isBefore(familyApplyDO.getApplyTime());
                    familyApplyDTO.setFamilyOwner(isFamilyOwner);
                    return familyApplyDTO;
                })
                .collect(Collectors.toList());
        return ResultGenerator.genSuccessResult("", familyApplyDTOS);
    }

}
