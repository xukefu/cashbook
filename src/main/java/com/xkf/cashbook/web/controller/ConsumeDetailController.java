package com.xkf.cashbook.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.sun.mail.imap.protocol.ID;
import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.common.ResultGenerator;
import com.xkf.cashbook.web.mysql.ConsumeDetailDO;
import com.xkf.cashbook.web.service.IConsumeDetailService;
import com.xkf.cashbook.web.vo.ConsumeDetailPageVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author xukf01
 */
@RequestMapping("consume/detail")
@Controller
public class ConsumeDetailController {

    @Resource
    private IConsumeDetailService consumeDetailService;

    @PostMapping("saveOrUpdate")
    @ResponseBody
    public Result saveOrUpdate(@RequestBody ConsumeDetailVO consumeDetailVO) {
        if (Objects.isNull(consumeDetailVO.getConsumeCategoryId()) || consumeDetailVO.getConsumeCategoryId() < 1) {
            return ResultGenerator.genFailResult("消费类别不能为空!");
        }
        if (consumeDetailVO.getConsumeAmount() == null) {
            return ResultGenerator.genFailResult("金额不能为空!");
        }
        ConsumeDetailDO consumeDetailDO = BeanUtil.copyProperties(consumeDetailVO, ConsumeDetailDO.class);
        consumeDetailDO.setRecordDate(LocalDateTime.now());
        consumeDetailDO.setRecordBy(consumeDetailVO.getConsumeBy());
        consumeDetailDO.setUpdateTime(LocalDateTime.now());
        boolean result = consumeDetailService.saveOrUpdate(consumeDetailDO);
        if (result) {
            return ResultGenerator.genSuccessResult("操作成功!",null);
        }
        return ResultGenerator.genFailResult("操作失败!");
    }

    @GetMapping("getLastDetail")
    @ResponseBody
    public Result getLastDetail(@RequestParam(value = "size") int size) {
        return consumeDetailService.getLastDetail(size);
    }

    @PostMapping("pageDetail")
    @ResponseBody
    public Result pageDetail(@RequestBody ConsumeDetailPageVO consumeDetailPageVO) {
        return ResultGenerator.genSuccessResult(consumeDetailService.pageDetail(consumeDetailPageVO));
    }

}
