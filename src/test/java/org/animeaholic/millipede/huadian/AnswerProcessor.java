package org.animeaholic.millipede.huadian;

import com.alibaba.fastjson.JSON;
import org.animeaholic.millipede.demo.Bootstrap;
import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.huadian.entity.Answer;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AnswerProcessor extends AbsCrawlResProcessor {

    @Override
    public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws IOException {
        List<Answer> answers = JSON.parseArray(crawlResTask.getContent(), Answer.class);
        Answer answer = answers.get(NumberUtils.BYTE_ZERO);
        System.out.println(JSON.toJSONString(answer));
        Bootstrap.lock.lock();
        FileUtils.writeLines(
                new File("/Users/zhangshengnan/myworkspace/millipede/src/test/resources/answer.txt"),
                CharEncoding.UTF_8,
                Collections.singletonList(JSON.toJSONString(answer)),
                Boolean.TRUE);
        Bootstrap.lock.unlock();
        return null;
    }

}
