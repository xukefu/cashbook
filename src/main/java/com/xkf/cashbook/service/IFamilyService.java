package com.xkf.cashbook.service;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.pojo.vo.FamilyApplyVO;
import com.xkf.cashbook.pojo.vo.FamilyVO;

public interface IFamilyService {
    Result add(FamilyVO familyVO);

    Result apply(FamilyApplyVO familyApplyVO);

    Result approve(String approveCode);

    FamilyDTO selectByPhoneNumber(String phoneNumber);

}
