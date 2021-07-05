package com.arjun.subjective.demo.entity;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
class Person implements Comparable<Person> {
    String name;
    Integer age;

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 实现 “Comparable<String>” 的接口，即重写compareTo<T t>函数。
     * 这里是通过“person的名字”进行比较的
     */
    @Override
    public int compareTo(Person person) {
        return name.compareTo(person.name);
    }

    public static void main(String[] args) {
        // 新建ArrayList(动态数组)
        ArrayList<Person> list = new ArrayList<Person>();
        // 添加对象到ArrayList中
        list.add(new Person("fff", 30));
        list.add(new Person("ccc", 20));
        list.add(new Person("eee", 30));
        list.add(new Person("bbb", 10));
        list.add(new Person("AAA", 30));
        list.add(new Person("ddd", 40));
        // 打印list的原始序列
        System.out.printf("原始序列, list:%s\n", list);
        // 对list进行排序
        // 这里会根据“Person实现的Comparable<String>接口”进行排序，即会根据“name”进行排序
        Collections.sort(list);
        System.out.printf("姓名排序, list:%s\n", list);

        Collections.sort(list, new AscAgeComparator());
        System.out.printf("年龄升序, list:%s\n", list);

        Collections.sort(list, new DescAgeComparator());
        System.out.printf("年龄降序, list:%s\n", list);

        list.sort((o1, o2) -> o2.getAge().compareTo(o1.getAge()));
        System.out.printf("年龄降序, list:%s\n", list);

        list.sort(Comparator.comparing(Person::getAge));
        System.out.printf("年龄升序, list:%s\n", list);

        list.sort(Comparator.comparing(Person::getAge).reversed());
        System.out.printf("lambda年龄降序, list:%s\n", list);

        Collections.sort(list, Comparator.comparing(Person::getName));
        System.out.printf("名称排序, list:%s\n", list);

        Collections.sort(list, (o1, o2) -> {
            int i = o1.getAge().compareTo(o2.getAge());
            if (i == 0) {
                return o2.getName().compareTo(o1.getName());
            }
            return i;
        });
        System.out.printf("按年龄升序，年龄相同则按名称降序排列, list:%s\n", list);

        List<Person> collect = list.stream().sorted(Comparator.comparing(Person::getAge)).collect(Collectors.toList());
        System.out.printf("使用Stream流方式按年龄升序, list:%s\n", collect);

        list.stream().sorted(Comparator.comparing(Person::getAge).reversed()
                .thenComparing(Person::getName)).collect(Collectors.toList())
                .forEach(System.out::println);

        //=============隔开================================================================================================
        System.out.println("=============隔开=====================================================================");

        List<Integer> nums = Arrays.asList(3, 5, 1, 0, 2, 4);
        System.out.println("原始序列：" + nums);
        //升序排序，默认写法
        Collections.sort(nums);
        System.out.println("升序排序，默认写法，nums = " + nums);
        //升序排序，流的写法
        nums.stream().sorted(Comparator.comparing(num -> num)).collect(Collectors.toList()).forEach(System.out::println);

        List<String> strList = Lists.newArrayList("AAA", "CBA", "abc", "E", "aceF", "bDfgH", "GBK");

        //自定义排序器,Comparator写法
        //升序
        strList.sort(Comparator.comparingInt(String::length));
        System.out.println("字符串升序：" + strList);
        //降序,显示的指定一个类型
        strList.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("字符串降序：" + strList);

        /**两层排序:先按照长度排序，再按照字符串顺序**/
        strList.sort(Comparator.comparingInt(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER));
        System.out.println("不区分大小写的排序" + strList);
        strList.sort(Comparator.comparingInt(String::length).thenComparing(String::compareTo));
        System.out.println("排序" + strList);
        strList.sort(Comparator.comparingInt(String::length).thenComparing((item1, item2) -> item1.toLowerCase().compareTo(item2.toLowerCase())));
        strList.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.comparing(String::toLowerCase)));
        System.out.println("字符串转换为小写排序" + strList);
        strList.sort(Comparator.comparingInt(String::length).thenComparing(String::toLowerCase, Comparator.reverseOrder()));
        System.out.println(strList);
        strList.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.comparing(String::toLowerCase, Comparator.reverseOrder())).
                thenComparing(Comparator.reverseOrder()));//和上一个结果是一样的，因为已经排好序了，最后一个就不起作用了

        strList.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.comparing(String::toLowerCase, Comparator.reverseOrder())).reversed());//和上一个结果是一样的，因为已经排好序了，最后一个就不起作用了
        System.out.println("list = " + strList);


    }

    /**
     * AscAgeComparator比较器
     * 它是“Person的age的升序比较器”
     */
    private static class AscAgeComparator implements Comparator<Person> {

        @Override
        public int compare(Person p1, Person p2) {
            return p1.getAge() - p2.getAge();
        }
    }


    /**
     * DescAgeComparator比较器
     * 它是“Person的age的升序比较器”
     */
    private static class DescAgeComparator implements Comparator<Person> {

        @Override
        public int compare(Person p1, Person p2) {
            return p2.getAge() - p1.getAge();
        }
    }
} 