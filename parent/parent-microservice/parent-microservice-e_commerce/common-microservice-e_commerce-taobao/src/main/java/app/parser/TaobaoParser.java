package app.parser;

import com.google.gson.*;
import com.microservice.dao.entity.crawler.e_commerce.taobao.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TaobaoParser {

    public TaobaoUserInfo parserTaobaoUserInfo(WebDriver webDriver) {
        TaobaoUserInfo userInfo = new TaobaoUserInfo();

        WebElement element = webDriver.findElement(By.id("J_uniqueName-mask"));
        if (element != null) {
            element.click();
            userInfo.setNickName(element.getAttribute("value").trim());
        }

        element = webDriver.findElement(By.id("J_realname-mask"));
        if (element != null) {
            element.click();
            userInfo.setRealname(element.getAttribute("value").trim());
        }

        element = webDriver.findElement(By.id("J_gender1"));
        if (element != null) {
            if ("true".equals(element.getAttribute("checked"))) {
                userInfo.setGerden("男");
            }
        } else {
            element = webDriver.findElement(By.id("J_gender2"));
            if (element != null) {
                if ("true".equals(element.getAttribute("checked"))) {
                    userInfo.setGerden("女");
                }
            }
        }

        WebElement mainProfile = webDriver.findElement(By.id("main-profile"));
        userInfo.setBirthday(parserBirthday(mainProfile));

        element = webDriver.findElement(By.id("astro"));
        List<WebElement> optionList = element.findElements(By.tagName("option"));
        if (optionList != null && optionList.size() > 0) {
            userInfo.setConstellation(optionList.get(0).getAttribute("value"));
        }
        return parserHometownAndDomicile(mainProfile, userInfo);
    }

    private TaobaoUserInfo parserHometownAndDomicile(WebElement webElement, TaobaoUserInfo userInfo) {
        String province = parserSelect(webElement, "J_live_province");
        String city = parserSelect(webElement, "J_live_city");
        String area = parserSelect(webElement, "J_live_area");//日
        userInfo.setHometownProvince(province);
        userInfo.setHometownCity(city);
        userInfo.setHometownArea(area);
        province = parserSelect(webElement, "J_redstar_province");
        city = parserSelect(webElement, "J_redstar_city");
        area = parserSelect(webElement, "J_redstar_area");//日
        userInfo.setDomicileProvince(province);
        userInfo.setDomicileCity(city);
        userInfo.setDomicileArea(area);

        return userInfo;
    }


    private String parserBirthday(WebElement webElement) {
        String brithday = null;
        String year = parserSelect(webElement, "J_Year"); //年
        String month = parserSelect(webElement, "J_Month");//月
        String date = parserSelect(webElement, "J_Date");//日
        if (year != null && month != null && date != null) {
            year = year.replaceAll("年", "");
            month = month.replaceAll("月", "");
            date = date.replaceAll("日", "");
            if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(date)) {
                brithday = year + "-" + month + "-" + date;
            }
        }
        return brithday;
    }

    private String parserSelect(WebElement webElement, String id) {
        WebElement element = webElement.findElement(By.id(id));
        if (element != null) {
            List<WebElement> optionList = element.findElements(By.tagName("option"));
            for (WebElement option : optionList) {
                if ("true".equals(option.getAttribute("selected"))) {
                    return option.getText();
                }
            }
        }
        return null;
    }


    public List<TaobaoDeliverAddress> parserTaobaoDeliverAddress(WebDriver webDriver) {

        //地址页面下方 地址列表table
        WebElement table = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/div/div[3]/table"));
        List<TaobaoDeliverAddress> addressList = new ArrayList<TaobaoDeliverAddress>();
        List<WebElement> editList = table.findElements(By.linkText("修改"));
        List<String> addressUrlList = new ArrayList<>();

        for (WebElement edit : editList) {
            String url = edit.getAttribute("href");
            if (url.startsWith("/member")) {
                url = "https://member1.taobao.com" + url;
            }
            addressUrlList.add(url);
        }


        for (int i = 0; i < addressUrlList.size(); i++) {

            String url = addressUrlList.get(i);
//            WebElement tableElement = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/div/div[3]/table"));
//            List<WebElement> list = tableElement.findElements(By.linkText("修改"));
//            WebElement edit = list.get(i);
            // edit.click();

            webDriver.get(url);
            TaobaoDeliverAddress taobaoDeliverAddress = parserTaobaoDeliverAddress(webDriver.getPageSource());
            if (i == 0) {
                taobaoDeliverAddress.setIsDefault(1);
            }
            addressList.add(taobaoDeliverAddress);
        }

//        for (WebElement edit : editList) {
//            edit.click();
//            addressList.add(parserTaobaoDeliverAddress(webDriver.getPageSource()));
//        }
        return addressList;
    }

    public TaobaoDeliverAddress parserTaobaoDeliverAddress(String addressHtml) {

        TaobaoDeliverAddress address = new TaobaoDeliverAddress();
        Pattern pattern = Pattern.compile("<div.*?id=[\"|']city-title[\"|'][^>]+?>([^<]+?)<");
        Matcher matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setArea(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<textarea[\\s\\S]*?id=[\"|']J_Street[\"|'][^>]+?>([^<]+?)</textarea>");
        matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setAddress(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<input.*?id=[\"|']J_PostCode[\"|'].*?value=[\"|'](\\d+)[\"|']\\s*(?:/)*>");
        matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setPostcode(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<input.*?id=[\"|']J_Name[\"|'][\\s\\S]*?value=[\"|'](.+?)[\"|']\\s*(?:/)*>");
        matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setReceiver(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<input.*?id=[\"|']J_Mobile[\"|'][\\s\\S]*?value=[\"|'](.+?)[\"|']\\s*(?:/)*>");
        matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setPhoneNum(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<input.*?value=[\"|'](.+?)[\"|'].*?id=[\"|']J_Phone[\"|'][^>]+?>");
        matcher = pattern.matcher(addressHtml);
        if (matcher.find()) {
            address.setTelNum(matcher.group(1).trim());
        }
        return address;
    }

    public List<TaobaoAlipayPaymentInfo> parserAlipayPaymentInfo(WebDriver webDriver) {
        List<TaobaoAlipayPaymentInfo> list = new ArrayList<TaobaoAlipayPaymentInfo>();
        try {
            WebElement element = null;
            WebElement nextPage = null;
            do {
                webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                element = webDriver.findElement(By.xpath(" //*[@id=\"J_home-record-container\"]/div[2]/div/div[2]/div[2]"));
                nextPage = element.findElement(By.linkText("下一页>"));
                for (int i = 1; i <= 10; i++) {
                    try {
//                        try {
//                            WebElement riskQrcodeCnt = webDriver.findElement(By.xpath("//*[@id=\"risk_qrcode_cnt\"]/canvas"));
//                            if (riskQrcodeCnt != null) {
                        //String base64Code = screenShot4Element(webDriver, riskQrcodeCnt);
                        //二次验证
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        TaobaoAlipayPaymentInfo paymentInfo = new TaobaoAlipayPaymentInfo();
                        WebElement tr = webDriver.findElement(By.xpath("//*[@id=\"J-item-" + i + "\"]"));
                        List<WebElement> tdList = tr.findElements(By.tagName("td"));
                        //交易时间
                        try {
                            WebElement timeTd = tdList.get(1);//交易时间td标签
                            List<WebElement> pList = timeTd.findElements(By.tagName("p"));
                            paymentInfo.setTransactionDate(pList.get(0).getText().trim());
                            paymentInfo.setTransactionTime(pList.get(1).getText().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //交易方
                        try {
                            WebElement timeTd = tdList.get(2);//交易方td标签
                            List<WebElement> pList = timeTd.findElements(By.tagName("p"));
                            paymentInfo.setTransactionName(pList.get(0).getText().trim());//交易名称
                            paymentInfo.setTrader(pList.get(1).getText().replaceAll("\\|", "").trim());//交易方

                            WebElement a = timeTd.findElement(By.tagName("a"));
                            paymentInfo.setTransactionNum(a.getAttribute("title"));//流水号
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //交易金额
                        try {
                            WebElement timeTd = tdList.get(3);//交易金额td标签
                            paymentInfo.setAmount(Double.valueOf(timeTd.getText().replaceAll(" ", "").trim()).doubleValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //交易状态
                        try {
                            WebElement timeTd = tdList.get(5);//交易状态td标签
                            WebElement p = timeTd.findElement(By.tagName("p"));
                            paymentInfo.setStatus(p.getText().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        list.add(paymentInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(3000);
                nextPage.click();
                Thread.sleep(1000);
            } while (nextPage != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public TaobaoAlipayInfo parserAlipayInfo(WebDriver webDriver) {
        try {
            TaobaoAlipayInfo alipayInfo = new TaobaoAlipayInfo();

            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            WebElement alipayUserInfoElement = null;
            //上次登录时间
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[2]/div[2]/div[2]/div[2]"));
                if (element != null) {
                    String lastLoginTime = element.getText();
                    if (lastLoginTime != null) {
                        alipayInfo.setLastLoginTime(lastLoginTime.trim().replaceAll("上次登录时间：", "").trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //用户名
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[2]/div[2]/div[1]/p/a"));
                //alipayUserInfoElement = element;
                if (element != null) {
                    String userName = element.getText();
                    if (userName != null) {
                        alipayInfo.setUserName(userName.trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //登录账号
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"J-userInfo-account-userEmail\"]"));
                if (element != null) {
                    //alipayUserInfoElement = element;
                    String account = element.getText();
                    if (account != null) {
                        alipayInfo.setAccount(account.trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //账户余额
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"showAccountAmountText\"]"));
                if (element != null) {
                    String text = element.getText();
                    if (text != null) {
                        if (text.contains("显示金额")) {
                            element.click();
                            Thread.sleep(100);
                        }
                    }
                }
                element = webDriver.findElement(By.xpath("//*[@id=\"account-amount-container\"]/strong"));
                if (element != null) {
                    String accountBalance = element.getText();
                    if (accountBalance != null) {
                        if (!accountBalance.contains("*.*")) {
//                            alipayInfo.setAccountBalance(Double.valueOf(accountBalance.trim()).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //余额宝
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"showYuebaoAmountText\"]"));
                if (element != null) {
                    String text = element.getText();
                    if (text != null) {
                        if (text.contains("显示金额")) {
                            element.click();
                            Thread.sleep(100);
                        }
                    }
                }

                element = webDriver.findElement(By.xpath("//*[@id=\"J-assets-mfund-amount\"]/strong"));
                if (element != null) {
                    String yebao = element.getText();
                    if (yebao != null) {
                        if (!yebao.contains("*.*")) {
//                            alipayInfo.setYu_e_bao(Double.valueOf(yebao.trim()).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //余额宝累计收益
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"J-income-num\"]"));
                if (element != null) {
                    String earnings = element.getText();
                    if (earnings != null) {
                        if (!earnings.contains("*.*")) {
//                            alipayInfo.setYu_e_baoAccumulatedEarnings(Double.valueOf(earnings.trim()).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //花呗
            try {
                WebElement element = webDriver.findElement(By.xpath("//*[@id=\"showHuabeiAmountText\"]"));
                if (element != null) {
                    String text = element.getText();
                    if (text != null) {
                        if (text.contains("显示金额")) {
                            element.click();
                            Thread.sleep(500);
                        }
                    }
                }
                //可用额度
                WebElement availableCreditElement = webDriver.findElement(By.xpath("//*[@id=\"available-amount-container\"]/strong"));
                if (availableCreditElement != null) {
                    String availableCredit = availableCreditElement.getText();
                    System.out.println(availableCredit);
                    if (availableCredit != null) {
                        if (!availableCredit.contains("*.*")) {
//                            alipayInfo.setHuabeiAvailableCredit(Double.valueOf(availableCredit.trim()).doubleValue());
                        }
                    }
                }
                //总额度
                WebElement totalCreditElement = webDriver.findElement(By.xpath("//*[@id=\"credit-amount-container\"]/strong"));
                if (availableCreditElement != null) {
                    String totalCredit = totalCreditElement.getText();
                    System.out.println(totalCredit);
                    if (totalCredit != null) {
                        if (!totalCredit.contains("*.*")) {
//                            alipayInfo.setHuabeiTotalCredit(Double.valueOf(totalCredit.trim()).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                alipayUserInfoElement = webDriver.findElement(By.xpath("//*[@id=\"globalContainer\"]/div[2]/div/div[2]/ul/li[3]/a"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (alipayUserInfoElement != null) {

                alipayUserInfoElement.click();
                webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                String html = webDriver.getPageSource();

                //webDriver.close();
                return parserAlipayDetailUserDetailUserInfo(html, alipayInfo);
            } else {
                return alipayInfo;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("+++++++++++++++++++++++++" + e.getMessage());
        }
        return null;
    }

    private TaobaoAlipayInfo parserAlipayDetailUserDetailUserInfo(String html, TaobaoAlipayInfo alipayInfo) {

        Pattern pattern = Pattern.compile("真实姓名\\s*</th>\\s*<td[^>]*>([\\s\\S]+?)</td>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String val = matcher.group(1);
            pattern = Pattern.compile("<span\\s*?id=[\"|']username[\"|'][^>]*>([^<]+)</span>");
            matcher = pattern.matcher(val);
            if (matcher.find()) {
                alipayInfo.setRealName(matcher.group(1).trim());
            }

            pattern = Pattern.compile("<span\\s*class=[\"|']text-muted[\"|']>([^>]+)</span>\\s*?<span\\s*?class=[\"|']text-muted[\"|']>已认证");
            matcher = pattern.matcher(val);
            if (matcher.find()) {
                alipayInfo.setIdNum(matcher.group(1));
            }
        }

        pattern = Pattern.compile("邮箱</th>\\s*<td>\\s*<span\\s*class=[\"|']text-muted[\"|']>([^<]+)</span");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            alipayInfo.setEmail(matcher.group(1));
        }


        pattern = Pattern.compile("手机</th>\\s*<td>\\s*<span\\s*class=[\"|']text-muted[\"|']>([^<]+)</span");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            alipayInfo.setPhone(matcher.group(1));
        }

        pattern = Pattern.compile("淘宝会员名</th>\\s*<td>([^<]+)</td");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            alipayInfo.setTaobaoMemberName(matcher.group(1));
        }

        pattern = Pattern.compile("注册时间</th>\\s*<td>([^<]+)</td");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            alipayInfo.setRegisterTime(matcher.group(1));
        }

        pattern = Pattern.compile("会员保障</th>\\s*<td>([^<]+)</td");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            alipayInfo.setMemberGuarantee(matcher.group(1));
        }

        return alipayInfo;
    }

    public List<TaobaoAlipayBankCardInfo> parserAlipayBankCard(WebDriver webDriver) {
        List<TaobaoAlipayBankCardInfo> list = new ArrayList<>();
        try {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            WebElement parentDiv = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div"));

            List<WebElement> bankCardList = parentDiv.findElements(By.linkText("管理"));
            List<String> urlList = new ArrayList<>();
            for (WebElement aElement : bankCardList) {
                urlList.add(aElement.getAttribute("href"));
            }


            for (String url : urlList) {
                try {
                    webDriver.get(url);
                    TaobaoAlipayBankCardInfo bankCardInfo = new TaobaoAlipayBankCardInfo();

                    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

                    WebElement bankImg = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[1]/div[1]/img"));
                    bankCardInfo.setBankName(bankImg.getAttribute("alt"));
                    WebElement element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[1]/div[1]/span[1]"));
                    bankCardInfo.setCardType(element.getText().trim());
                    element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[1]/div[1]/span[2]"));
                    bankCardInfo.setCardNo(element.getText().trim());
                    element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[1]/div[1]/div"));
                    bankCardInfo.setCardholder(element.getText().trim());

                    element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[1]/div[2]/div"));
                    bankCardInfo.setCustomerServiceTel(element.getText().replaceAll("客服电话：", "").trim());

                    element = webDriver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/div[1]/div/div[3]/div/div[3]/span"));
                    bankCardInfo.setReservedPhone(element.getText().replaceAll("银行预留手机：", "").trim());

                    list.add(bankCardInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TaobaoOrderInfo> parserTaobaoOrderInfo(WebDriver webDriver) {
        List<TaobaoOrderInfo> list = new ArrayList<>();
        try {
            WebElement nextPage = null;
            do {
                try {
                    webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    WebElement div = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]"));
                    List<WebElement> orderTableList = div.findElements(By.className("bought-table-mod__table___mnLl_ bought-wrapper-mod__table___3xFFM"));
                    for (WebElement orderTable : orderTableList) {

                        WebElement orderDateSpan = orderTable.findElement(By.className("bought-wrapper-mod__create-time___yNWVS"));
                        String orderDate = orderDateSpan.getText().trim();
                        String orderNo = orderDateSpan.findElement(By.xpath("../../span/span[3]")).getText().trim();
                        String shopsName = orderTable.findElement(By.className("seller-mod__name___1K9qu")).getText().trim();

                        WebElement tbody1 = orderTable.findElements(By.tagName("tbody")).get(1);
                        List<WebElement> goodsTrElementList = tbody1.findElements(By.tagName("tr"));
                        WebElement tr = goodsTrElementList.get(0);

                        List<WebElement> tdList = tr.findElements(By.tagName("td"));
                        WebElement priceTd = tdList.get(4);
                        WebElement statusTd = tdList.get(5);

                        List<WebElement> priceSpanList = priceTd.findElements(By.tagName("span"));
                        String orderTotalOriginalPrice = null;
                        if (priceSpanList.size() == 4) {
                            orderTotalOriginalPrice = priceSpanList.get(1).getText().trim();//订单总原价
                        }

                        String orderTotalTransactionPrice = priceSpanList.get(3).getText().trim();//订单总成交价

                        String orderStatus = statusTd.findElement(By.tagName("span")).getText().trim();

                        for (WebElement goodsTrElement : goodsTrElementList) {
                            TaobaoOrderInfo orderInfo = new TaobaoOrderInfo();
                            orderInfo.setOrderDate(orderDate);
                            orderInfo.setOrderNo(orderNo);
                            orderInfo.setShopsName(shopsName);
                            orderInfo.setOrderTotalOriginalPrice(Double.valueOf(orderTotalOriginalPrice).doubleValue());
                            orderInfo.setOrderTotalTransactionPrice(Double.valueOf(orderTotalTransactionPrice).doubleValue());
                            orderInfo.setOrderTradingStatus(orderStatus);

                            List<WebElement> goodsTdList = goodsTrElement.findElements(By.xpath("td"));
                            WebElement goodsNameTd = goodsTdList.get(0);
                            List<WebElement> pList = goodsNameTd.findElements(By.tagName("p"));
                            orderInfo.setGoodsName(pList.get(0).findElements(By.tagName("span")).get(1).getText().trim());
                            orderInfo.setGoodsName(pList.get(1).getText().trim());


                            WebElement goodsPriceTd = goodsTdList.get(1);
                            List<WebElement> spanList = goodsPriceTd.findElements(By.tagName("span"));
                            if (spanList.size() == 4) {
                                String goodsOriginalPrice = spanList.get(1).getText().trim();
                                orderInfo.setGoodsOriginalPrice(Double.valueOf(goodsOriginalPrice).doubleValue());
                            }

                            String goodsTransactionPrice = spanList.get(1).getText().trim();
                            orderInfo.setGoodsTransactionPrice(Double.valueOf(goodsTransactionPrice).doubleValue());

                            orderInfo.setGoodsCount(Integer.valueOf(goodsTdList.get(2).getText().trim()).intValue());

                            list.add(orderInfo);
                        }
                    }

                    nextPage = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]/div[3]/div[2]/div/button[2]"));
                    nextPage.click();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (nextPage != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TaobaoOrderInfo> parserTaobaoOrderInfo2(WebDriver webDriver) {
        List<TaobaoOrderInfo> list = new ArrayList<>();
        try {
            String html = webDriver.getPageSource();
            Pattern pattern = Pattern.compile("var\\s*data\\s*=\\s*JSON.parse\\('(.+)'\\);");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                String json = matcher.group(1).replaceAll("\\\\\"", "\"").replaceAll("\\\\/", "/").trim();
                List<TaobaoOrderInfo> orderInfoList = parserTaobaoOrderInfo(json);
                list.addAll(orderInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TaobaoOrderInfo> parserTaobaoOrderInfo(String json) {
        List<TaobaoOrderInfo> list = new ArrayList<>();
        //System.out.println(json);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        // System.out.println(jsonObject);
        JsonArray mainOrders = jsonObject.getAsJsonArray("mainOrders");
        for (int i = 0; i < mainOrders.size(); i++) {
            JsonObject orderObject = mainOrders.get(i).getAsJsonObject();
            //System.out.println(orderObject.toString());
            String orderNo = orderObject.get("id").getAsString();
            String orderDate = orderObject.getAsJsonObject("orderInfo").get("createTime").getAsString();
            JsonObject payInfo = orderObject.getAsJsonObject("payInfo");
            String orderTradingStatus = orderObject.getAsJsonObject("statusInfo").get("text").getAsString();
            //System.out.println(payInfo);
            String freight = null;
            try {
                JsonElement freightJson = payInfo.getAsJsonArray("postFees").get(0);
                freight = freightJson.getAsJsonObject().get("value").getAsString();
            } catch (Exception e) {
            }


            Double orderTotalOriginalPrice = null;
            try {
                orderTotalOriginalPrice = Double.valueOf(payInfo.get("oldActualFee").getAsString()).doubleValue();
            } catch (NullPointerException e) {
            }

            double orderTotalTransactionPrice = Double.valueOf(payInfo.get("actualFee").getAsString()).doubleValue();
            String shopsName = orderObject.getAsJsonObject("seller").get("shopName").getAsString();


            JsonArray subOrders = orderObject.getAsJsonArray("subOrders");
            for (JsonElement jsonElement : subOrders) {
                JsonObject goodsObject = jsonElement.getAsJsonObject();
                JsonObject itemInfo = goodsObject.getAsJsonObject("itemInfo");
                String goodsName = itemInfo.get("title").getAsString();

                if (!goodsName.equals("保险服务")) {
                    TaobaoOrderInfo orderInfo = new TaobaoOrderInfo();
                    orderInfo.setGoodsCount(goodsObject.get("quantity").getAsInt());

                    JsonArray skuTextList = itemInfo.getAsJsonArray("skuText");

                    StringBuffer stringBuffer = new StringBuffer();
                    for (JsonElement skuText : skuTextList) {
                        JsonObject text = skuText.getAsJsonObject();
                        stringBuffer.append(text.get("name")).append(":").append(text.get("value")).append(",");
                    }
                    orderInfo.setGoodsDescription(stringBuffer.toString());
                    orderInfo.setGoodsName(goodsName);
                    orderInfo.setShopsName(shopsName);
                    orderInfo.setOrderTotalTransactionPrice(orderTotalTransactionPrice);
                    if (freight != null) {
                        orderInfo.setFreight(Double.valueOf(freight.replaceAll("￥", "")).doubleValue());
                    }

                    if (orderTotalOriginalPrice != null) {
                        orderInfo.setOrderTotalOriginalPrice(orderTotalOriginalPrice);
                    }
                    orderInfo.setOrderTradingStatus(orderTradingStatus);
                    orderInfo.setOrderDate(orderDate);
                    orderInfo.setOrderNo(orderNo);

                    JsonObject priceInfo = goodsObject.getAsJsonObject("priceInfo");
                    try {
                        orderInfo.setGoodsOriginalPrice(priceInfo.get("original").getAsDouble());
                    } catch (NullPointerException e) {
                    }
                    orderInfo.setGoodsTransactionPrice(priceInfo.get("realTotal").getAsDouble());

                    list.add(orderInfo);
                }
            }
        }
        return list;
    }


    public List<TaobaoOrderInfo> parserTaobaoOrderInfo3(WebDriver webDriver) {
        List<TaobaoOrderInfo> list = new ArrayList<>();
        WebElement nextPage = null;
        String nextPageDisabled = null;
        int clickNum = 1;
        try {
            do {
                try {
                    nextPage = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]/div[3]/div[2]/div/button[2]"));
                    nextPageDisabled = nextPage.getAttribute("disabled");
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }

                List<WebElement> orderDetailElementList = webDriver.findElements(By.linkText("订单详情"));

                List<String> orderDetailPageUrlList = new ArrayList<>();
                for (WebElement orderDetailElement : orderDetailElementList) {
                    try {
                        orderDetailPageUrlList.add(orderDetailElement.getAttribute("href"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (String orderDetailPageUrl : orderDetailPageUrlList) {
                    try {
                        webDriver.get(orderDetailPageUrl);
                        String html = webDriver.getPageSource();
                        List<TaobaoOrderInfo> orderInfoList = parserTaobaoOrderInfo4Json(html);
                        list.addAll(orderInfoList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //webDriver.navigate().back();
                }
                webDriver.get("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
                for (int i = 0; i < clickNum; i++) {
                    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    try {
                        nextPage = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]/div[3]/div[2]/div/button[2]"));
                        nextPageDisabled = nextPage.getAttribute("disabled");
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                    }

                    if (nextPage != null && nextPageDisabled == null) {
                        Thread.sleep(2000);
                        nextPage.click();
                        Thread.sleep(3000);
                        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    } else {
                        return list;
                    }

                }
                ++clickNum;

            } while (nextPage != null && nextPageDisabled == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<TaobaoOrderInfo> parserTaobaoOrderInfo4(WebDriver webDriver) {
        List<TaobaoOrderInfo> list = new ArrayList<>();
        List<String> orderDetailPageUrlList = new ArrayList<>();
        WebElement nextPage = null;
        String nextPageDisabled = null;
        try {
            do {
                try {
                    nextPage = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]/div[3]/div[2]/div/button[2]"));
                    nextPageDisabled = nextPage.getAttribute("disabled");
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }

                List<WebElement> orderDetailElementList = webDriver.findElements(By.linkText("订单详情"));

                for (WebElement orderDetailElement : orderDetailElementList) {
                    try {
                        orderDetailPageUrlList.add(orderDetailElement.getAttribute("href"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                try {
                    nextPage = webDriver.findElement(By.xpath("//*[@id=\"tp-bought-root\"]/div[3]/div[2]/div/button[2]"));
                    nextPageDisabled = nextPage.getAttribute("disabled");
                } catch (NoSuchElementException e) {
                    nextPage = null;
                    nextPageDisabled = null;
                }

                if (nextPage != null && nextPageDisabled == null) {
                    Thread.sleep(2000);
                    nextPage.click();
                    Thread.sleep(3000);
                    webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                } else {
                    break;
                }

            } while (nextPage != null && nextPageDisabled == null);

            for (String orderDetailPageUrl : orderDetailPageUrlList) {
                try {
                    webDriver.get(orderDetailPageUrl);
                    String html = webDriver.getPageSource();
                    List<TaobaoOrderInfo> orderInfoList = parserTaobaoOrderInfo4Json(html);
                    list.addAll(orderInfoList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //webDriver.navigate().back();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TaobaoOrderInfo> parserTaobaoOrderInfo4Json(String html) {
        final String TAOBAO = "taobao";
        final String TIANMAO = "tianmao";
        Pattern pattern = Pattern.compile("var\\s*detailData\\s*=(.+)\\s*</script");
        Matcher matcher = pattern.matcher(html);
        String json = null;
        String detailType = null;

        if (matcher.find()) {
            json = matcher.group(1).trim();
            detailType = TIANMAO;
        } else {
            pattern = Pattern.compile("var\\s*data\\s*=\\s*JSON.parse\\(['|\"](.+)['|\"]\\)");
            matcher = pattern.matcher(html);
            if (matcher.find()) {
                json = matcher.group(1).trim();
            }
            detailType = TAOBAO;
        }
        if (json != null) {
            json = json.replaceAll("\\\\\"", "\"").replaceAll("\\\\/", "/").trim();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
            if (detailType.equals(TAOBAO)) {
                return parserTaobaoOrderDetail(jsonObject);
            } else if (detailType.equals(TIANMAO)) {
                return parserTianmaoOrderDetail(jsonObject);
            }
        } else {
            return parserTaobaoOrderDetail(html);
        }
        return null;
    }

    private List<TaobaoOrderInfo> parserTianmaoOrderDetail(JsonObject jsonObject) {

        List<TaobaoOrderInfo> orderInfoList = new ArrayList<>();
        TaobaoOrderInfo tempBean = new TaobaoOrderInfo();

        JsonObject amount = jsonObject.getAsJsonObject("amount");

        JsonArray count = amount.getAsJsonArray("count");
        for (JsonElement jsonElement : count) {
            JsonArray asJsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : asJsonArray) {
                JsonObject asJsonObject = element.getAsJsonObject();
                JsonArray content = asJsonObject.getAsJsonArray("content");
                for (JsonElement jsonElement1 : content) {
                    String str = jsonElement1.toString();
                    if (str.contains("订单总价")) {
                        Pattern pattern1 = Pattern.compile("[\"|']text[\"|']:[\"|']￥(.+?)[\"|']");
                        Matcher matcher1 = pattern1.matcher(str);
                        if (matcher1.find()) {
                            String totoTransactionPrice = matcher1.group(1);
                            tempBean.setOrderTotalTransactionPrice(Double.valueOf(totoTransactionPrice).doubleValue());
                        }
                    }
                }
            }
        }
        JsonObject basic = jsonObject.getAsJsonObject("basic");
        JsonArray lists = basic.getAsJsonArray("lists");
        for (JsonElement jsonElement : lists) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String key = asJsonObject.get("key").getAsString().trim();
            //System.out.println(asJsonObject);
            if (key.equals("收货地址")) {
                JsonElement addressElement = asJsonObject.getAsJsonArray("content").get(0);
                String addressStr = addressElement.getAsJsonObject().get("text").getAsString();
                //System.out.println(addressStr);
                String[] addressArr = addressStr.split(",");
                if (addressArr.length == 5) {
                    tempBean.setReceiver(addressArr[0]);
                    tempBean.setPhoneNum(addressArr[1]);
                    tempBean.setTelNum(addressArr[2]);
                    tempBean.setReceivAddress(addressArr[3]);
                    tempBean.setPostNum(addressArr[4]);
                } else if (addressArr.length == 4) {
                    tempBean.setReceiver(addressArr[0]);
                    tempBean.setPhoneNum(addressArr[1]);
                    tempBean.setReceivAddress(addressArr[2]);
                    tempBean.setPostNum(addressArr[3]);
                }
            } else if (key.equals("订单编号")) {

                JsonArray content = asJsonObject.getAsJsonArray("content");
                for (int i = 0; i < content.size(); i++) {
                    JsonObject jsonObject1 = content.get(i).getAsJsonObject();
                    if (i == 0) {
                        tempBean.setOrderNo(jsonObject1.get("text").getAsString());
                    } else {
                        JsonArray moreList = jsonObject1.getAsJsonArray("moreList");
                        for (JsonElement jsonElement1 : moreList) {
                            JsonObject asJsonObject1 = jsonElement1.getAsJsonObject();
                            String key2 = asJsonObject1.get("key").getAsString().trim();

                            if ("成交时间".equals(key2)) {
                                tempBean.setOrderDate(asJsonObject1.getAsJsonArray("content").get(0).getAsJsonObject().get("text").getAsString().trim());
                            } else if ("支付宝交易号".equals(key2)) {
                                tempBean.setPaymentMethod("支付宝");
                            }
                        }
                    }
                }
            }
        }

        JsonObject orders = jsonObject.getAsJsonObject("orders");
        JsonObject list = orders.getAsJsonArray("list").get(0).getAsJsonObject();

        //System.out.println(jsonElement);
        JsonObject status = list.getAsJsonArray("status").get(0).getAsJsonObject();

        tempBean.setOrderTradingStatus(status.getAsJsonArray("statusInfo").get(0).getAsJsonObject().get("text").getAsString());

        JsonArray subOrders = status.getAsJsonArray("subOrders");
        for (JsonElement jsonElement1 : subOrders) {
            TaobaoOrderInfo taobaoOrderInfo = new TaobaoOrderInfo();
            taobaoOrderInfo.setGoodsName(jsonElement1.getAsJsonObject().getAsJsonObject("itemInfo").get("title").getAsString());
            try {
                taobaoOrderInfo.copy(tempBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderInfoList.add(taobaoOrderInfo);
        }

        return orderInfoList;
    }

    private List<TaobaoOrderInfo> parserTaobaoOrderDetail(JsonObject jsonObject) {
        List<TaobaoOrderInfo> orderInfoList = new ArrayList<>();
        TaobaoOrderInfo tempBean = new TaobaoOrderInfo();
        JsonObject deliveryInfo = jsonObject.getAsJsonObject("deliveryInfo");
        String addressStr = deliveryInfo.get("address").getAsString();
        //System.out.println(addressStr);周，86-18500225169，北京 北京市 丰台区 新村街道 三环新城7号院5号楼4单元 804 ，000000
        String[] addressArr = addressStr.split("，");
        if (addressArr.length == 5) {
            tempBean.setReceiver(addressArr[0]);
            tempBean.setPhoneNum(addressArr[1]);
            tempBean.setTelNum(addressArr[2]);
            tempBean.setReceivAddress(addressArr[3]);
            tempBean.setPostNum(addressArr[4]);
        } else if (addressArr.length == 4) {
            tempBean.setReceiver(addressArr[0]);
            tempBean.setPhoneNum(addressArr[1]);
            tempBean.setReceivAddress(addressArr[2]);
            tempBean.setPostNum(addressArr[3]);
        }

        JsonObject mainOrder = jsonObject.getAsJsonObject("mainOrder");
        tempBean.setOrderNo(mainOrder.get("id").getAsString());

        tempBean.setOrderTotalTransactionPrice(Double.valueOf(mainOrder.getAsJsonArray("totalPrice").get(0).getAsJsonObject().getAsJsonArray("content").get(0).getAsJsonObject().get("value").getAsString()).doubleValue());

        JsonObject orderInfo = mainOrder.getAsJsonObject("orderInfo");
        JsonArray lines = orderInfo.getAsJsonArray("lines");
        for (JsonElement jsonElement1 : lines) {
            JsonObject line = jsonElement1.getAsJsonObject();
            JsonArray contentArr = line.getAsJsonArray("content");
            for (JsonElement content : contentArr) {
                JsonObject value = content.getAsJsonObject().getAsJsonObject("value");
                String name = value.get("name").getAsString();
                if (name.contains("支付宝交易号")) {
                    tempBean.setPaymentMethod("支付宝");
                } else if (name.contains("创建时间")) {
                    tempBean.setOrderDate(value.get("value").getAsString());
                }
            }
        }
        tempBean.setOrderTradingStatus(mainOrder.getAsJsonObject("statusInfo").get("text").getAsString());

        JsonArray subOrderArr = mainOrder.getAsJsonArray("subOrders");
        for (JsonElement subOrder : subOrderArr) {
            JsonObject itemInfo = subOrder.getAsJsonObject().getAsJsonObject("itemInfo");
            TaobaoOrderInfo info = new TaobaoOrderInfo();
            info.setGoodsName(itemInfo.get("title").getAsString());
            try {
                info.copy(tempBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderInfoList.add(info);
        }

        return orderInfoList;
    }

    private List<TaobaoOrderInfo> parserTaobaoOrderDetail(String html) {
        List<TaobaoOrderInfo> orderInfoList = new ArrayList<>();
        TaobaoOrderInfo tempBean = new TaobaoOrderInfo();
        Pattern pattern = Pattern.compile("订单编号[^>]+>[^>]+>([^<]+)<");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            tempBean.setOrderNo(matcher.group(1));
        }

        pattern = Pattern.compile("收货地址[^>]+>[^>]+>([^<]+)<");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            String[] addressArr = matcher.group(1).split("，");
            if (addressArr.length == 5) {
                tempBean.setReceiver(addressArr[0].trim());
                tempBean.setPhoneNum(addressArr[1].trim());
                tempBean.setTelNum(addressArr[2].trim());
                tempBean.setReceivAddress(addressArr[3].trim());
                tempBean.setPostNum(addressArr[4].trim());
            } else if (addressArr.length == 4) {
                tempBean.setReceiver(addressArr[0].trim());
                tempBean.setPhoneNum(addressArr[1].trim());
                tempBean.setReceivAddress(addressArr[2].trim());
                tempBean.setPostNum(addressArr[3].trim());
            }
        }

        pattern = Pattern.compile("实付款[^>]+>([^<]+)<");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            tempBean.setOrderTotalTransactionPrice(Double.valueOf(matcher.group(1).trim()));
        }

        pattern = Pattern.compile("(支付宝交易号)");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            tempBean.setPaymentMethod("支付宝");
        }

        pattern = Pattern.compile("成交时间[^>]+>[^>]+>([^<]+)<");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            tempBean.setOrderDate(matcher.group(1).trim());
        }

        pattern = Pattern.compile("当前订单状态[^>]+>([^<]+)<");
        matcher = pattern.matcher(html);
        if (matcher.find()) {
            tempBean.setOrderTradingStatus(matcher.group(1).trim());
        }

        pattern = Pattern.compile("<td\\s*class=[\"|']item[\"|']>\\s*<div\\s*class=[\"|']pic-info[\"|']>\\s*<div\\s*class=\"pic s50\">\\s*<a[^>]*?>\\s*<img\\s*alt=[\"|'](.+?)[\"|'][^>]*?>");
        matcher = pattern.matcher(html);
        while (matcher.find()) {
            TaobaoOrderInfo taobaoOrderInfo = new TaobaoOrderInfo();
            taobaoOrderInfo.setGoodsName(matcher.group(1).trim());
            try {
                taobaoOrderInfo.copy(tempBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            orderInfoList.add(taobaoOrderInfo);
        }

        return orderInfoList;
    }


    public String screenShot4Element(WebDriver driver, WebElement element) {
        //截取整个页面的图片
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            //获取元素在所处frame中位置对象
            Point p = element.getLocation();
            //获取元素的宽与高
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            //矩形图像对象
            Rectangle rect = new Rectangle(p.x, p.y, width, height);
            BufferedImage img = ImageIO.read(scrFile);
            BufferedImage bufferedImage = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);

            ByteArrayOutputStream outputStream = null;
            try {
                outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", outputStream);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}