package com.xkf.cashbook.web.task;


import cn.hutool.core.date.DateUtil;
import com.xkf.cashbook.web.mapper.ConsumeCategoryMapper;
import com.xkf.cashbook.web.mapper.ConsumeDetailMapper;
import com.xkf.cashbook.web.mapper.IncomeCategoryMapper;
import com.xkf.cashbook.web.mapper.IncomeDetailMapper;
import com.xkf.cashbook.web.service.MailService;
import com.xkf.cashbook.web.vo.ConsumeCategoryVO;
import com.xkf.cashbook.web.vo.ConsumeDetailVO;
import com.xkf.cashbook.web.vo.IncomeCategoryVO;
import com.xkf.cashbook.web.vo.IncomeDetailVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 日常备份数据任务
 *
 * @author xukf01
 */
@Component
public class DailyBakDataTask {

    private static final String INSERT_CONSUME_DETAIL = "INSERT INTO `cash_book`.`c_consume_detail` (`id`, `consume_category_id`, `consume_amount`, `consume_way`, `record_date`, `record_by`, `consume_by`, `consume_date`) VALUES ('%d', '%d', '%f', '%d', '%s', '%s', '%s', '%s');\r";

    private static final String INSERT_INCOME_DETAIL = "INSERT INTO `cash_book`.`c_income_detail` (`id`, `income_by`, `income_amount`, `income_category_id`, `income_date`, `record_date`, `record_by`) VALUES ('%d','%d','%f','%d','%s','%s','%s');\r";

    private static final String INSERT_CONSUME_CATEGORY = "INSERT INTO `cash_book`.`c_consume_category` (`id`, `category_name`) VALUES ('%d', '%s');\r";

    private static final String INSERT_INCOME_CATEGORY = "INSERT INTO `cash_book`.`c_income_category` (`id`, `category_name`) VALUES ('%d', '%s');\r";

    private static final String BAK_SQL_FILE_NAME = "cash_book.sql";

    private static final String BAK_SQL_ZIP_FILE_NAME = "cash_book.zip";

    @Resource
    private ConsumeDetailMapper consumeDetailMapper;

    @Resource
    private IncomeDetailMapper incomeDetailMapper;

    @Resource
    private ConsumeCategoryMapper consumeCategoryMapper;

    @Resource
    private IncomeCategoryMapper incomeCategoryMapper;

    @Resource
    private MailService mailService;

    @Value("${spring.mail.username}")
    private String to;

    /**
     * 每天备份一份数据
     */
    @Scheduled(cron = "0 00 22 1/1 * ?")
    public void bakData() {
        StringBuilder sb = new StringBuilder(1024);
        getConsumeDetailInsert(sb);
        getIncomeDetailInsert(sb);
        getConsumeCategoryInsert(sb);
        getIncomeCategoryInsert(sb);

        writeToFile(sb.toString());
        sendMail(fileToZip());
    }

    /**
     * sql写入到sql文件
     *
     * @param sql sql语句
     * @return
     */
    private void writeToFile(String sql) {
        File file = null;
        BufferedWriter bw = null;
        try {
            file = new File(BAK_SQL_FILE_NAME);
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(sql);
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
    }

    /**
     * 压缩文件
     *
     * @return
     */
    private String fileToZip() {
        InputStream input = null;
        ZipOutputStream zipOut = null;
        try {
            File file = new File(BAK_SQL_FILE_NAME);
            File zipFile = new File(BAK_SQL_ZIP_FILE_NAME);
            input = new FileInputStream(file);
            zipOut = null;
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            zipOut.setComment("www.cashbook.top");
            int temp = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                zipOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return BAK_SQL_ZIP_FILE_NAME;

    }

    /**
     * 发送邮件
     *
     * @param filePath 文件路径
     */
    public void sendMail(String filePath) {
        String nowDate = LocalDate.now().toString();
        mailService.sendAttachmentsMail(to, nowDate + "_bak", "bak", filePath);
    }

    /**
     * 获取消费详情插入语句
     */
    public void getConsumeDetailInsert(StringBuilder sb) {
        List<ConsumeDetailVO> consumeDetails = consumeDetailMapper.getLastDetail(100000000);
        consumeDetails.forEach(consumeDetailVO ->
                sb.append(String.format(INSERT_CONSUME_DETAIL, consumeDetailVO.getId(), consumeDetailVO.getConsumeCategoryId(), consumeDetailVO.getConsumeAmount(), consumeDetailVO.getConsumeWay()
                        , DateUtil.formatDateTime(consumeDetailVO.getRecordDate()), consumeDetailVO.getRecordBy(), consumeDetailVO.getConsumeBy(), consumeDetailVO.getConsumeDate())
                ));
    }

    /**
     * 获取消费分类插入语句
     */
    public void getConsumeCategoryInsert(StringBuilder sb) {
        List<ConsumeCategoryVO> consumeCategories = consumeCategoryMapper.getAll();
        consumeCategories.forEach(consumeCategory ->
                sb.append(String.format(INSERT_CONSUME_CATEGORY, consumeCategory.getId(), consumeCategory.getCategoryName()))
        );
    }

    /**
     * 获取收入分类插入语句
     */
    public void getIncomeCategoryInsert(StringBuilder sb) {
        List<IncomeCategoryVO> incomeCategories = incomeCategoryMapper.getAll();
        incomeCategories.forEach(incomeCategory ->
                sb.append(String.format(INSERT_INCOME_CATEGORY, incomeCategory.getId(), incomeCategory.getCategoryName()))
        );
    }

    /**
     * 获取收入详情插入语句
     */
    public void getIncomeDetailInsert(StringBuilder sb) {
        List<IncomeDetailVO> incomeDetails = incomeDetailMapper.getLastDetail(100000000);
        incomeDetails.forEach(incomeDetailVO ->
                sb.append(String.format(INSERT_INCOME_DETAIL, incomeDetailVO.getId(), incomeDetailVO.getIncomeBy(), incomeDetailVO.getIncomeAmount()
                        , incomeDetailVO.getIncomeCategoryId(), incomeDetailVO.getIncomeDate(), DateUtil.formatDateTime(incomeDetailVO.getRecordDate()), incomeDetailVO.getRecordBy())));
    }


}
