
package com.city.city_collector.channel.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.groovy.control.CompilationFailedException;

import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * @author nb
 * @Description:
 */
public class GroovyUtil {

    private static GroovyShell groovyShell;

    static {
        groovyShell = new GroovyShell();
    }

    /**
     * 初始化脚本
     *
     * @param list
     * @author:nb
     */
    public static void initScripts(List<PayClientModel> list) {
        Map<String, GroovyScript> scriptList = new HashMap<String, GroovyScript>();
        int count = 0;
        for (PayClientModel pcm : list) {
            System.out.println("Groovy deal..." + list.get(count).getName());
            GroovyScript gs = new GroovyScript();
            gs.setVersion(pcm.getVersion());

            Map<String, Object> jsons = null;
            try {
                jsons = new Gson().fromJson(pcm.getJson(), new TypeToken<HashMap<String, Object>>() {
                }.getType());
            } catch (Exception e) {
                continue;
            }

            if (pcm.getOrderStatus().intValue() == 1 || pcm.getTestStatus().intValue() == 1) {
                //下单数据处理
                String data = jsons.get("reqpage").toString();
                data += "def dealOrderData(data,clientId,clientNo,clientName,clientChannelId,url,sn,channelParams){\n";
                data += jsons.get("rspprogram").toString();
                data += "\n}\n dealOrderData(data,clientId,clientNo,clientName,clientChannelId,url,sn,channelParams);";
                System.out.println("下单数据处理：\n" + data);
                gs.setDealOrderData(groovyShell.parse(data));
            }

            if (pcm.getOrderStatus().intValue() == 1) {//下单程序启用
                //请求参数处理程序
                String data = jsons.get("reqpage").toString();
                data += "def createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform){\n";
                data += jsons.get("reqprogram").toString();
                data += "\n}\n createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform);";
                System.out.println("请求参数：\n" + data);
                gs.setOrderParams(groovyShell.parse(data));

//                //下单数据处理
//                data=jsons.get("rsppage").toString();
//                data+="def dealOrderData(data,clientId,clientNo,clientName,clientChannelId){\n";
//                data+=jsons.get("rspprogram").toString();
//                data+="\n}\n dealOrderData(data,clientId,clientNo,clientName,clientChannelId);";
//                System.out.println("下单数据处理：\n"+data);
//                gs.setDealOrderData(groovyShell.parse(data));

                //回调数据处理
                data = jsons.get("reqpage").toString();
                data += "def notifyData(params,merchantNo,merchantMy,channelParams,body){\n";
                data += jsons.get("ntprogram").toString();
                data += "\n}\n notifyData(params,merchantNo,merchantMy,channelParams,body);";
                System.out.println("回调数据处理：\n" + data);
                gs.setNotifyData(groovyShell.parse(data));
            }

            if (pcm.getTestStatus().intValue() == 1) {// 启用测试
                //请求参数处理程序
                String data = jsons.get("reqpage").toString();
                data += "def createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform){\n";
                data += jsons.get("treqprogram").toString();
                data += "\n}\n createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform);";
                System.out.println("测试-请求参数：\n" + data);
                gs.setTestOrderParams(groovyShell.parse(data));
            }

            if (pcm.getPayStatus().intValue() == 1) {// 启用代付
                //请求参数处理程序
                String data = jsons.get("reqpage").toString();
                data += "def createReqParams(cash,client,domain,urlcash,keyname){\n";
                data += jsons.get("payreqprogram").toString();
                data += "\n}\n createReqParams(cash,client,domain,urlcash,keyname);";
                System.out.println("代付请求参数：\n" + data);
                gs.setPayOrderParams(groovyShell.parse(data));

                //代付数据处理
                data = jsons.get("reqpage").toString();
                data += "def dealOrderData(data,sn,reqParams){\n";
                data += jsons.get("payrspprogram").toString();
                data += "\n}\n dealOrderData(data,sn,reqParams);";
                System.out.println("代付数据处理：\n" + data);
                gs.setDealPayOrderData(groovyShell.parse(data));

                //回调数据处理
                data = jsons.get("reqpage").toString();
                data += "def notifyData(params,merchantNo,merchantMy,channelParams,body){\n";
                data += jsons.get("ntprogram").toString();
                data += "\n}\n notifyData(params,merchantNo,merchantMy,channelParams,body);";
                System.out.println("回调数据处理：\n" + data);
                gs.setNotifyData(groovyShell.parse(data));
            }

            if ((pcm.getCdStatus() != null) && (pcm.getCdStatus().intValue() == 1)) {//查单
                String data = jsons.get("reqpage").toString();
                data = data + "def createQueryReqParams(order,clientChannel,client,channelParams){\n";
                data = data + jsons.get("queryreqprogram").toString();
                data = data + "\n}\n createQueryReqParams(order,clientChannel,client,channelParams);";
                System.out.println("查单请求参数：\n" + data);
                gs.setQueryOrderData(groovyShell.parse(data));

                data = jsons.get("reqpage").toString();
                data = data + "def dealOrderData(data,order,clientChannel,client,channelParams){\n";
                data = data + jsons.get("queryrspprogram").toString();
                data = data + "\n}\n dealOrderData(data,order,clientChannel,client,channelParams);";
                System.out.println("查单数据处理：\n" + data);
                gs.setDealQueryOrderData(groovyShell.parse(data));
            }

            scriptList.put(pcm.getKeyname(), gs);
            count += 1;
        }
        ChannelManager1.getInstance().initChannelScript(scriptList);
    }

    /**
     * 更新脚本对象，如果脚本不存在，则新增
     *
     * @param pcm
     * @author:nb
     */
    public static void updateGroovyScript(PayClientModel pcm) {
        Map<String, GroovyScript> scriptList = ChannelManager1.getInstance().getScriptList();
        if (scriptList == null) {
            scriptList = new HashMap<String, GroovyScript>();
            ChannelManager1.getInstance().initChannelScript(scriptList);
        }
        GroovyScript gs = scriptList.get(pcm.getKeyname());
        if (gs == null) {//新增
            gs = new GroovyScript();
            scriptList.put(pcm.getKeyname(), gs);
        } else {
            if (gs.getVersion() != null && gs.getVersion().longValue() == pcm.getVersion().longValue()) {
                return;
            }
        }


        Map<String, Object> jsons = null;
        try {
            jsons = new Gson().fromJson(pcm.getJson(), new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            return;
        }

        if (pcm.getOrderStatus().intValue() == 1 || pcm.getTestStatus().intValue() == 1) {
            //下单数据处理
            String data = jsons.get("reqpage").toString();
            data += "def dealOrderData(data,clientId,clientNo,clientName,clientChannelId){\n";
            data += jsons.get("rspprogram").toString();
            data += "\n}\n dealOrderData(data,clientId,clientNo,clientName,clientChannelId);";
            System.out.println("下单数据处理：\n" + data);
            gs.setDealOrderData(groovyShell.parse(data));
        }

        if (pcm.getOrderStatus().intValue() == 1) {//下单程序启用
            //请求参数处理程序
            String data = jsons.get("reqpage").toString();
            data += "def createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform){\n";
            data += jsons.get("reqprogram").toString();
            data += "\n}\n createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform);";
            System.out.println("请求参数：\n" + data);
            gs.setOrderParams(groovyShell.parse(data));

            //回调数据处理
            data = jsons.get("reqpage").toString();
            data += "def notifyData(params,merchantNo,merchantMy,channelParams,body){\n";
            data += jsons.get("ntprogram").toString();
            data += "\n}\n notifyData(params,merchantNo,merchantMy,channelParams,body);";
            System.out.println("回调数据处理：\n" + data);
            gs.setNotifyData(groovyShell.parse(data));
        }

        if (pcm.getTestStatus().intValue() == 1) {// 启用测试
            //请求参数处理程序
            String data = jsons.get("reqpage").toString();
            data += "def createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform){\n";
            data += jsons.get("treqprogram").toString();
            data += "\n}\n createReqParams(id,merchantNo,merchantMy,channelParams,sn,amount,domain,platform);";
            System.out.println("测试-请求参数：\n" + data);
            gs.setTestOrderParams(groovyShell.parse(data));
        }

        if (pcm.getPayStatus().intValue() == 1) {// 启用代付
            //请求参数处理程序
            String data = jsons.get("reqpage").toString();
            data += "def createReqParams(cash,client,domain,urlcash,keyname){\n";
            data += jsons.get("payreqprogram").toString();
            data += "\n}\n createReqParams(cash,client,domain,urlcash,keyname);";
            System.out.println("代付请求参数：\n" + data);
            gs.setPayOrderParams(groovyShell.parse(data));

            //代付数据处理
            data = jsons.get("reqpage").toString();
            data += "def dealOrderData(data,sn,reqParams){\n";
            data += jsons.get("payrspprogram").toString();
            data += "\n}\n dealOrderData(data,sn,reqParams);";
            System.out.println("代付数据处理：\n" + data);
            gs.setDealPayOrderData(groovyShell.parse(data));

            //回调数据处理
            data = jsons.get("reqpage").toString();
            data += "def notifyData(params,merchantNo,merchantMy,channelParams,body){\n";
            data += jsons.get("ntprogram").toString();
            data += "\n}\n notifyData(params,merchantNo,merchantMy,channelParams,body);";
            System.out.println("回调数据处理：\n" + data);
            gs.setNotifyData(groovyShell.parse(data));
        }

        if ((pcm.getCdStatus() != null) && (pcm.getCdStatus().intValue() == 1)) {//查单
            String data = jsons.get("reqpage").toString();
            data = data + "def createQueryReqParams(order,clientChannel,client,channelParams){\n";
            data = data + jsons.get("queryreqprogram").toString();
            data = data + "\n}\n createQueryReqParams(order,clientChannel,client,channelParams);";
            System.out.println("查单请求参数：\n" + data);
            gs.setQueryOrderData(groovyShell.parse(data));

            data = jsons.get("reqpage").toString();
            data = data + "def dealOrderData(data,order,clientChannel,client,channelParams){\n";
            data = data + jsons.get("queryrspprogram").toString();
            data = data + "\n}\n dealOrderData(data,order,clientChannel,client,channelParams);";
            System.out.println("查单数据处理：\n" + data);
            gs.setDealQueryOrderData(groovyShell.parse(data));
        }
        gs.setVersion(pcm.getVersion());
    }

    /**
     * 运行脚本，采用同步的方式，防止并发下Binding数据错乱
     *
     * @param sc
     * @param bind
     * @return
     */
    public static Object runScript(Script sc, Binding bind) {
        synchronized (sc) {
            sc.setBinding(bind);
            return sc.run();
        }
    }

//    private static Script sc;
//
//    public static void main(String[] args) {
////        GroovyShell groovyShell = new GroovyShell();
//        try {
//            sc = groovyShell.parse(new File("D:\\groovy.txt"));
////            for(int i=0;i<10;i++) {
////                testRun(sc);
////            }
//            for(int i=0;i<10;i++) {
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        testRun(sc);;
//                    }
//                }).start();
//            }
//        }
//        catch (CompilationFailedException | IOException e1) {
//
//            e1.printStackTrace();
//        }
//
//    }

//    private static void testRun(Script sc) {
////        synchronized (sc) {
//              long l1=System.currentTimeMillis();
//              String rd=Math.random()+"";
//              System.out.println("测试脚本.."+sc+"|"+rd);
////              try {
////                Thread.sleep(1000L);
////            }
////            catch (InterruptedException e) {
////
////                e.printStackTrace();
////            }
//              Binding bind=new Binding();
//              bind.setProperty("merchantNo", "121232112kk");
//              bind.setProperty("merchantMy", "121232112kkzzz");
//              bind.setProperty("sn", "test123456");
//              bind.setProperty("amount", rd);
//              bind.setProperty("domain", "http://www.baidu.com/");
//              bind.setProperty("id", rd);
//
//              Map < String, Object> cp=new HashMap<String,Object>();
//              cp.put("ip", "127.0.0.1");
//              cp.put("payType", "CP");
//
//              bind.setProperty("channelParams", cp);
//
//              sc.setBinding(bind);
//
//              Object result=sc.run();
//              System.out.println((System.currentTimeMillis()-l1)+"|"+result);
////        }
//
//    }

//    public static void main(String[] args) {
//        int i=9;
//        switch(i) {
//        default:
//            System.out.println("default");
//        case 0:
//            System.out.println("zero");
//            break;
//        case 1:
//            System.out.println("one");
//        case 2:
//            System.out.println("two");
//        }

//        Runtime run=Runtime.getRuntime();
//
//        System.out.println("初始-最大内存："+run.maxMemory());
//        System.out.println("初始-已分配内存："+run.totalMemory());
//        System.out.println("初始-剩余内存："+run.freeMemory());
//        System.out.println("初始-最大可用内存："+(run.maxMemory() -run.totalMemory()+run.freeMemory() ));
//
//        GroovyShell groovyShell = new GroovyShell();
//        Script sc=null;
//        try {
//            sc = groovyShell.parse(new File("D:\\groovy.txt"));
////            Thread.sleep(5000L);
//
//            System.out.println("1-最大内存："+run.maxMemory());
//            System.out.println("1-已分配内存："+run.totalMemory());
//            System.out.println("1-剩余内存："+run.freeMemory());
//            System.out.println("1-最大可用内存："+(run.maxMemory() -run.totalMemory()+run.freeMemory() ));
//
////            Thread.sleep(7000L);
//        }
//        catch (CompilationFailedException | IOException e1) {
//
//            e1.printStackTrace();
//        }
////        catch (InterruptedException e) {
////
////            e.printStackTrace();
////        }
////        for(int i=0;i<3;i++) {
//            long l1=System.currentTimeMillis();
//            Binding bind=new Binding();
//            bind.setProperty("merchantNo", "121232112kk");
//            bind.setProperty("merchantMy", "121232112kkzzz");
//            bind.setProperty("sn", "test123456");
//            bind.setProperty("amount", new BigDecimal(100.01).setScale(2,BigDecimal.ROUND_HALF_UP));
//            bind.setProperty("domain", "http://www.baidu.com/");
//            bind.setProperty("id", 14);
//
//            Map < String, Object> cp=new HashMap<String,Object>();
//            cp.put("ip", "127.0.0.1");
//            cp.put("payType", "CP");
//
//            bind.setProperty("channelParams", cp);
//
//    //        groovyShell.evaluate("");
//            try {
//
//                sc.setBinding(bind);
//
//                Object result=sc.run();
//                System.out.println(result);
//    //            groovyShell.evaluate(new File("D:\\groovy.txt"));
//            }
//            catch (CompilationFailedException e) {
//
//                e.printStackTrace();
//            }
//
//            System.out.println("耗时:"+(System.currentTimeMillis()-l1));
////        }
//        System.out.println("最大内存："+run.maxMemory());
//        System.out.println("已分配内存："+run.totalMemory());
//        System.out.println("剩余内存："+run.freeMemory());
//        System.out.println("最大可用内存："+(run.maxMemory() -run.totalMemory()+run.freeMemory() ));
//        createReqParams(1L, "zzdsssaa1", "qqkjkjdksjda", cp, "thkdskdsj", new BigDecimal(100.01).setScale(2,BigDecimal.ROUND_HALF_UP), "http://www.baidu.com");
//    }

//    初始-最大内存：3795845120
//    初始-已分配内存：257425408
//    初始-剩余内存：250706000
//    初始-最大可用内存：3789125712
//    1-最大内存：3795845120
//    1-已分配内存：257425408
//    1-剩余内存：199739512
//    1-最大可用内存：3738159224

//        最大内存：3795845120
//        已分配内存：257425408
//        剩余内存：187146336
//        最大可用内存：3725566048

//    最大内存：3795845120
//    已分配内存：1841299456
//    剩余内存：1172413544
//    最大可用内存：3126959208

//    private static Map < String, Object > createReqParams(Long id,String merchantNo,String merchantMy,Map < String,Object> channelParams,String sn,BigDecimal amount,String domain){
//        long l1=System.currentTimeMillis();
//        Map < String,Object> params=new TreeMap<String,Object>();
//        params.put("appId", merchantNo);
//        params.put("outTradeNo", sn);
//        params.put("amount", amount);
//        //params.put("nonceStr", System.currentTimeMillis()+"");
//        params.put("asyncUrl", domain+Constants.ASYNC_URL+"/"+id);
//        params.put("returnUrl", domain+Constants.RETURN_URL+"/"+id);
//
//        if(channelParams!=null) params.putAll(channelParams);
//
//        Iterator < Entry < String, Object > > it = params.entrySet().iterator();
//        StringBuffer sbuf=new StringBuffer("");
//        while(it.hasNext()) {
//            Entry < String, Object > et=it.next();
//            //System.out.println(String.valueOf(et.getValue()));
//            if(et.getValue()!=null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("key=");
//        sbuf.append(merchantMy);
//
////        params.put("sign", MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase());
//        params.put("sign", SignUtil.getMD5Sign(params, "12123123121"));
//        System.out.println("time1:"+(System.currentTimeMillis()-l1));
//        return params;
//    }

//    public static void main(String[] args) {
//        System.out.println(MD5Util.MD5Encode("version=1.0&customerid=11510&total_fee=100.00&sdorderno=2020091915121264394d&notifyurl=http://localhost/api/pay/notify/24&returnurl=http://localhost/api/pay/success&42a0ed21e2653e22df72200e3df5cdd5fb460248", "UTF-8"));
//    }
}
