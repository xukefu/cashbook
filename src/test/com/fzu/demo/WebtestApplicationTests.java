package com.xkf.cashbook;

import com.xkf.cashbook.common.AlgorithmUtils;
import com.xkf.cashbook.web.entity.GameEntity;
import com.xkf.cashbook.web.entity.UserEntity;
import com.xkf.cashbook.web.mapper.GameMapper;
import com.xkf.cashbook.web.mapper.UserMapper;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.xkf.cashbook.web.mapper")
public class WebtestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GameMapper gameMapper;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void contextLoads() throws Exception {
        System.out.println(gameMapper.getGameSale());
    }

}
