package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.mysql.model.FamilyDO;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyApproveVO;
import com.xkf.cashbook.pojo.vo.FamilyVO;

public interface IFamilyService extends IService<FamilyDO> {
    Result add(FamilyVO familyVO);

    Result apply(FamilyApplyVO familyApplyVO);

    FamilyDTO selectByPhoneNumber(String phoneNumber);

    Result getUsers(Long familyId);
}
