package org.animeaholic.millipede.huadian;

import org.animeaholic.millipede.entity.CrawlPlan;
import org.animeaholic.millipede.entity.CrawlTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsPlanProcessor;

import java.util.ArrayList;
import java.util.List;

public class PlainProcessor extends AbsPlanProcessor {

    @Override
    public List<AbsBaseTask<?>> process(CrawlPlan plan) {
        System.out.println("------ start plan ------");
        List<AbsBaseTask<?>> nextTaskList = new ArrayList<AbsBaseTask<?>>();
        String seedUrl = plan.getOwnConfig("seedUrl");
        CrawlTask task = new CrawlTask(plan.getPlanName());
        task.setCallBack(LoginProcessor.class);
        task.setUrl(seedUrl);
        task.setHttpHeader("Host", "hd.cdqindao.com:8089");
        task.setHttpHeader("Upgrade-Insecure-Requests", "1");
        task.setHttpHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        task.setHttpHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        task.setHttpHeader("Referer", "http://hd.cdqindao.com:8089/");
        task.setHttpHeader("Accept-Language", "zh-CN,zh;q=0.9");
        nextTaskList.add(task);
        return nextTaskList;
    }

}
