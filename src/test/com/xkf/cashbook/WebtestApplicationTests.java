package com.xkf.cashbook;

import com.xkf.cashbook.task.DailyBakDataTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.xkf.cashbook.mysql.mapper")
public class WebtestApplicationTests {

    @Resource
    private DailyBakDataTask dailyBakDataTask;

    @Test
    public void test1() {
        dailyBakDataTask.bakData();
    }
}
