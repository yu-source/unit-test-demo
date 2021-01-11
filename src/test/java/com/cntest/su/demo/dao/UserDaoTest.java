package com.cntest.su.demo.dao;

import com.cntest.su.demo.BaseTest;
import com.cntest.su.demo.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description： demo dao test
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 14:57
 */
@Transactional
public class UserDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Rollback
    public void testGet() {
        userDao.insert(3, "AAA", 20);
        User u = userDao.getById(3);
        Assert.assertEquals(20, u.getAge().intValue());
        Assert.assertEquals("AAA", u.getName());
    }
}
