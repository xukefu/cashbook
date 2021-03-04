package com.xkf.cashbook.jwt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
	@Resource
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(phoneNumber)) {
			throw new UsernameNotFoundException("用户名不能为空!");
		}
		QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(UserDO::getPhoneNumber,Long.parseLong(phoneNumber));
		UserDO user = userMapper.selectOne(wrapper);
		if (user == null) {
			log.warn("loadUserByUsername 无效账号: " + phoneNumber);
			throw new UsernameNotFoundException("用户不存在！");
		}
		return new User(phoneNumber, user.getPassword(), new ArrayList<>());
	}

}