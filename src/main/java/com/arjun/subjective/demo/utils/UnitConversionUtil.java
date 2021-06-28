package com.arjun.subjective.demo.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 单位换算.
 *
 * @author arjun
 * @date 2021/05/08
 */
public class UnitConversionUtil {

    /**
     * 数字格式化显示
     * 小于万默认显示 大于万以1.7万方式显示最大是9999.9万
     * 大于亿以1.1亿方式显示最大没有限制都是亿单位
     *
     * @param num   格式化的数字
     * @param kBool 是否格式化千,为true,并且num大于999就显示999+,小于等于999就正常显示
     * @return
     */
    public static String formatNum(String num, Boolean kBool) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isNumeric(num)) {
            return "0";
        }
        if (kBool == null) {
            kBool = false;
        }

        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b2 = new BigDecimal("100000000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";
        String nuit = "";

        // 以千为单位处理
        if (kBool) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                return "999+";
            }
            return num;
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            sb.append(b3.toString());
        } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                || b3.compareTo(b2) == -1) {
            formatNumStr = b3.divide(b1).toString();
            nuit = "万";
        } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
            formatNumStr = b3.divide(b2).toString();
            nuit = "亿";
        }
        if (!"".equals(formatNumStr)) {
            int i = formatNumStr.indexOf(".");
            if (i == -1) {
                sb.append(formatNumStr).append(nuit);
            } else {
                i = i + 1;
                String v = formatNumStr.substring(i, i + 1);
                if (!v.equals("0")) {
                    sb.append(formatNumStr.substring(0, i + 1)).append(nuit);
                } else {
                    sb.append(formatNumStr.substring(0, i - 1)).append(nuit);
                }
            }
        }
        if (sb.length() == 0) {
            return "0";
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(formatNum("1000000", true));
        System.out.println(formatNum("1000000", false));
        System.out.println(formatNum("1507000", false));

        //----------------------------------------------

        // 1E8 为一亿，即 1*10的8次方
        double i = 1E8D;
        // toPlainString()可以装换为数值
        System.out.println(BigDecimal.valueOf(1E8D).toPlainString());
        System.out.println(10 * 10 * 10 * 10 * 10 * 10 * 10 * 10);

    }
}
