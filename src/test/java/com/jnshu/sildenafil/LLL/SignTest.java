package com.jnshu.sildenafil.LLL;

import com.jnshu.sildenafil.system.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * #Title: SignTest
 * #ProjectName sildenafil
 * #Description: TODO
 * #author lihoo
 * #date 2018/11/2-16:53
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignTest {
    @Autowired
    private SignService signService;
    @Test
    public void testSign() {
    }

    @Test
    public void getSignList() throws Exception {
        signService.getSignList(1L).forEach(System.out::println);
    }
}
