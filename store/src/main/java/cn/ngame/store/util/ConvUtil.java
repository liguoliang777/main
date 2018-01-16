package cn.ngame.store.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换类型用的工具类
 */
public class ConvUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 转换字符串，如果转换失败，返回空字符串
     */
    public static String NS(Object value) {
        return NS(value, "");
    }

    /**
     * 转换字符串，如果转换失败，返回指定的字符串
     */
    public static String NS(Object value, String defaultValue) {
        if (value == null) return defaultValue;
        return value.toString();
    }

    /**
     * 转换任意对象到整型，如果转换失败返回0
     */
    public static int NI(Object value) {
        return NI(value, 0);
    }

    /**
     * 转换任意对象到整型，如果转换失败返回defaultValue
     */
    public static int NI(Object value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            int ret = Integer.parseInt(value.toString());
            return ret;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换任意对象到Short类型，如果转换失败返回0
     */
    public static short NSI(Object value) {
        return NSI(value, (short) 0);
    }

    /**
     * 转换任意对象到short，如果转换失败返回默认值(defaultValue)
     */
    public static short NSI(Object value, short defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Short.parseShort(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将任意对象转换成Long类型，如果转换失败则返回0L
     */
    public static long NL(Object value) {
        return NL(value, 0L);
    }

    /**
     * 将任意对象转换为Long类型，如果转换失败则返回defaultValue
     */
    public static long NL(Object value, long defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将任意对象转换为Double类型，如果转换失败则返回0D
     */
    public static float NF(Object value) {
        return NF(value, 0f);
    }

    /**
     * 将任意对象转换为Double类型，如果转换失败则返回defaultValue
     */
    public static float NF(Object value, float defaultValue) {
        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将任意对象转换为Double类型，如果转换失败则返回0D
     */
    public static double ND(Object value) {
        return ND(value, 0D);
    }

    /**
     * 将任意对象转换为Double类型，如果转换失败则返回defaultValue
     */
    public static double ND(Object value, double defaultValue) {
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将任意对象转换为BigDecimal类型，如果转换失败则返回0M
     */
    public static BigDecimal NDec(Object value) {
        return NDec(value, BigDecimal.ZERO);
    }

    /**
     * 将任意对象转换为BigDecimal类型，如果转换失败则返回defaultValue
     */
    public static BigDecimal NDec(Object value, BigDecimal defaultValue) {
        if (value == null) return defaultValue;
        try {
            return BigDecimal.valueOf(ConvUtil.ND(value, defaultValue.doubleValue()));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换任意对象到Boolean，如果转换失败返回false
     */
    public static Boolean NB(Object value) {
        return NB(value, false);
    }

    /**
     * 转换任意对象到Boolean，如果转换失败返回默认值
     */
    public static Boolean NB(Object value, Boolean defValue) {
        if (value == null) return defValue;
        try {
            return Boolean.parseBoolean(value.toString());
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 将某种类型转换为另一种类型
     */
    public static Object TryConvert(Object value, Class<?> declaringClass) {
        if (declaringClass.isInstance(value)) {
            return value;
        }
        if (declaringClass == Integer.class || declaringClass == int.class) {
            return ConvUtil.NI(value);

        } else if (declaringClass == Long.class || declaringClass == long.class) {
            return ConvUtil.NL(value);

        } else if (declaringClass == String.class) {
            return ConvUtil.NS(value);

        } else if (declaringClass == Double.class || declaringClass == double.class) {
            return ConvUtil.ND(value);

        } else if (declaringClass == Float.class || declaringClass == float.class) {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception e) {
                return Float.NaN;
            }

        } else if (declaringClass == Short.class || declaringClass == short.class) {
            return ConvUtil.NSI(value);

        } else if (declaringClass == Byte.class || declaringClass == byte.class) {
            return (byte) ConvUtil.NSI(value);

        } else if (declaringClass == Character.class || declaringClass == char.class) {
            return (char) ConvUtil.NSI(value);

        } else if (declaringClass == BigDecimal.class) {
            return ConvUtil.NDec(value);

        } else {
            throw new RuntimeException("Unrecognized Type");
        }
    }

    /**
     * 将时间转换成时间戳，时间必须是yyyy-MM-dd HH:mm:ss类型的字符串
     */
    public static long dataTimeToTimespan(String uploadTime) {
        long timeStemp = 0;
        try {
            if (uploadTime != null && uploadTime.length() > 0) {
                if (uploadTime.contains("-")) { // 返回的为正常时间则需要转换为时间戳

                    Date date = simpleDateFormat.parse(uploadTime);
                    timeStemp = (long) (date.getTime() / 1000L);
                    return timeStemp;

                } else {
                    timeStemp = Long.parseLong(uploadTime);// 传递过来的为时间戳则直接转换
                }
            }
        } catch (Exception e) {
            return timeStemp;
        }
        return timeStemp;
    }
}
