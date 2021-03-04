package com.xkf.cashbook.jwt;

import com.xkf.cashbook.common.exception.ServiceException;
import com.xkf.cashbook.common.result.ResultCode;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.JacksonUtils;
import com.xkf.cashbook.pojo.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;

import static com.xkf.cashbook.common.constant.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


/**
 * @author xukf01
 */
@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    /**
     * jwt properties key
     */
    public static final String JWT_SECRET_KEY = "jwt.secret";
    public static final String JWT_AES_SEED_KEY = "jwt.aes-seed";
    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * jwt claim
     */
    public static final String NAME_CARD = "ncard";
    public static final String USER_ID = "uid";
    public static final String FAMILY_ID = "fid";

    private static final long serialVersionUID = -2550185165626007488L;
    /**
     * access token 有效期1天；refresh token 有效期7天
     */
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 24 * 60 * 60;
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Resource
    private Environment env;

    public String getUsernameFromToken(String token) {
        String username = getClaimFromToken(token, Claims::getSubject);
        if (StringUtils.isEmpty(username)) {
            log.error("jws数据解析异常，无法解析 username，token： {}", token);
            throw new IllegalArgumentException(ResultGenerator.LOGIN_EXPIRATION_MESSAGE);
        }
        log.info("jws数据解析,username:{}",username);
        return username;
    }

	public UserDTO getNameCard(String token) {
		Object claim = getAllClaimsFromToken(token).get(NAME_CARD);
		if (claim != null) {
            return JacksonUtils.json2pojo(JacksonUtils.obj2json(claim),UserDTO.class);
		}
		// 无法获取用户信息
        throw new ServiceException(ResultCode.FAIL, "登录已过期,请刷新后重新登录!");
	}

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAt(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return getJwsFromToken(token).getBody();
    }

    private JwsHeader getHeaderFromToken(String token) {
        return getJwsFromToken(token).getHeader();
    }

    /**
     * 作用：Reading a JWS
     *
     * @param token
     * @return
     */
    private Jws<Claims> getJwsFromToken(String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
            return jws;
        } catch (JwtException ex) {
            // distrust jwt
            // we *cannot* use the JWT as intended by its creator
            log.warn("不被信任的 JWT: {}", token, ex);
            throw new IllegalArgumentException("不被信任的 TOKEN");
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    /**
     * 作用：过滤无效 token
     * 1. 修改密码，操作前该用户所有登录生成token失效
     * 2. 用户登出，当前token添加至黑名单
     *
     * @param token
     * @return
     */
    private Boolean invalidToken(String token) {
        final Date issuedAt = getIssuedAt(token);
        final String username = getUsernameFromToken(token);

        // 修改密码
        String keyExpiration = KEY_PREFIX_TOKEN_EXPIRATION + username;
        if (redisTemplate.hasKey(keyExpiration)) {
            String disableToken = redisTemplate.opsForValue().get(keyExpiration);
            Date disableTime = new Date(Long.valueOf(disableToken));
            return issuedAt.before(disableTime);
        }

        // 用户登出
        String keyBlacklist = KEY_PREFIX_TOKEN_BLACKLIST + username + ":" + Md5Crypt.md5Crypt(token.getBytes());
        if (redisTemplate.hasKey(keyBlacklist)) {
            return true;
        }
        return false;
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> claims) {
        return doGenerateToken(claims, userDetails.getUsername(),
                new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000));
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Date expiration) {
        return Jwts.builder()
                .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration).signWith(getSigningKey()).compact();
    }

    private SecretKey getSigningKey() {
        String secret = env.getProperty(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    /**
     * 作用：校验 token 是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) && !invalidToken(token);
    }

    public Boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
