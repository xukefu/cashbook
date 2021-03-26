package com.xkf.cashbook.controller;


import com.google.common.base.Strings;
import com.xkf.cashbook.common.exception.ServiceException;
import com.xkf.cashbook.common.result.ResultCode;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.jwt.JwtTokenUtil;
import com.xkf.cashbook.pojo.dto.FamilyDTO;
import com.xkf.cashbook.service.IFamilyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * base
 *
 * @author xukf01
 */
@Slf4j
public class BaseController {

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Resource
    private IFamilyService familyService;

    @Value("${jwt.aes-seed}")
    private String aesSeed;

    protected String getPhoneNumber(HttpServletRequest request) {
        String token = decryptJwtToken(request);
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    protected Long getUserId(HttpServletRequest request) {
        String token = decryptJwtToken(request);
        return jwtTokenUtil.getUserId(token);
    }

    protected Long getFamilyId(HttpServletRequest request) {
        String token = decryptJwtToken(request);
        Long familyId = jwtTokenUtil.getFamilyId(token);
        if (familyId == null || familyId == 0) {
            log.warn("token familyId is empty,begin select db");
            FamilyDTO familyDTO = familyService.selectByPhoneNumber(getPhoneNumber(request));
            if (Objects.isNull(familyDTO)) {
                log.error("getFamilyId -> familyDTO is null ;familyId:{}", familyId);
                return null;
            }
            familyId = familyDTO.getId();
        }
        return familyId;
    }

    /**
     * 解密 token
     *
     * @param request
     * @return
     */
    protected String decryptJwtToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!Strings.isNullOrEmpty(header)) {
            String token = header.replace(JwtTokenUtil.TOKEN_PREFIX, "");
            try {
                SecretKey geneKey = AesEncryptUtils.geneKey(aesSeed);
                return AesEncryptUtils.decrypt(token, geneKey);
            } catch (Exception e) {
                log.error("jwt 解密失败，token {}", token);
            }
        }
        throw new ServiceException(ResultCode.FAIL, "解密失败");
    }
}
