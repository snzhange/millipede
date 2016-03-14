package org.animeaholic.millipede.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.animeaholic.millipede.entity.CrawlResTask;
import org.animeaholic.millipede.entity.base.AbsBaseTask;
import org.animeaholic.millipede.processor.AbsCrawlResProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VOA_DetailProcessor extends AbsCrawlResProcessor {

	private static String rootPath = "/opt";
	
	@Override
	public List<AbsBaseTask<?>> process(CrawlResTask crawlResTask) throws Exception {
		String html = crawlResTask.getContent();
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("#mp3");
		for (Element ele : eles) {
			String title = doc.select("#title > h1").get(0).text();
			String detailUrl = ele.attr("href");
			System.out.println("开始下载" + detailUrl);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(detailUrl);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
			FileOutputStream out = FileUtils.openOutputStream(new File(rootPath + "/" + title + ".mp3"));
			try {
				IOUtils.copy(in, out);
			} finally {
				in.close();
				out.close();
				httpclient.close();
			}
		}
		return null;
	}

}
