package com.xkf.cashbook.jwt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkf.cashbook.common.constant.UserStatus;
import com.xkf.cashbook.common.result.Result;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.common.utils.IpUtil;
import com.xkf.cashbook.common.utils.JacksonUtils;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.xkf.cashbook.common.constant.Constants.KEY_PREFIX_USER_STATUS;

/**
 * @author xukf01
 */
@Component
@Slf4j
@Order(1)
public class JwtRequestFilter extends OncePerRequestFilter {

    @Resource
    protected Environment env;

    @Resource
    private JwtUserDetailsService jwtUserDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.aes-seed}")
    private String aesSeed;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 允许跨域
//        setHeader(request, response);

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("请求接口：{}，请求IP：{}，请求参数：{}",
                request.getRequestURI(), IpUtil.getIpAddress(request), JacksonUtils.obj2json(request.getParameterMap()));

        String username = null;
        String jwtToken = null;
        // JWT Token 需要从 "Bearer {token}" 获取
        if (requestTokenHeader != null && requestTokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            String encryptToken = requestTokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
            try {
                jwtToken = AesEncryptUtils.decrypt(encryptToken, AesEncryptUtils.geneKey(aesSeed));
                log.info("jwtToken:{}", jwtToken);
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.warn("Unable to get JWT Token:" + encryptToken);
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token has expired, token: {}", jwtToken);
            } catch (Exception e) {
                // 解析 JWT 失败
                log.warn("encryptToken cannot decrypt, aesSeed: {}", aesSeed, e);
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }


        //一旦得到token，就进行验证。
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }

            // if token is valid configure Spring Security to manually set authentication
            if (userDetails != null && jwtTokenUtil.validateAccessToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }


    /**
     * 为response设置header，实现跨域
     */
    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        //跨域的header设置
        String originHeader = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", originHeader);
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));

        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
    }

}
