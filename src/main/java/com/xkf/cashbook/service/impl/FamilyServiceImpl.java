package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ApplyStatus;
import com.xkf.cashbook.common.constant.FamilyStatus;
import com.xkf.cashbook.common.constant.UserStatus;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.SmsUtils;
import com.xkf.cashbook.mysql.mapper.FamilyApplyMapper;
import com.xkf.cashbook.mysql.mapper.FamilyMapper;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.FamilyApplyDO;
import com.xkf.cashbook.mysql.model.FamilyDO;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.FamilyApplyDTO;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.dto.UserSelectDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyVO;
import com.xkf.cashbook.service.IFamilyApplyService;
import com.xkf.cashbook.service.IFamilyService;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.xkf.cashbook.common.constant.Constants.*;

/**
 * 家庭服务
 *
 * @author xukf01
 */
@Service
@Slf4j
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, FamilyDO> implements IFamilyService {

    @Resource
    private FamilyMapper familyMapper;

//    @Resource
//    private FamilyApplyMapper familyApplyMapper;

    @Resource
    private UserMapper userMapper;

    @Value("${family.members.max}")
    private Integer maxMembers;

    @Resource
    private TaskExecutor threadPoolTaskExecutor;

    @Resource
    private SmsUtils smsUtils;

    @Resource
    private UserService userService;

    @Resource
    private IFamilyApplyService familyApplyService;

    @Override
    @Transactional
    public Result add(FamilyVO familyVO) {
        FamilyDO familyDO = selectByFamilyName(familyVO.getFamilyName());
        if (!Objects.isNull(familyDO)) {
            return ResultGenerator.genFailResult("您创建的家庭名称已存在!");
        }
        familyDO = BeanUtil.copyProperties(familyVO, FamilyDO.class);
        familyDO.setCreateTime(LocalDateTime.now());
        int insert = familyMapper.insert(familyDO);
        log.info("add family insert:{}", insert);
        if (insert == 1) {
            userService.updateUserStatus(familyDO.getId(), familyVO.getFamilyOwner());
            updateUserNickName(familyVO.getFamilyOwner(), familyVO.getUserNickName());
            return ResultGenerator.genSuccessResult("创建成功,快邀请您的家人一起来记账吧!");
        }
        return ResultGenerator.genFailResult();
    }

    @Override
    @Transactional
    public Result apply(FamilyApplyVO familyApplyVO) {
        //校验是否成员已满
        //todo 建家庭名称唯一索引
        FamilyDO familyDO = selectByFamilyName(familyApplyVO.getApplyFamilyName());
        if (Objects.isNull(familyDO)) {
            return ResultGenerator.genFailResult("您申请的家庭不存在!");
        }
        QueryWrapper<UserDO> userWrapper = new QueryWrapper<>();
        userWrapper.lambda()
                .eq(UserDO::getFamilyId, familyDO.getId())
                .ne(UserDO::getStatus, UserStatus.DISABLE);
        List<UserDO> users = userMapper.selectList(userWrapper);
        if (CollectionUtil.isEmpty(users)) {
            return ResultGenerator.genFailResult("您申请的家庭信息异常!");
        }
        if (users.size() > maxMembers) {
            return ResultGenerator.genFailResult("您申请的家庭成员已满!");
        }
        //校验是否已有初始的申请
        List<FamilyApplyDTO> familyApplys = familyApplyService.selectInitListByFamilyId(familyApplyVO);
        log.info("familyApplys familyName:{},applyUserId:{},size:{}", familyApplyVO.getApplyFamilyName(), familyApplyVO.getApplyUserId(), familyApplys.size());
        if (!CollectionUtil.isEmpty(familyApplys) && familyApplys.size() >= 1) {
            return ResultGenerator.genFailResult("申请已存在,请等待审核!");
        }
        //开始发起申请
        FamilyApplyDO familyApplyDO = BeanUtil.copyProperties(familyApplyVO, FamilyApplyDO.class);
        familyApplyDO.setApplyFamilyId(familyDO.getId())
                .setApplyStatus(ApplyStatus.INIT.getStatus())
                .setApplyTime(LocalDateTime.now())
                .setApproveCode(UUID.randomUUID().toString());
        int insert = familyApplyService.add(familyApplyDO);
        if (insert == 0) {
            return ResultGenerator.genFailResult();
        }
        updateUserNickName(familyApplyVO.getApplyPhoneNumber(), familyApplyVO.getUserNickName());
        //发送审批链接到家庭拥有者
        threadPoolTaskExecutor.execute(() -> {
            try {
                smsUtils.sendFamilyApply(Lists.newArrayList(DOMAIN + "/family/approve?approveCode=" + familyApplyDO.getApproveCode()), familyDO.getFamilyOwner());
            } catch (Exception e) {
                log.error("send family apply err,applyUser:{}", familyApplyVO.getApplyUserId());
            }
        });
        return ResultGenerator.genSuccessResult();
    }

    private void updateUserNickName(String phoneNumber, String nickName) {
        //更新用户昵称
        QueryWrapper<UserDO> userWrapper;
        userWrapper = new QueryWrapper<>();
        userWrapper.lambda()
                .eq(UserDO::getPhoneNumber, phoneNumber);
        UserDO userDO = new UserDO();
        userDO.setNickName(nickName);
        userMapper.update(userDO, userWrapper);
    }

    @Override
    public FamilyDTO selectByPhoneNumber(String phoneNumber) {
        QueryWrapper<FamilyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(FamilyDO::getFamilyOwner, phoneNumber)
                .eq(FamilyDO::getStatus, FamilyStatus.INIT);
        FamilyDO familyDO = familyMapper.selectOne(wrapper);
        if (Objects.isNull(familyDO)) {
            return null;
        }
        return BeanUtil.copyProperties(familyDO, FamilyDTO.class);
    }

    @Override
    public Result getUsers(Long familyId) {
        List<UserSelectDTO> userDTOS = userService.selectUsersByFamilyId(familyId);
        if (CollectionUtil.isEmpty(userDTOS)) {
            return ResultGenerator.genFailResult("数据异常,您的家庭没有成员");
        }
        return ResultGenerator.genSuccessResult(userDTOS);
    }

    private FamilyDO selectByFamilyName(String familyName) {
        QueryWrapper<FamilyDO> familyWrapper = new QueryWrapper<>();
        familyWrapper.lambda()
                .eq(FamilyDO::getFamilyName, familyName)
                .eq(FamilyDO::getStatus, FamilyStatus.INIT.getStatus());
        return familyMapper.selectOne(familyWrapper);
    }
}
