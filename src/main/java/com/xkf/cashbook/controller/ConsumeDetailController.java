package com.xkf.cashbook.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.mysql.model.ConsumeDetailDO;
import com.xkf.cashbook.service.IConsumeDetailService;
import com.xkf.cashbook.pojo.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.pojo.vo.ConsumeDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author xukf01
 */
@RequestMapping("consume/detail")
@RestController
@Slf4j
public class ConsumeDetailController extends BaseController {

    @Resource
    private IConsumeDetailService consumeDetailService;

    @Value("${comments.length.max}")
    private Integer commentsLength;

    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ConsumeDetailVO consumeDetailVO, HttpServletRequest request) {
        if (Objects.isNull(consumeDetailVO.getConsumeCategoryId()) || consumeDetailVO.getConsumeCategoryId() < 1) {
            return ResultGenerator.genFailResult("消费类别不能为空!");
        }
        if (Objects.isNull(consumeDetailVO.getConsumeAmount())) {
            return ResultGenerator.genFailResult("消费金额不能为空!");
        }
        if (Objects.isNull(consumeDetailVO.getConsumeWay())) {
            return ResultGenerator.genFailResult("消费方式不能为空");
        }
        if (Objects.isNull(consumeDetailVO.getConsumeDate())) {
            return ResultGenerator.genFailResult("消费时间不能为空");
        }
        if (!Objects.isNull(consumeDetailVO.getConsumeComment()) && consumeDetailVO.getConsumeComment().length() > commentsLength) {
            return ResultGenerator.genFailResult("消费备注最多"+commentsLength+"个字哦");
        }
        Long userId = getUserId(request);
        if (Objects.isNull(userId)) {
            return ResultGenerator.genFailResult("用户信息异常,ID不能为空");
        }
        //只能修改自己的记录
        if (!Objects.isNull(consumeDetailVO.getId())) {
            ConsumeDetailDO consumeDetailDO = consumeDetailService.getById(consumeDetailVO.getId());
            if (!userId.equals(consumeDetailDO.getConsumeBy())) {
                log.warn("用户:{}尝试修改其他用户:{}的记录", userId, consumeDetailDO.getConsumeBy());
                return ResultGenerator.genFailResult("不能修改其他用户的记录哦");
            }
        }
        ConsumeDetailDO consumeDetailDO = BeanUtil.copyProperties(consumeDetailVO, ConsumeDetailDO.class);
        consumeDetailDO.setRecordDate(LocalDateTime.now());
        consumeDetailDO.setRecordBy(userId);
        consumeDetailDO.setUpdateTime(LocalDateTime.now());
        consumeDetailDO.setConsumeBy(userId);
        boolean result = consumeDetailService.saveOrUpdate(consumeDetailDO);
        if (result) {
            return ResultGenerator.genSuccessResult("操作成功!", null);
        }
        return ResultGenerator.genFailResult("操作失败!");
    }

    @PostMapping("pageDetail")
    public Result pageDetail(@RequestBody ConsumeDetailPageVO consumeDetailPageVO, HttpServletRequest request) {
        Long familyId = getFamilyId(request);
        if (Objects.isNull(familyId)) {
            return ResultGenerator.genFailResult("您还没创建/加入家庭哦");
        }
        return consumeDetailService.pageDetail(consumeDetailPageVO, familyId);
    }


}
