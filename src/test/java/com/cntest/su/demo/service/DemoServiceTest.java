package com.cntest.su.demo.service;

import com.cntest.su.demo.BaseTest;
import com.cntest.su.demo.entity.User;
import com.cntest.su.demo.matcher.DivisibleBy;
import com.cntest.su.demo.matcher.IsEven;
import org.assertj.core.util.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.event.MenuEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.eventFrom;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.Matchers.typeCompatibleWith;

;

/**
 * Description： service unit test
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 14:23
 */
public class DemoServiceTest extends BaseTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void testHi() {
        String name = "test";
        Assert.assertEquals(demoService.hi(name), "hi:" + name);

        // 实际值（actual）与期望值（expected）相等
        Assert.assertThat("hi:test", is(equalTo(demoService.hi(name))));

        // 匹配给定类型的任何变量.
        Assert.assertThat("name", any(String.class));

        // 匹配任何东西。
        Assert.assertThat(5, anything());

        // 输入实际值大于指定值
        Assert.assertThat(11, greaterThan(10));

        // 输入实际值小于指定值
        Assert.assertThat(1, lessThan(2));

        // 输入测试值大于或等于指定值
        Assert.assertThat(10, greaterThanOrEqualTo(10));

        // 输入测试值小于或等于指定值
        Assert.assertThat(10, lessThanOrEqualTo(10));

        // 数组的长度必须与匹配器的数量匹配，并且它们的顺序很重要。
        String[] strings = {"why", "hello", "there"};
        Assert.assertThat(strings, arrayContaining("why", "hello", "there"));

        // vararg匹配器，如果输入String以给定的子字符串结尾，或者开头，则匹配的匹配器。以及相等。
        Assert.assertThat(strings, arrayContaining(startsWith("w"), equalTo("hello"), endsWith("re")));

        // 它们的顺序并不重要
        Assert.assertThat(strings, arrayContainingInAnyOrder("hello", "there", "why"));
        Assert.assertThat(strings, arrayWithSize(3));

        // 用于检查数组是否具有特定长度。
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(0, "o");
        objects.add(1, "a");
        objects.add(2, "b");
        objects.add(3, "c");
        Assert.assertThat(objects.toArray(), arrayWithSize(greaterThan(2)));

        // 输入Iterable具有指定大小时匹配或与指定大小匹配
        Assert.assertThat(objects, iterableWithSize(4));

        // 可以与Double或BigDecimal一起使用的匹配器，以检查值是否在期望值的指定误差范围内。
        Double i = 6.5;
        BigDecimal b = BigDecimal.valueOf(i);
        Assert.assertThat(i, closeTo(6, 0.5));
        Assert.assertThat(b, closeTo(BigDecimal.valueOf(6L), BigDecimal.valueOf(0.5d)));
        Assert.assertThat(BigDecimal.valueOf(600L), closeTo(BigDecimal.valueOf(650L), BigDecimal.valueOf(50L)));

        // 输入的匹配器值返回0，则匹配器将匹配，否则将不匹配
        Assert.assertThat(i, comparesEqualTo(6.5));
        Assert.assertThat(i, equalTo(6.5));

        // 测试集合的值，数相匹配
        Assert.assertThat(Arrays.asList(strings), contains("why", "hello", "there"));

        // 测试集合的值，数相匹配，顺序可以不同
        Assert.assertThat(Arrays.asList(strings), containsInAnyOrder("hello", "why", "there"));

        // 字符串是否包含给定的子字符串
        Assert.assertThat("value", containsString("l"));

        // 匹配空集合
        ArrayList<Object> list = Lists.newArrayList();
        Assert.assertThat(list, empty());

        // 如果输入数组的长度为0，则匹配的匹配器
        Double[] doubles = new Double[0];
        Assert.assertThat(doubles, emptyArray());

        // Typesafe匹配器，如果输入集合为给定类型且为空，则匹配。
        HashSet<String> set = new HashSet<>();
        Assert.assertThat(set, emptyCollectionOf(String.class));

        // 如果输入Iterable没有值，则匹配的匹配器。
        Assert.assertThat(set, emptyIterable());

        // Typesafe Matcher，如果输入的Iterable没有值并且是给定类型，则匹配。
        Assert.assertThat(set, emptyCollectionOf(String.class));

        // 如果输入的String值等于指定的String而忽略大小写，则匹配的匹配器。
        Assert.assertThat("value", equalToIgnoringCase("VAlUe"));

        // 输入的String值等于指定的String而不匹配多余的空格
        String testValue = "this    is   my    value    ";
        Assert.assertThat(testValue, equalToIgnoringWhiteSpace("  This  Is My           value"));

        // 如果输入EventObject来自给定源，则匹配器。也可以接受EventObject指定子类型的。
        Object source = new Object();
        EventObject testEvent = new EventObject(source);
        Assert.assertThat(testEvent, eventFrom(source));
        // EventObject指定子类型的
        EventObject menuEvent = new MenuEvent(source);
        Assert.assertThat(menuEvent, eventFrom(MenuEvent.class, source));

        // 给定映射包含与指定键和值匹配的条目
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "hi");
        map.put(2, "hello");
        Assert.assertThat(map, hasEntry(2, "hello"));
        Assert.assertThat(map, hasEntry(greaterThan(1), startsWith("h")));

        // 输入Map具有至少一个与指定值或匹配项匹配的键
        Assert.assertThat(map, hasKey(1));
        Assert.assertThat(map, hasKey(lessThan(2)));

        // 输入Map具有至少一个与指定值或匹配器匹配的值
        Assert.assertThat(map, hasValue(startsWith("h")));

        // 输入Iterable具有至少一个与指定值或匹配器匹配的项目
        Assert.assertThat(objects, hasItems("a", "o"));
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        Assert.assertThat(integers, hasItem(lessThan(3)));

        // 输入数组具有至少一个与指定值或匹配器匹配的项目
        Assert.assertThat(strings, hasItemInArray("there"));
        Assert.assertThat(strings, hasItemInArray(startsWith("t")));

        // 输入对象满足Bean约定并且具有具有指定名称的属性，并且该属性的值可选地
        User user = new User();
        user.setAge(12);
        user.setName("fei");
        Assert.assertThat(user, hasProperty("age", greaterThan(10)));
        Assert.assertThat(user, hasProperty("name"));

        // 输入Collection具有指定的大小，或者它的大小与指定的匹配器匹配.
        Assert.assertThat(integers, hasSize(lessThan(10)));

        // 输入对象的toString（）方法匹配指定的String或输入匹配器.
        Assert.assertThat(3.1415926, hasToString("3.1415926"));
        Assert.assertThat(3.1415926, hasToString(containsString(".")));

        // 输入对象是给定类型
        Assert.assertThat("Hello World !", instanceOf(String.class));

        // 输入字符串为空或null时匹配
        Assert.assertThat("", isEmptyOrNullString());
        Assert.assertThat(null, isEmptyOrNullString());

        // 输入字符串为空时匹配
        Assert.assertThat("", isEmptyString());

        // 空值与非空值匹配
        Assert.assertThat(null, nullValue());
        Assert.assertThat("", notNullValue());

        // 当在给定的Collection或Array中找到输入项时匹配
        Assert.assertThat(6, isIn(integers));

        // 当输入对象是给定对象之一时匹配
        Assert.assertThat("!", isOneOf("$", "#", "%", "!", "￥"));

        // 反转其匹配逻辑
        Assert.assertThat("apple", not("orange"));
        Assert.assertThat(5, not(lessThan(2)));

        // 输入对象与指定值相同的实例时匹配
        User user1 = user;
        Assert.assertThat(user, sameInstance(user1));
        Assert.assertThat(user, theInstance(user1));

        //输入Bean具有与指定Bean相同的属性值时匹配
        User user2 = new User();
        user2.setAge(12);
        user2.setName("fei");
        Assert.assertThat(user, samePropertyValuesAs(user2));

        // 输入字符串包含给定Iterable中的子字符串，则按从Iterable返回的顺序进行匹配
        String test = "Rule number one: two's company, but three's a crowd!";
        Assert.assertThat(test, stringContainsInOrder(Arrays.asList("one", "two", "three")));

        //输入类型的对象可以分配给指定基本类型的引用时进行匹配
        Assert.assertThat(User.class, typeCompatibleWith(Object.class));

        // 组合匹配器
        // 类似于逻辑AND
        Assert.assertThat(test, allOf(startsWith("R"), endsWith("!")));
        List<Matcher<? super String>> matchers = Arrays.asList(containsString("one"), startsWith("R"));
        Assert.assertThat(test, allOf(matchers));

        // 类似于逻辑或
        Assert.assertThat(test, anyOf(containsString("hello"), instanceOf(String.class)));
        Assert.assertThat(test, anyOf(matchers));

        // 输入数组的元素分别使用指定的匹配器按顺序匹配时匹配。匹配器的数量必须等于数组的大小。
        Assert.assertThat(strings, array(startsWith("w"), equalTo("hello"), notNullValue()));

        // 当与它的可组合匹配器.and（）结合使用时，将在两个指定的匹配器匹配时匹配。(都)
        Assert.assertThat(test, both(startsWith("R")).and(containsString("two")));

        // 当与它的可组合匹配器.or（）结合使用时，如果指定的匹配器匹配（要么）
        Assert.assertThat(test, either(notNullValue()).or(instanceOf(String.class)));

        // 当输入匹配器匹配时匹配 类似 not
        Assert.assertThat(1, Matchers.is(1));

        // 用于覆盖另一个匹配器的失败输出。当需要自定义故障输出时使用。参数是失败消息，原始Matcher，然后是将使用占位符％0，％1，％2格式化为失败消息的所有值。
        Integer actual = 7;
        Integer expected = 10;
        Assert.assertThat(actual, describedAs("input > %0", greaterThan(expected), expected));
    }

    @Test
    public void testHello(){
        // 自定义匹配器

        // 判断偶数
        int i = 4;
        Assert.assertThat(i, IsEven.isEven());

        // 判断整除
        int b = 5;
        Assert.assertThat(101, DivisibleBy.divisibleBy(5));
    }
}