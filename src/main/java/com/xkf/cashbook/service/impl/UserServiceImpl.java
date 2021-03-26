package com.xkf.cashbook.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.xkf.cashbook.common.constant.UserStatus;
import com.xkf.cashbook.common.constant.ValidateCodeType;
import com.xkf.cashbook.common.exception.ServiceException;
import com.xkf.cashbook.common.result.ResultCode;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.jwt.JwtTokenUtil;
import com.xkf.cashbook.jwt.JwtUserDetailsService;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.pojo.vo.LoginVO;
import com.xkf.cashbook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.xkf.cashbook.common.constant.Constants.KEY_PREFIX_VALIDATE_CODE;

/**
 * @author xukf01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUserDetailsService userDetailsService;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Value("${password.salt}")
    private String salt;

    @Value("${jwt.aes-seed}")
    private String aesSeed;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Override
    public String login(LoginVO loginVO) {
        //先根据手机号查询是否有账号(含禁用的)
        UserDTO userDto = selectByPhoneNumber(loginVO.getPhoneNumber());
        if (Objects.isNull(userDto)) {
            log.info("用户:{}首次登陆,执行注册流程", loginVO.getPhoneNumber());
            String password = loginVO.getPhoneNumber().substring(5, 11);
            MD5 md5 = new MD5(salt.getBytes());
            password = md5.digestHex(md5.digestHex(password));
            log.info("默认密码后6位手机号:{}", password);
            //TODO 发送短信通知密码
            //没有就注册账号
            UserDO userDO = UserDO.builder()
                    .phoneNumber(loginVO.getPhoneNumber())
                    .password(password)
                    .status(UserStatus.INIT.getStatus())
                    .createTime(LocalDateTime.now())
                    .gender(0)
                    .lastLoginTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            int add = add(userDO);
            if (add == 0) {
                throw new ServiceException(ResultCode.FAIL, "注册失败!");
            }
            userDto = selectByPhoneNumber(loginVO.getPhoneNumber());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginVO.getPhoneNumber());
        log.info("根据手机号load得到userName:{},password:{}", userDetails.getUsername(), userDetails.getPassword());
        //存进security
        String token = encryptJwtToken(userDetails, userDto);
        //返回token
        return token;
    }

    @Override
    public UserDTO selectByPhoneNumber(String phoneNumber) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserDO::getPhoneNumber, phoneNumber);
        List<UserDO> users = userMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }
        return BeanUtil.copyProperties(users.get(0), UserDTO.class);
    }

    private int add(UserDO userDO) {
        return userMapper.insert(userDO);
    }

    /**
     * 加密 token
     *
     * @param userDetails
     * @param userDto
     * @return
     */
    private String encryptJwtToken(UserDetails userDetails, UserDTO userDto) {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put(JwtTokenUtil.NAME_CARD, userDto);
        claims.put(JwtTokenUtil.USER_ID, userDto.getId());
        claims.put(JwtTokenUtil.PHONE_NUMBER, userDto.getPhoneNumber());
        claims.put(JwtTokenUtil.USER_STATUS, userDto.getStatus());
        claims.put(JwtTokenUtil.FAMILY_ID, userDto.getFamilyId());
        String token = jwtTokenUtil.generateAccessToken(userDetails, claims);
        try {
            SecretKey geneKey = AesEncryptUtils.geneKey(aesSeed);
            token = AesEncryptUtils.encrypt(token, geneKey);
            log.info("token:{}", token);
        } catch (Exception e) {
            log.error("jwt 加密失败， cellphone {}, token {}", userDto.getPhoneNumber(), token);
            throw new ServiceException(ResultCode.FAIL, ResultGenerator.SERVICE_ERROR_MESSAGE);
        }
        return token;
    }
}
