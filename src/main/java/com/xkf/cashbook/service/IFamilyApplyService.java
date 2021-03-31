package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.mysql.model.FamilyApplyDO;
import com.xkf.cashbook.pojo.dto.FamilyApplyDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyApproveVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xukf01
 */
public interface IFamilyApplyService extends IService<FamilyApplyDO> {

    /**
     * 获取审批记录
     *
     * @param familyId
     * @return
     */
    Result selectListByFamilyId(Long familyId, LocalDateTime familyCreateTime);

    /**
     * 获取未同意的审批记录
     *
     * @param familyApplyVO
     * @return
     */
    List<FamilyApplyDTO> selectInitListByFamilyId(FamilyApplyVO familyApplyVO);

    /**
     * 新增
     *
     * @param familyApplyDO
     * @return
     */
    int add(FamilyApplyDO familyApplyDO);


    Result approve(FamilyApproveVO familyApproveVO);

    Result markRead(FamilyApproveVO familyApproveVO);

    Result selectListByApplyStatus(List<Integer> applyStatus,String phoneNumber,LocalDateTime familyCreateTime,Long familyId);
}
