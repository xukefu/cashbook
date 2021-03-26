package com.xkf.cashbook.service;

import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.pojo.dto.ConsumeCategoryDTO;
import com.xkf.cashbook.pojo.vo.ConsumeCategoryVO;

import java.util.List;

public interface IConsumeCategoryService {

    List<ConsumeCategoryDTO> getAll(Long familyId);

    Result add(String categoryName,Long familyId);
}
