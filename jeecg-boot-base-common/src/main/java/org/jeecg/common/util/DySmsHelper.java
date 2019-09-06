package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
public class DySmsHelper {

	private final static Logger logger=LoggerFactory.getLogger(DySmsHelper.class);

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "NSUp6+W5Sm4i2DFApNkWpvfXV5xE6AOH";
    static final String accessKeySecret = "NSUp6+W5Sm5cXQE224UcMpUV5WVzQLrZ";

    /**
     * 登陆时采用的短信发送模板编码
     */
    public static final String LOGIN_TEMPLATE_CODE="SMS_167040816";

    /**
     * 忘记密码时采用的短信发送模板编码
     */
    public static final String FORGET_PASSWORD_TEMPLATE_CODE="SMS_CODE_004";


    /**
     * 注册时采用的短信发送模板编码
     */
    public static final String REGISTER_TEMPLATE_CODE="SMS_CODE_003";

    /**
     * 必填:短信签名-可在短信控制台中找到
     */
    public static final String signName="经开区科技局";

    public static final String mcApi="http://192.168.66.67:8090/restful_mczzidc_com";//销售线索提交审核模板ID

    /**
     *
     * @param sendType 短信类型：1 验证码  2 短信通知（若为通知，则根据模板要求传参数 condent1、condent1。。。。）
     * @param descr 发送原因 比如注册时使用，就写注册
     * @param mobile 手机号
     * @param code 验证码
     * @param content 发送内容，句首必须带【景安网络】，如【景安网络】您好，您的注册验证码是764317
     * @param templateNum 模板编号  科技局-注册用户SMS_CODE_003 科技局-修改密码SMS_CODE_004  科技局-授权通知SMS_NOTICE_005  科技局-信息通知SMS_NOTICE_006
     * @param paramJson 参数json 如 {"content1":"测试业务2","content2":"2019-07-01 16:29:09","content3":"2019-07-04 16:29:13","content4":"李博文"}
     * @return
     * @throws ClientException
     */
    public static boolean sendSms(Integer sendType ,String descr,String mobile,String code,String content,String templateNum,String paramJson) throws ClientException {
        logger.info("******开始调用mcapi短信接口,接受参数，sendType："+sendType+",mobile:"+mobile+",code:"+code+",templateNum:"+templateNum+",paramJson:"+paramJson);
        try{
            if(GainetUtils.isEmpty(templateNum)){
                logger.info("调用短信接口失败，失败原因：模板标识为空");
                return false;
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("mobile",mobile);
            jsonObject.put("randCode",code);
            jsonObject.put("uuid", UUID.randomUUID());
            jsonObject.put("resource","VG3OQ81wtrNGANeuq8IdwTBhgkAF53lFOIY6aCIBeo0=");
            jsonObject.put("source","k");
            jsonObject.put("descr",descr);
            jsonObject.put("accessKeyId",accessKeyId);
            jsonObject.put("accessSecret",accessKeySecret);
            jsonObject.put("templateNum",templateNum);
            jsonObject.put("sendType",sendType);
            //jsonObject.put("signName",signName);
            jsonObject.put("content",content);
            jsonObject.put("notices",paramJson);
            jsonObject.put("ip","1.1.1.1");
            HttpUtil http = new HttpUtil(HttpUtil.POST_METHOD, mcApi + "/coderestful/verificationcoderestful/sendSMSVerificationCode");
            String resultJSON = http.doJSON(jsonObject);
            if (!GainetUtils.isEmpty(resultJSON)) {
                com.alibaba.fastjson.JSONObject resJson = com.alibaba.fastjson.JSONObject.parseObject(resultJSON);
                if ("0".equals(resJson.get("code").toString())) {
                    logger.info("调用短信接口成功");
                    return true;
                } else {
                    logger.info("调用短信接口失败，失败code："+resJson.get("code").toString()+",失败原因："+resJson.get("message").toString());
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
        /*//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为."{\"code\":\""+code+"\",\"name\":\""+name+"\"}"
        if(paramJson==null){
            request.setTemplateParam(paramJson);
        }else{
            request.setTemplateParam("{\"code\":\""+code+"\",\"name\":\""+name+"\"}");
        }


        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        boolean result = false;

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        logger.info("短信接口返回的数据----------------");
        logger.info("{Code:" + sendSmsResponse.getCode()+",Message:" + sendSmsResponse.getMessage()+",RequestId:"+ sendSmsResponse.getRequestId()+",BizId:"+sendSmsResponse.getBizId()+"}");
        if ("OK".equals(sendSmsResponse.getCode())) {
            result = true;
        }
        return result;
        */

    }


    public static void main(String[] args) throws ClientException, InterruptedException {

        //sendSms("13800138000", "李博文","123456", FORGET_PASSWORD_TEMPLATE_CODE);

    }
}
