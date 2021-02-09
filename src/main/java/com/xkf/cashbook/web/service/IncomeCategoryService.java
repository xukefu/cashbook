package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.vo.IncomeCategoryVO;

public interface IncomeCategoryService {

    Result getAll();

    Result add(String categoryName);
}
