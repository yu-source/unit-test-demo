package com.cntest.su.demo.utils;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.springframework.beans.BeanUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author arjun
 * @date 2021/01/18
 */
public class ParseEfileUtil {

    /**
     * 读文件
     * eFileName : 读取文件的文件名
     * node ： E文件里的节点名称
     */
    public static List<List<String>> readEFile(String eFileName, String node) {
        String path = "C:\\Users\\Lee\\Desktop" + File.separator + eFileName;
        try {
            int startIndex = 0;
            int thisIndex = 0;
            int endIndex = 0;
            boolean flag = false;
            //文件内容的字符集 UTF8
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
            String line = reader.readLine();
            List<List<String>> listDatas = new ArrayList<List<String>>();
            while ((line = reader.readLine()) != null && flag == false) {
                thisIndex++;
                if (line.startsWith("<" + node)) {
                    startIndex = thisIndex;
                } else if (line.startsWith("</" + node)) {
                    endIndex = thisIndex;
                    flag = true;
                } else if (startIndex != 0) {
                    String[] split = line.split("\\s+");
                    List<String> lineDatas = new ArrayList<String>(Arrays.asList(split));
                    lineDatas.remove(0);
                    listDatas.add(lineDatas);
                }
            }
            System.err.println(node + "节点标签在第" + startIndex + "-" + endIndex);
            reader.close();
            return listDatas;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 写文件
     */
    public static void writeTxt(String txtInfo, String fileName) throws IOException {
        String filePath = "/path" + "/" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        // write 解决中文乱码问题
        // FileWriter fw = new FileWriter(file, true);
        // 写入文件的字符集 GBK 看需求而设定
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), "GBK");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(txtInfo);
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * @param filePath 文件路径
     * @return 返回map
     * @throws IOException
     */
    public static Map<String, List<List<String>>> getE(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> stringList = Files.readLines(path.toFile(), Charset.defaultCharset());
        Map<Boolean, List<String>> booleanListMap = stringList.stream().collect(Collectors.groupingBy(s -> s.contains("<")));
        List<String> keyList = booleanListMap.get(true);
        List<Integer> indexList = keyList.stream().map(s -> stringList.indexOf(s)).collect(Collectors.toList());
        Map<String, List<List<String>>> hashMap = new HashMap<>();
        hashMap.put(keyList.get(0), null);
        for (int i = 2; i < indexList.size(); i = i + 2) {
            hashMap.put(keyList.get(i - 1),
                    stringList.subList(indexList.get(i - 1) + 3, indexList.get(i))
                            .stream().map(s -> s.replace("#\t", "")).map(s -> Arrays.asList(s.split("\\s+"))).collect(Collectors.toList()));
        }
        return hashMap;
    }

//    list<T> paseBean() {
//
//    }

    public <T> List<T> setObjVal(Class<T> tClass, List<?> value) throws IllegalAccessException, InstantiationException {
        if (tClass == null || value == null) {
            return null;
        }
        // 取
        List list = (List) value.get(0);



        ArrayList<T> classes = Lists.newArrayList(tClass.newInstance());

        List<String> periodList = Lists.newArrayList();

        List<Field> fields = getFieldOfClass(tClass);

        for (int j = 0; j < periodList.size(); j++) {
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getName().equals(periodList.get(j))) {
                    try {
                        // 获取属性的名字
                        Field each = fields.get(i);
                        // 为set方法set值
                        each.set(tClass.newInstance(), value.get(j));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return classes;
    }

    private static final Map<String, List<Field>> CACHE = new HashMap<String, List<Field>>();

    /**
     * 获取实体类的所有属性，返回Field列表
     *
     * @param clazz
     * @return
     */
    private static List<Field> getFieldOfClass(Class<?> clazz) {
        List<Field> fields = CACHE.get(clazz.getName());
        if (fields == null) {
            Field[] fieldsArr = clazz.getDeclaredFields();
            fields = new ArrayList<>(fieldsArr.length);
            for (Field field : fieldsArr) {
                field.setAccessible(true);
                fields.add(field);
            }
            CACHE.put(clazz.getName(), fields);
        }
        return fields;
    }

    public static void main(String[] args) throws IOException {
        List<List<String>> lists = readEFile("StandbyDbInfo.txt", "XLJH");

        lists.forEach(System.out::println);

//        Map<String, List<List<String>>> map = getE("C:\\Users\\Lee\\Desktop\\StandbyDbInfo.txt");
//        System.out.println(map);
    }
}
