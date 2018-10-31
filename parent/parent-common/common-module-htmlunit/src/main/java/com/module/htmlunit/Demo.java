package com.module.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;

import java.util.Random;

/**
 * Created by kaixu on 2017/9/18.
 */
public class Demo extends Thread {
    @Override
    public void run() {
        try {
            int t = new Random().nextInt(8000);
            Thread.sleep(t);
            WebCrawler webCrawler = WebCrawler.getInstance();
            WebClient webClient = webCrawler.getNewWebClient();
            System.out.println("webCrawler:" + webCrawler.hashCode() + "--------------" + webCrawler + "--------------" + t);
            System.out.println("webclient:" + webClient.hashCode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Demo[] mts = new Demo[10];
        for (int i = 0; i < mts.length; i++) {
            mts[i] = new Demo();
        }

        for (int j = 0; j < mts.length; j++) {
            mts[j].start();
        }
//        for (int j = 0; j < mts.length; j++) {
//            WebCrawler webCrawler = WebCrawler.getInstance();
//            System.out.println("webcralwer"+webCrawler);
//        }
    }
}
