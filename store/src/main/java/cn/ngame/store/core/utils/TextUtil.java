package cn.ngame.store.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 * Created by zeng on 2016/6/14.
 */
public class TextUtil {

    /**
     * @param str 被判的字符串
     * @return 如果任何一个字符串为null, 则返回true
     */
    public static boolean isAnyEmpty(String... str) {

        for (String s : str) {
            if (s == null || s.length() <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @return 如果为空则返回 true
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否是合法字符串
     *
     * @param str 被校验的字符串
     * @param reg 正则表达式
     * @return
     */
    public static boolean isLegal(String str, String reg) {

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 检测是否是合法的手机号
     *
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {

        return isLegal(phone, "^1[3|4|5|7|8]\\d{9}$");
    }

    /**
     * 文件单位转换
     *
     * @param size 文件大小，单位字节（Byte）
     * @return 最小单位KB
     */
    public static String formatFileSize(long size) {

        String sizeStr;
        if ((size = size / 1024) > 1024) {

            sizeStr = Math.round(size/1024) + "M";
            sizeStr = size / 1024 + "M";
        } else {
            sizeStr = Math.round(size) + "K";
            sizeStr = size + "K";
        }

        return sizeStr;
    }

    /**
     * 格式化下载数值
     *
     * @param count 数值
     * @return 格式化后的字符串
     */
    public static String formatCount(long count) {

        String countStr;

        if (count > 1000) {
            countStr = Math.round(count / 1000) + "千";
        } else if (count > 10000) {
            countStr = Math.round(count / 10000) + "万";
        } else if (count > 100000) {
            countStr = Math.round(count / 100000) + "十万";
        } else if (count > 1000000) {
            countStr = Math.round(count / 1000000) + "百万";
        } else {
            countStr = count + "";
        }
        return countStr;
    }
}
