package com.xkf.cashbook.web.task;


import cn.hutool.core.date.DateUtil;
import com.xkf.cashbook.web.mapper.ConsumeDetailMapper;
import com.xkf.cashbook.web.service.MailService;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日常备份数据任务
 *
 * @author xukf01
 */
@Component
public class DailyBakDataTask {

    private static String insertConsumeDetail = "INSERT INTO `cash_book`.`c_consume_detail` (`id`, `consume_category_id`, `consume_amount`, `consume_way`, `record_date`, `record_by`, `consume_by`, `consume_date`) VALUES ('%d', '%d', '%f', '%d', '%s', '%s', '%s', '%s');\r";

    @Resource
    private ConsumeDetailMapper consumeDetailMapper;

    @Resource
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String to;

    /**
     * 每天备份一份数据
     */
    @Scheduled(cron = "0 33 22 1/1 * ?")
    public void bakData() {
        List<String> sql = getConsumeDetailInsert();
        String filePath = writeToFile(sql);
        sendMail(filePath);
    }

    /**
     * 获取消费详情插入语句
     *
     * @return
     */
    public List<String> getConsumeDetailInsert() {
        List<ConsumeDetailVO> allConsumeDetail = consumeDetailMapper.getLastDetail(100000000);
        List<String> inserts = new ArrayList<>(allConsumeDetail.size());
        for (ConsumeDetailVO consumeDetailVO : allConsumeDetail) {
            String format = String.format(insertConsumeDetail, consumeDetailVO.getId(), consumeDetailVO.getConsumeCategoryId(), consumeDetailVO.getConsumeAmount(), consumeDetailVO.getConsumeWay()
                    , DateUtil.formatDateTime(consumeDetailVO.getRecordDate()), consumeDetailVO.getRecordBy(), consumeDetailVO.getConsumeBy(), consumeDetailVO.getConsumeDate());
            inserts.add(format);
        }
        return inserts;
    }

    private String writeToFile(List<String> sql) {
        File file = null;
        BufferedWriter bw = null;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath() + "cash_book.sql");
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(sql.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    //刷新缓存区
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }


    public void sendMail(String filePath) {
        String nowDate = LocalDate.now().toString();
        mailService.sendAttachmentsMail(to, nowDate + "_数据备份", "数据备份", filePath);
    }


}
