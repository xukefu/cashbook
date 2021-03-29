package com.xkf.cashbook.service;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.vo.IncomeCategoryVO;

public interface IncomeCategoryService {

    Result getAll();

    Result add(String categoryName,Long familyId);
}
