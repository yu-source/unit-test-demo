package com.cntest.su.demo.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 对 Map.key/value 进行排序
 *
 * @author arjun
 */
public class MapSortUtil {

    public static void main(String[] args) {
        Map<String, String> map = ImmutableMap.of("cc", "cc", "aa", "aa", "dd", "dd", "bb", "bb");
        System.out.println("原始的map：" + map);
        System.out.println("根据map的key降序：" + sortByKey(map, true));
        System.out.println("根据map的key升序：" + sortByKey(map, false));
        System.out.println("根据map的value降序：" + sortByValue(map, true));
        System.out.println("根据map的value升序：" + sortByValue(map, false));

        Map<String, Double> map1 = new HashMap<>();
        map1.put("SUM(P0000)", 1200D);
        map1.put("SUM(P2345)", 820D);
        map1.put("SUM(P1615)", 940D);
        map1.put("SUM(P2045)", 1000D);

        System.out.println("map1：" + map1);
        System.out.println("根据map1的key升序：" + sortByKey(map1, false));
        System.out.println("根据map1的value升序：" + sortByValue(map1, false));

        Map<String, String> map2 = new TreeMap<>();
        map2.put("b", "ccccc");
        map2.put("d", "aaaaa");
        map2.put("c", "bbbbb");
        map2.put("a", "ddddd");

        System.out.println("map2：" + map2);
        System.out.println("根据map2的key升序：" + sortByKey(map2, false));
        System.out.println("根据map2的value升序：" + sortByValue(map2, false));
    }

    /**
     * 根据map的key排序
     *
     * @param map    待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 根据map的value排序
     *
     * @param map    待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEach(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
}
