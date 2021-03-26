package com.xkf.cashbook.controller;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyVO;
import com.xkf.cashbook.service.IFamilyService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 家庭
 *
 * @author xukf01
 */
@RestController
@RequestMapping("family")
public class FamilyController extends BaseController {

    @Resource
    private IFamilyService familyService;

    @PostMapping("add")
    public Result add(@RequestBody FamilyVO familyVO, HttpServletRequest request) {
        String phoneNumber = getPhoneNumber(request);
        if (StringUtils.isEmpty(familyVO.getFamilyOwner()) || !familyVO.getFamilyOwner().equals(phoneNumber)) {
            return ResultGenerator.genUnAuthorizedResult();
        }
        if (StringUtils.isEmpty(familyVO.getFamilyName()) || StringUtils.isEmpty(familyVO.getUserNickName())) {
            return ResultGenerator.genFailResult("参数有误");
        }
        return familyService.add(familyVO);
    }

    @PostMapping("apply")
    public Result apply(@RequestBody FamilyApplyVO familyApplyVO, HttpServletRequest request) {
        String phoneNumber = getPhoneNumber(request);
        Long userId = getUserId(request);
        if (StringUtils.isEmpty(familyApplyVO.getApplyPhoneNumber()) || !familyApplyVO.getApplyPhoneNumber().equals(phoneNumber)) {
            return ResultGenerator.genUnAuthorizedResult();
        }
        if (StringUtils.isEmpty(familyApplyVO.getApplyFamilyName()) || StringUtils.isEmpty(familyApplyVO.getUserNickName())) {
            return ResultGenerator.genFailResult("参数有误");
        }
        familyApplyVO.setApplyUserId(userId);
        return familyService.apply(familyApplyVO);
    }

    @GetMapping("approve")
    public Result approve(@RequestParam(value = "approveCode") String approveCode) {
        if (StringUtils.isEmpty(approveCode)) {
            return ResultGenerator.genFailResult();
        }
        return familyService.approve(approveCode);
    }
}
