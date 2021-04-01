package com.xkf.cashbook.controller;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyVO;
import com.xkf.cashbook.service.IFamilyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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

    @Value("${common.length.max}")
    private Integer nameLength;

    @PostMapping("add")
    public Result add(@RequestBody FamilyVO familyVO, HttpServletRequest request) {
        String phoneNumber = getPhoneNumber(request);
        if (StringUtils.isEmpty(familyVO.getFamilyOwner()) || !familyVO.getFamilyOwner().equals(phoneNumber)) {
            return ResultGenerator.genUnAuthorizedResult();
        }
        if (StringUtils.isEmpty(familyVO.getFamilyName()) || StringUtils.isEmpty(familyVO.getUserNickName())) {
            return ResultGenerator.genFailResult("参数有误");
        }
        if (familyVO.getFamilyName().length() > nameLength) {
            return ResultGenerator.genFailResult("家庭名称最多" + nameLength + "个字哦");
        }
        if (familyVO.getUserNickName().length() > nameLength) {
            return ResultGenerator.genFailResult("用户昵称最多" + nameLength + "个字哦");
        }
        return familyService.add(familyVO);
    }

    @PostMapping("apply")
    public Result apply(@RequestBody FamilyApplyVO familyApplyVO, HttpServletRequest request) {
        String phoneNumber = getPhoneNumber(request);
        Long userId = getUserId(request);
        if (StringUtils.isEmpty(familyApplyVO.getApplyPhoneNumber()) || !familyApplyVO.getApplyPhoneNumber().equals(phoneNumber)) {
            return ResultGenerator.genFailResult("参数有误,请尝试退出重新登录");
        }
        if (StringUtils.isEmpty(familyApplyVO.getApplyFamilyName()) || StringUtils.isEmpty(familyApplyVO.getUserNickName())) {
            return ResultGenerator.genFailResult("参数有误");
        }
        if (familyApplyVO.getApplyFamilyName().length() > nameLength) {
            return ResultGenerator.genFailResult("家庭名称最多" + nameLength + "个字哦");
        }
        if (familyApplyVO.getUserNickName().length() > nameLength) {
            return ResultGenerator.genFailResult("用户昵称最多" + nameLength + "个字哦");
        }
        familyApplyVO.setApplyUserId(userId);
        return familyService.apply(familyApplyVO);
    }


    @GetMapping("getUsers")
    public Result getUsers(HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)) {
            return ResultGenerator.genFailResult("参数有误,家庭id不能为空");
        }
        return familyService.getUsers(familyId);
    }
}
