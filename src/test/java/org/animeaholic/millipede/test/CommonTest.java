package org.animeaholic.millipede.test;

import com.alibaba.fastjson.JSON;
import org.animeaholic.millipede.huadian.entity.Answer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonTest {

    public static void main(String[] args) throws IOException {
        //创建Excel对象
        //创建工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();//这里也可以设置sheet的Name
        //创建工作表对象
        HSSFSheet sheet = workbook.createSheet();

        int rowNum = 0;
        for (String readLine : FileUtils.readLines(new File("/Users/zhangshengnan/myworkspace/millipede/src/test/resources/answer.txt"), CharEncoding.UTF_8)) {
            Answer answer = JSON.parseObject(readLine, Answer.class);
            if (answer.getQuestion_Type().equalsIgnoreCase("填空题")) {
				continue;
            }

			//创建工作表的行["填空题","多选题","单选题","判断题"]
			//["","一般"]
			HSSFRow row = sheet.createRow(rowNum++);//设置第一行，从零开始
			row.createCell(0).setCellValue(answer.getQuestion_Type().replace("题", ""));
			row.createCell(1).setCellValue(answer.getQuestion());
			row.createCell(2).setCellValue(StringUtils.defaultIfBlank(answer.getA(), ""));
			row.createCell(3).setCellValue(StringUtils.defaultIfBlank(answer.getB(), ""));
			row.createCell(4).setCellValue(StringUtils.defaultIfBlank(answer.getC(), ""));
			row.createCell(5).setCellValue(StringUtils.defaultIfBlank(answer.getD(), ""));
			row.createCell(6).setCellValue(StringUtils.defaultIfBlank(answer.getE(), ""));
			row.createCell(7).setCellValue(StringUtils.defaultIfBlank(answer.getF(), ""));
			row.createCell(8).setCellValue(answer.getAnswer());
			row.createCell(9).setCellValue("中");
        }
        //文档输出
        FileOutputStream out = new FileOutputStream("/Users/zhangshengnan/myworkspace/millipede/src/test/resources/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + ".xls");
        workbook.write(out);
        out.close();
    }

}
