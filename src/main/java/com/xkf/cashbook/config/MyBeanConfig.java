package com.xkf.cashbook.config;

import cn.ucloud.common.pojo.Account;
import cn.ucloud.common.pojo.UcloudConfig;
import cn.ucloud.usms.client.DefaultUSMSClient;
import com.xkf.cashbook.CashBookApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(basePackageClasses = CashBookApplication.class)
public class MyBeanConfig {

    @Value("${usms.privateKey}")
    private String privateKey;

    @Value("${usms.publicKey}")
    private String publicKey;

    @Bean
    public DefaultUSMSClient initUmsClient() {
        return new DefaultUSMSClient(new UcloudConfig(new Account(privateKey, publicKey)));
    }

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        executor.setQueueCapacity(100);
        //线程池维护线程的最少数量
        executor.setCorePoolSize(10);
        //线程池维护线程的最大数量
        executor.setMaxPoolSize(100);
        //核心线程出之外的线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(30 * 60);
        //线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

}
