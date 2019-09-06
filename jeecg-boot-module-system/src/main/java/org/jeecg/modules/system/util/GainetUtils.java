package org.jeecg.modules.system.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Great artists
 * @date 2018/10/10/010.
 * 作用：工具类
 */
@Slf4j
public class GainetUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static int changeLinuxPwdPort = 21567;

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     * @author LiBaozhen
     * @date 2014-2-16 上午11:08:15
     */
    public static String getClientIP4(HttpServletRequest request) {
        if (request != null) {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        } else {
            return "";
        }
    }

    /**
     * [当前日期加上指定月数后的日期] <br>
     *
     * @param month
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午10:07:32 <br>
     */
    public static Timestamp getTimes(int month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = format.parse(format.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp ts = new Timestamp(dt.getTime());
        return ts;
    }

    /**
     * [当前日期加上指定天数后的日期] <br>
     *
     * @param days
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午10:08:41 <br>
     */
    public static Timestamp getDateTime(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date dt = null;
        try {
            dt = format.parse(format.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp ts = new Timestamp(dt.getTime());
        return ts;

    }

    /**
     * 时间推迟
     *
     * @param date
     * @param sec  秒
     * @return @Date
     * @author icesummer
     * @date 2015-07-13
     */
    public static Date setTimePutOff(Date date, Long sec) {
        Long putofflong = (date.getTime() + sec * 1000);
        Date enddate = new Date(putofflong);
        return enddate;
    }

    /**
     * 时间推迟
     *
     * @param date
     * @param seconds
     * @return @String
     * @author icesummer
     * @date 2015-07-13
     */
    public static String setTimePutOffStr(Date date, Long seconds) {
        Date d = setTimePutOff(date, seconds);
        return getYyyyMMddHHmmssSDF().format(d);
    }

    /**
     * @description: 获取日期格式转换工具类(yyyyMMddHHmmss)
     */
    public static SimpleDateFormat getYyyyMMddHHmmssSDF() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }


    public static StringBuilder StringBuilderReplace(StringBuilder contentBuilder,String str1,String str2){
        int index = contentBuilder.indexOf(str1);
        if (index > -1) {
            int lastIndex = 0;
            while (index > -1) {
                contentBuilder.replace(index, index + str1.length(), str2);
                lastIndex = index + str2.length();
                index = contentBuilder.indexOf(str1.toString(), lastIndex);
            }
        }
        return contentBuilder;
    }

    /**
     * 过滤SQL中的特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 清除掉这些特殊字符(')
        String regEx = "[']";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * [获取唯一的UUID] <br>
     *
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:27:11 <br>
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
    }

    /**
     * 计算两个时间之间相隔天数
     *
     * @param startday 开始时间
     * @param endday   结束时间
     * @return
     */
    public static int getIntervalDays(Calendar startday, Calendar endday) {
        // 确保startday在endday之前
        if (startday.after(endday)) {
            Calendar cal = startday;
            startday = endday;
            endday = cal;
        }
        // 分别得到两个时间的毫秒数
        long sl = startday.getTimeInMillis();
        long el = endday.getTimeInMillis();

        long ei = el - sl;
        // 根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /**
     * 计算两个时间之间相隔月份
     *
     * @param startday 开始时间
     * @param endday   结束时间
     * @return
     */
    public static double getIntervalMonths(Timestamp startday, Timestamp endday) {
        // 分别得到两个时间的毫秒数
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        // 根据毫秒数计算间隔月数
        return ei / 1000 / 60 / 60 / 24 / 30.4375;
    }

    /**
     * [计算两个日期之间相差的月数] <br>
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午10:10:07 <br>
     */
    public static int getMonths(Date date1, Date date2) {
        int iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(date1);

            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(date2);

            if (objCalendarDate2.equals(objCalendarDate1))
                return 0;
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
                    .get(Calendar.DAY_OF_MONTH))
                flag = 1;

            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
                    .get(Calendar.YEAR))
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
                        .get(Calendar.YEAR))
                        * 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
                        - objCalendarDate1.get(Calendar.MONTH);
            else
                iMonth = objCalendarDate2.get(Calendar.MONTH)
                        - objCalendarDate1.get(Calendar.MONTH) - flag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return iMonth;
    }

    /**
     * 计算两个时间之间相隔天数
     *
     * @param startday 开始时间
     * @param endday   结束时间
     * @return
     */
    public static int getIntervalDays(Timestamp startday, Timestamp endday) {
        // 分别得到两个时间的毫秒数
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        // 根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }


    /**
     * 给出两个日期，计算他们之间相差的年数|月数|天数
     *
     * @param c1   日期1
     * @param c2   日期2
     * @param what 比较模式，如果是Calendar.YEAR则在年份上比较； 如果是Calendar.MONTH则在月数上比较；
     *             如果是Calendar.DATE则在天数上比较（默认情形）
     * @return 相差的年数或月数或天数
     */
    public static int compare(Calendar c1, Calendar c2, int what) {
        int number = 0;
        switch (what) {
            case Calendar.YEAR:
                number = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
                break;
            case Calendar.MONTH:
                int years = compare(c1, c2, Calendar.YEAR);
                number = 12 * years
                        + (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
                break;
            case Calendar.DATE:
                number = (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                break;
            default:
                number = (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                break;
        }
        return number;
    }

    /**
     * 根据下标获取指定字符串
     *
     * @param splitStr   要截取的字符串
     * @param splitChare 分隔符(不可为.)
     * @param index      第几个,从1开始
     * @return String
     * @author l0uj1e, 2013-01-29 17:30
     */
    public static String splitStrByChare(String splitStr, String splitChare,
                                         int index) {
        // 1.分割字符串根据指定分隔符
        String[] strs = splitStr.split(splitChare);
        // 2.获取指定位置上的字符串
        if (index > 0 && index <= strs.length) {
            return strs[index - 1];
        } else {
            return null;
        }
    }

    /**
     * 获取调用该方法的类名及方法名称
     * CN:ClassName,MN:MethodName,LN:LineNumber,SE:StrackTraceException
     *
     * @return 类及方法名字符串String
     * @author l0uj1e, 2013-01-28 15:20
     */
    public static String nameClassAndMethod() {
        try {
            // 1.存放返回信息
            StringBuffer stringBuffer = new StringBuffer();
            // 2.获取堆栈信息
            StackTraceElement[] itemStackTraces = new Throwable()
                    .getStackTrace();
            // 3.遍历堆栈信息
            if (itemStackTraces == null || itemStackTraces.length <= 1) {
                stringBuffer.append("UNNONE");
            } else {
                // 4.获取调用方的信息
                stringBuffer.append("CN:[" + itemStackTraces[1].getFileName()
                        + "];MN:[" + itemStackTraces[1].getMethodName()
                        + "];LN:[" + itemStackTraces[1].getLineNumber() + "];");
            }
            // 5.返回信息
            return stringBuffer.toString();
        } catch (Exception e) {
            // 5.异常,返回信息
            log.warn("获取类,方法名称出现异常:[" + e.getMessage() + "]");
            return "STE:[" + e.getMessage() + "]";
        }
    }

    /**
     * 获取调用该方法的类名及方法名称,不小于index下标的记录
     * CN:ClassName,MN:MethodName,LN:LineNumber,SE:StrackTraceException
     *
     * @param index 堆栈数组的下标,0:是nameClassAndMethod,1:0的直接调用方,2:0的间接调用方,.....[不合法时为1
     *              ]
     * @return 类及方法名字符串String
     * @author l0uj1e, 2013-01-29 14:23
     */
    public static String nameClassAndMethod(int index) {
        try {
            // 处理参数
            if (index < 0) {
                index = 1;
            }
            // 1.存放返回信息
            StringBuffer stringBuffer = new StringBuffer();
            // 2.获取堆栈信息
            StackTraceElement[] itemStackTraces = new Throwable()
                    .getStackTrace();
            // 3.遍历堆栈信息
            if (itemStackTraces == null || itemStackTraces.length < index) {
                stringBuffer.append("UNNONE");
            } else {
                // 4.获取调用方的信息
                for (int i = index; i < itemStackTraces.length; i++) {
                    stringBuffer.append("index[" + i + "];CN:["
                            + itemStackTraces[i].getFileName() + "];MN:["
                            + itemStackTraces[i].getMethodName() + "];LN:["
                            + itemStackTraces[i].getLineNumber() + "];");
                }
            }
            // 5.返回信息
            return stringBuffer.toString();
        } catch (Exception e) {
            // 5.异常,返回信息
            log.warn("获取类,方法名称出现异常:[" + e.getMessage() + "]");
            return "STE:[" + e.getMessage() + "]";
        }
    }

    /**
     * 字符串过滤：防SQL注入及XSS
     * 过滤内容：<>'"();%&~^及两端的空字符
     *
     * @param str 要过滤的字符串
     * @return String
     */
    public static String filterStr(String str) {
        if (str == null || "".equals(str = str.trim()))
            return "";

        try {
            return str.replaceAll("<|>|'|\"|;|/|%|~|\\^", "");
        } catch (Exception e) {
            log.warn(nameClassAndMethod(1) + "过滤字符串[" + str + "]出现异常："
                    + e.toString());
            return "";
        }
    }

    /**
     * 方法描述：分页查询条件防注入过滤
     *
     * @param map 条件
     * @return Map<String                                                               ,                                                               String>
     */
    public final static Map<String, String> filterSQLMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        Set<Entry<String, String>> set = map.entrySet();
        for (Iterator<Entry<String, String>> it = set.iterator(); it
                .hasNext(); ) {
            Entry<String, String> entry = it
                    .next();
            resultMap.put(entry.getKey(), filterStr(entry.getValue()));
        }
        return resultMap;
    }

    /**
     * 将一个对象转换成整型
     *
     * @param obj 一个数字或其他对象
     * @return 数字或initValue
     * @author l0uj1e, 2013-01-28 16:49
     */
    public static int intOf(Object obj, int initValue) {
        // 1.判断参数是否合法
        if (obj == null || "".equals(obj)) {
            return initValue;
        } else {
            try {
                return Integer.parseInt(obj.toString());
            } catch (Exception e) {
                log.warn("将对象转换成整型出现异常:[obj:" + obj + "],E:["
                        + e.getMessage() + "]");
                return initValue;
            }
        }
    }

    /**
     * 将一个对象转换成长整型
     *
     * @param obj 一个长数字或其他对象
     * @return 数字或initValue
     * @author l0uj1e, 2013-01-28 16:53
     */
    public static long longOf(Object obj, long initValue) {
        // 1.判断参数是否合法
        if (obj == null || "".equals(obj)) {
            return initValue;
        } else {
            try {
                return Long.parseLong(obj.toString());
            } catch (Exception e) {
                log.warn("将对象转换成长整型出现异常:[obj:" + obj + "],E:["
                        + e.getMessage() + "]");
                return initValue;
            }
        }
    }

    /**
     * 将一个对象转换成长整型
     *
     * @param obj 一个长数字或其他对象
     * @return 数字或initValue
     * @author l0uj1e, 2013-01-28 16:53
     */
    public static float floatOf(Object obj, float initValue) {
        // 1.判断参数是否合法
        if (obj == null || "".equals(obj)) {
            return initValue;
        } else {
            try {
                return Float.parseFloat(obj.toString());
            } catch (Exception e) {
                log.warn("将对象转换成float类型出现异常:[obj:" + obj + "],E:["
                        + e.getMessage() + "]");
                return initValue;
            }
        }
    }

    /**
     * 将一个对象转换成长整型
     *
     * @param obj 一个长数字或其他对象
     * @return doubleValue
     * @author l0uj1e, 2013-01-28 16:53
     */
    public static double doubleOf(Object obj, double doubleValue) {
        // 1.判断参数是否合法
        if (obj == null || "".equals(obj)) {
            return doubleValue;
        } else {
            try {
                return Double.parseDouble(obj.toString());
            } catch (Exception e) {
                log.warn("将对象转换成double类型出现异常:[obj:" + obj + "],E:["
                        + e.getMessage() + "]");
                return doubleValue;
            }
        }
    }

    /**
     * 将一个对象转换成字符串
     *
     * @param obj 一个字符串或其他对象
     * @return 字符串
     * @author l0uj1e, 2013-04-01 15:45
     */
    public static String stringOf(Object obj) {
        if (obj == null || "".equals(obj)) {
            return "";
        } else {
            try {
                return String.valueOf(obj);
            } catch (Exception e) {
                return "";
            }
        }
    }

    /**
     * 将一个对象转换成Date数据库格式
     *
     * @param obj 一个符合转换的字符串
     * @return 字符串yyyy-MM-dd HH:mm:ss
     * @author l0uj1e, 2014-04-01 16:00
     */
    public static String dateOf(Object obj) {
        if (obj == null || "".equals(obj)) {
            return new SimpleDateFormat().format("00-00-00 00:00:00");
        } else {
            try {
                return new SimpleDateFormat().format(obj);
            } catch (Exception e) {
                return new SimpleDateFormat().format("00-00-00 00:00:00");
            }
        }
    }


    /**
     * 判断指定方法是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        try {
            if (str == null || "".equals(str)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 创建一个不大于8位的随机字符串
     *
     * @return
     */
    public static String createRandomPassword() {
        Random random = new Random();
        int passwordLength = 0;
        String password = "qazxswedcvfrtgbnhyujmASDFGHJQWE13570!@#$RTYUIOBVCXkiolp";
        String pw = "";
        do {
            passwordLength = random.nextInt(12);
        } while (passwordLength <= 8);
        for (int i = 0; i < passwordLength; i++) {
            pw += password.charAt(random.nextInt(password.length()));
        }
        return pw;
    }

    // public static final String allChar = "abOPQRSTUcpyz01EF@#$GMNV234qrsdnoW89!^&XefghLtijklHIJKuvwx567*_~ABCDmYZ";
    public static final String allChar = "abOPQRSTUcpyz01EFGMNV234qrsdnoW89XefghLtijklHIJKuvwx567ABCDmYZ";

    /**
     * [生成指定长度的一个随机字符串] <br>
     *
     * @param length 参数为返回随机数的长度
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午10:13:57 <br>
     */
    public static String generateStr(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /***
     * 对比日期是否过期
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 0 未过期 1已过期
     * @throws ParseException
     */
    public static int isTime(String beginTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bt = sdf.parse(beginTime);
        Date et = sdf.parse(endTime);
        if (bt.before(et)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static void main(String[] args) throws ParseException {
        String sjcy = "1540350404000";
        String start = stampToDate(sjcy);
        System.out.println(new Date().getTime());

    }

    /**
     * [随机生成一个字母数字组合长度为length字符串] <br>
     *
     * @param length 生成字符串长度
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午10:15:08 <br>
     */
    public static String randomPwd(int length) {
        if (length <= 0) {
            return "";
        }
        String pwd = RandomStringUtils.randomAlphanumeric(length);
        String var = "^(\\d+[a-zA-Z][\\da-zA-Z]*|[a-zA-Z]+\\d[\\da-zA-Z]*)$";
        boolean result = Pattern.matches(var, pwd);
        while (!result) {
            pwd = RandomStringUtils.randomAlphanumeric(length);
            result = Pattern.matches(var, pwd);
        }
        return pwd;
    }


    /**
     * 创建虚拟机名称根据IP地址
     *
     * @param ipAddress
     * @return
     */
    public static String createComputerName(String ipAddress) {
        return "w" + ipAddress.replace(".", "");
    }


    /**
     * 判断正则表达式是否匹配
     *
     * @param reg 正则表达式
     * @param str 要匹配的字符串
     * @return
     */
    public static boolean regexMatches(String reg, String str) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            flag = true;
        }
        return flag;
    }


    /**
     * [获取字符串的字节长度] <br>
     *
     * @param source
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:28:47 <br>
     */
    public static int getByteLength(String source) {
        int len = 0;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            int highByte = c >>> 8;
            len += highByte == 0 ? 1 : 2;
        }
        return len;
    }


    /**
     * [将字符串转成unicode] <br>
     *
     * @param str 待转字符串
     * @return unicode字符串<br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:32:03 <br>
     */
    public static String getUnicode(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>> 8) & 0xff; // 取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); // 取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
        }
        return (new String(sb));
    }

    /**
     * [获取当前时间，时是0时，分是0分，秒是0秒] <br>
     *
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:35:17 <br>
     */
    public static Date getMaxToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * [获取当前时间，时是23时，分是59分，秒是59秒] <br>
     *
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:35:49 <br>
     */
    public static Date getMinToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }


    /**
     * [获取指定季度的第一个月份值] <br>
     *
     * @param quarter 季度 1,2,3,4
     * @return 某个季度的第一个月份<br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:41:20 <br>
     */
    public static int getFirstMonthOfQuarters(int quarter) {
        int month = 0;
        switch (quarter) {
            case 1:
                month = 1;
                break;
            case 2:
                month = 4;
                break;
            case 3:
                month = 7;
                break;
            case 4:
                month = 10;
                break;
        }
        return month;
    }

    /**
     * [获取指定季度的最后一个月份值] <br>
     *
     * @param quarter 季度 1,2,3,4
     * @return 某个季度的最后一个月份<br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:41:20 <br>
     */
    public static int getLastMonthOfQuarters(int quarter) {
        int month = 0;
        switch (quarter) {
            case 1:
                month = 3;
                break;
            case 2:
                month = 6;
                break;
            case 3:
                month = 9;
                break;
            case 4:
                month = 12;
                break;
        }
        return month;
    }

    /**
     * [获取指定季度中文名称] <br>
     *
     * @param quarter 季度 1,2,3,4
     * @return 季度中文名称<br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:42:25 <br>
     */
    public static String getQuarterName(int quarter) {
        String quarterName = "";
        switch (quarter) {
            case 1:
                quarterName = "第一季度";
                break;
            case 2:
                quarterName = "第二季度";
                break;
            case 3:
                quarterName = "第三季度";
                break;
            case 4:
                quarterName = "第四季度";
                break;
        }
        return quarterName;
    }

    /**
     * [获取某年某月的最后一天] <br>
     *
     * @param year  年份
     * @param month int 月份（取值范围：1-12）
     * @return 某年某月的最后一天<br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:43:23 <br>
     */
    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// 默认的月份是从0开始的
        // 某年某月的最后一天
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * [ 判断是否为window系统] <br>
     *
     * @return <br>
     * @author LiBaozhen <br>
     * @date 2015-11-19 上午9:54:47 <br>
     */
    public static boolean isWindows() {
        String temp = System.getProperties().getProperty("os.name");
        if (temp != null && temp.split("Windows").length > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * [过滤一个对象,可忽略字段]<br>
     * writer: XiaXiaobing <br>
     * rq: 2014-6-5下午5:59:46<br>
     *
     * @param obj
     * @param ignoreFieldNames 忽略的字段，可多个，每个字段名需加上';'。
     * @return
     */
    public static Object fliterObject(Object obj, String ignoreFieldNames) {
        if (obj == null) {
            return obj;
        }
        try {
            Class<?> cls = Class.forName(obj.getClass().getName());
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                boolean is = field.getType().getName().equals(String.class.getName());
                if (is) {
                    Method setMethod = cls.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
                    Method getMethod = cls.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                    Object filterObject = getMethod.invoke(obj, null);
                    if (filterObject != null) {
                        if (ignoreFieldNames != null && ignoreFieldNames.contains(fieldName + ";")) {
                            setMethod.invoke(obj, filterObject);
                        } else {
                            setMethod.invoke(obj, (Object) filterStr(filterObject.toString()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * [给文件追加数据] <br>
     *
     * @param content 中心
     * @param url     地址
     * @return <br>
     * @author LiBaozhen <br>
     */
    public static boolean writeFileByAdd(String content, String url) {
        boolean status = false;
        try {
            BufferedReader in4 = new BufferedReader(new StringReader(content));

            File f = new File(url);
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter out1 = new PrintWriter(new BufferedWriter(
                    new FileWriter(url, true)));
            String s = "";
            while ((s = in4.readLine()) != null) {
                out1.println(s);
            }
            out1.close();
            in4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * [String转Timestamp] <br>
     *
     * @param date 时间
     * @return <
     * @author LiShan
     */
    public static Timestamp str2TimeStamp(String date) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }


    /**
     * SHA1 加密方法
     *
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 检验 是不是整数
     *
     * @param str
     * @return
     */
    public static boolean isNO(Object str) {
        if (!isEmpty(str)) {
            for (int i = str.toString().length(); --i >= 0; ) {
                if (!Character.isDigit(str.toString().charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取区间内的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static Integer getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;

    }


}
