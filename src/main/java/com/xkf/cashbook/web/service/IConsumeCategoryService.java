package com.xkf.cashbook.web.service;

import com.xkf.cashbook.common.Result;
import com.xkf.cashbook.web.vo.ConsumeCategoryVO;

import java.util.List;

public interface IConsumeCategoryService {

    List<ConsumeCategoryVO> getAll();

    Result add(String categoryName);
}
