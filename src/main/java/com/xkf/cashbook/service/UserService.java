package com.xkf.cashbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.vo.LoginVO;

public interface UserService extends IService<UserDO> {

    String login(LoginVO loginVO);
}
