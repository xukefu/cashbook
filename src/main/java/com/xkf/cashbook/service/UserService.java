package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.dto.UserSelectDTO;
import com.xkf.cashbook.pojo.vo.LoginVO;

import java.util.List;

public interface UserService extends IService<UserDO> {

    String login(LoginVO loginVO);

    UserDTO selectByPhoneNumber(String phoneNumber);

    List<UserSelectDTO> selectUsersByFamilyId(Long familyId);

    List<UserDTO> selectUsersByIds(List<Long> userIds);

    Result updateUserStatus(Long familyId, String phoneNumber);
}
