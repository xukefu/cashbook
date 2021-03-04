package com.xkf.cashbook;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.xkf.cashbook.common.exception.ServiceException;
import com.xkf.cashbook.common.result.ResultCode;
import com.xkf.cashbook.common.result.ResultGenerator;
import com.xkf.cashbook.common.utils.AesEncryptUtils;
import com.xkf.cashbook.common.utils.JacksonUtils;
import com.xkf.cashbook.jwt.JwtTokenUtil;
import com.xkf.cashbook.jwt.JwtUserDetailsService;
import com.xkf.cashbook.mysql.mapper.UserMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import com.xkf.cashbook.pojo.dto.UserDTO;
import com.xkf.cashbook.service.UserService;
import com.xkf.cashbook.task.DailyBakDataTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.xkf.cashbook.mysql.mapper")
public class WebtestApplicationTests {


    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUserDetailsService userDetailsService;

    @Resource
    protected Environment env;

    @Value("${jwt.aes-seed}")
    private String aesSeed;

    @Test
    public void test1() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("18823748161");
        UserDTO userDto = getByPhoneNumber("18823748161");

        String token = encryptJwtToken(userDetails, userDto);

        String decrypt = AesEncryptUtils.decrypt(token, AesEncryptUtils.geneKey(aesSeed));
        System.out.println(decrypt);
        UserDTO nameCard = jwtTokenUtil.getNameCard(decrypt);
        System.out.println(nameCard);
    }

    private UserDTO getByPhoneNumber(String phoneNumber) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserDO::getPhoneNumber, phoneNumber);
        List<UserDO> users = userMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }
        UserDTO userDTO = BeanUtil.copyProperties(users.get(0), UserDTO.class);
        return userDTO;
    }

    private String encryptJwtToken(UserDetails userDetails, UserDTO userDto) {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put(JwtTokenUtil.NAME_CARD, userDto);
        claims.put(JwtTokenUtil.USER_ID, userDto.getId());
        String token = jwtTokenUtil.generateAccessToken(userDetails, claims);
        try {
            SecretKey geneKey = AesEncryptUtils.geneKey(aesSeed);
            token = AesEncryptUtils.encrypt(token, geneKey);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAIL, ResultGenerator.SERVICE_ERROR_MESSAGE);
        }
        return token;
    }

    @Test
    public void test2() throws Exception {
        String token = "Bearer Jv2cMgLr+mrQbhZSO4GltIpxytMR9T7Lvp3TZ1Dz8TpX42FPyMoJ89PHpRdOdgc8t/GZlFRXkEFfA1plFTytJ9o5Z06M6hXucIZKchC1Q26I1z+ZgGZM604NGL9L8vLiEMrgXivVfXsFTBESm3Lr7458jRd9Z2znSb1F37tb9dlG19p5VIlbOW5RY1qe4NnlvzXQYXiIaIFMWnZdDUpRlIoyYuImrgNmiQU8txX/tqZhfV23kWZk6rRSqrBAyeQkogRaYqfzq1QjLIeJHoTejD7N8eWiCBXq153KlMgL4VQjdYFfr30weBKq0Z4KeiAwVreH8b9blZlyjnQC0f6szMW47MKsv8Jht2CM7BDJvXtgfAIWnkwY06kLm5kKba2E4LIk65Y1xZKrN9HGsP7WbU12BurBA5MkrYOvBFsBn1vrrEYv6fFRO91iow4LhrPioghzvPlFW1npU/g3JcpxyFaA3kK3zdNhp6AF5xXLWfPIKb6FJSW9jaOzA2cVTjgg4uOLAJmuMGpJCdMKPHThV0g+rHW2wO1xsFm27CjOt4Jg37Vc1C6zg5j7PqEf7adFgVyBsROuQlcYPnzO0JG2rxD7B+rEL87Uolx4kXGUF+t0dy0aPfOh2E2N6AseTJk1xBj1w+tw/noKKfSablA7dkY9BO3rSbpMQo1JVzUtsHsTblG7ZMD2ZbNn8IeAA5jPNfGVgCyFykj34p2Wy0PzAfhv2om4tKl190uZw5rHVp7RaO4cXwobOw7WXSnlllxYagItWpPt3u9rzqSDLJfQhr09km07XrQ2pkboxKjBv7S3KrZ0P0h/K1fGMGJKs8Q7eiiGsWxtWVnQI29BWwI6gfSX5VNPaqwG/anP5/DKAv7xzkg+aCJ7iT1gi4WYlUejlu9QWNkBetQeiCVTAs7o4oxxoazarGT86zCI5elf7WxxrNyFHVQqsyQnq6YNQDYuM+gP3sxJxOKZqgzFg0llCXofhYPmrGHwGXHkBf8OMRJOr+DydFPxhnDBq40Zb3W9GHwswkNu6emNF/utP+3BCWKfQdUSjjww5eMWccpiwJa5L4VbMnkyVnDskRB82+DhNYxkgCYgww5zWBAyVkIToPVgu08p+q6+5zjRqC96dBwSin6sSAxYYrZmu/q2c2WeTe9R8wwiyA7uc4a2KzhWZ5Z5R9DphC+fLQJvFjqDlkKe3XTivZbz+sFLqOEC1FNvlspCBibD9AkPU+Sgtu1VuzSBEx/7c0XEmwWjdEZVtchDzT1pXO2moswTtla5Nc5m5tXAf8qezINlGTnCXtB0eXzDw3sLa8Z3x44Vkx4jwhfUubeE7FMeQQ68Av1aSFf2soqivQXnWFG89oUgYOHOpqu+IKA7ADQr1RXz60NsO0ppSs1iD1J7NhyaZKZGL1De/jgDsQAIAWBIKY4wjQb2rm2Pu2ubDm8eq1iezjN/iWlEanL3DO2b5dwO8Ffxvq3f2sEXawMqdlHfybbStTamSXqfnsciZmR7fG/BolrCmM7J4My6RfhVQ9TPdPpBEZdJtfrXNTV3ZAB5sDoPmw8YQGz0ZI67nHNbe/0M7zmGqWm+PrX9qjwO1Cj+bqGw4VKZ";
        String encryptToken = token.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        String aesSeed = env.getProperty(JwtTokenUtil.JWT_AES_SEED_KEY);
//        String jwtToken = AesEncryptUtils.decrypt(encryptToken, AesEncryptUtils.geneKey(aesSeed));
        String jwtToken = AesEncryptUtils.decrypt(encryptToken, AesEncryptUtils.geneKey(JwtTokenUtil.JWT_AES_SEED_KEY));
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        System.out.println(username);
    }



}
