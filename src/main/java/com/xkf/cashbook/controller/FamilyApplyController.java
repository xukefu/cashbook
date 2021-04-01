package com.xkf.cashbook.controller;

import com.google.common.collect.Lists;
import com.xkf.cashbook.common.constant.ApplyStatus;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.FamilyDO;
import com.xkf.cashbook.pojo.dto.FamilyApplyDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyApproveVO;
import com.xkf.cashbook.service.IFamilyApplyService;
import com.xkf.cashbook.service.IFamilyService;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xukf01
 */
@RestController
@Slf4j
@RequestMapping("/familyApply")
public class FamilyApplyController extends BaseController {

    @Resource
    private IFamilyApplyService familyApplyService;

    @Resource
    private IFamilyService familyService;

    @RequestMapping("getList")
    public Result getList(HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)) {
            return ResultGenerator.genFailResult("您还没创建/加入任何家庭哦");
        }
        String phoneNumber = getPhoneNumber(request);
        //判断身份
        FamilyDO familyDO = familyService.getById(familyId);
        if (Objects.isNull(familyDO)) {
            log.error("家庭不存在，familyId：{}", familyId);
            return ResultGenerator.genFailResult("家庭不存在哦");
        }
        LocalDateTime familyCreateTime = familyDO.getCreateTime();
//        boolean isFamilyOwner = phoneNumber.equals(familyDO.getFamilyOwner());
        //群主 -> 查询审批信息 + 曾经是成员发出的申请
        Result ownerResult = familyApplyService.selectListByFamilyId(familyId, familyCreateTime);
        //群员 -> 查询审批通过、拒绝信息
        Result notOwnerResult = familyApplyService.selectListByApplyStatus(Lists.newArrayList(ApplyStatus.ACTIVE.getStatus(), ApplyStatus.DISABLE.getStatus())
                , phoneNumber, familyCreateTime,familyId);
        if (Objects.isNull(ownerResult)) {
            return notOwnerResult;
        }
        List<FamilyApplyDTO> ownerApplies = (List<FamilyApplyDTO>) ownerResult.getData();
        List<FamilyApplyDTO> notOwnerResultData = (List<FamilyApplyDTO>) notOwnerResult.getData();
        ArrayList<FamilyApplyDTO> resultApplies = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(ownerApplies)) {
            resultApplies.addAll(ownerApplies);
        }
        if (!CollectionUtils.isEmpty(notOwnerResultData)) {
            resultApplies.addAll(notOwnerResultData);
        }
        return ResultGenerator.genSuccessResult(resultApplies);
    }

    @PostMapping("approve")
    public Result approve(@RequestBody FamilyApproveVO familyApproveVO) {
        if (Objects.isNull(familyApproveVO) || StringUtils.isEmpty(familyApproveVO.getApproveCode()) || Objects.isNull(familyApproveVO.getApproveStatus())) {
            return ResultGenerator.genFailResult();
        }
        if (!ApplyStatus.ACTIVE.getStatus().equals(familyApproveVO.getApproveStatus())
                && !ApplyStatus.DISABLE.getStatus().equals(familyApproveVO.getApproveStatus())
        ) {
            return ResultGenerator.genFailResult("参数有误");
        }
        return familyApplyService.approve(familyApproveVO);
    }

    @PostMapping("read")
    public Result markRead(@RequestBody FamilyApproveVO familyApproveVO) {
        if (Objects.isNull(familyApproveVO) || StringUtils.isEmpty(familyApproveVO.getApproveCode()) || Objects.isNull(familyApproveVO.getApproveStatus())) {
            return ResultGenerator.genFailResult();
        }
        if (!ApplyStatus.IS_READ.getStatus().equals(familyApproveVO.getApproveStatus())) {
            return ResultGenerator.genFailResult("参数有误");
        }
        return familyApplyService.markRead(familyApproveVO);
    }

}
