package com.xkf.cashbook;

import com.xkf.cashbook.web.task.DailyBakDataTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.xkf.cashbook.web.mapper")
public class WebtestApplicationTests {

    @Resource
    private DailyBakDataTask dailyBakDataTask;

    @Test
    public void test1() {
        dailyBakDataTask.bakData();
    }
}
