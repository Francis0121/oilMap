package com.oilMap.server.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by Francis on 2015-05-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class AuthServiceTest {
    
    private static Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);
    
    @Qualifier(value = "authServiceImpl")
    @Autowired
    private AuthService authService;
    
    private Auth auth = new Auth("1234567890987654");
    
    @Before
    public void Before() {
        logger.debug("Before");
        authService.insert(auth);
    }

    @Test
    @Transactional
    public void 사용자_입력_및_조회() throws Exception {
        logger.debug("사용자 입력");
        Auth getAuth = authService.selectIsExist(auth);
        logger.debug("GetAuth"+getAuth.toString());
        assertThat(auth.getId(), is(getAuth.getId()));
        assertThat(null, not(getAuth.getRegisterDate()));
    }
    
    @Test
    @Transactional
    public void 사용자_삭제() throws Exception{
        logger.debug("사용자 삭제");
        Auth auth2 = new Auth("1234567890987652");
        authService.insert(auth2);
        Auth getAuth = authService.selectIsExist(auth2);
        assertThat(auth2.getId(), is(getAuth.getId()));
        assertThat(null, not(getAuth.getRegisterDate()));
        
        authService.delete(auth2);
        getAuth = authService.selectIsExist(auth2);
        assertThat(null, is(getAuth));

        getAuth = authService.selectIsExist(auth);
        assertThat(auth.getId(), is(getAuth.getId()));
        assertThat(null, not(getAuth.getRegisterDate()));
    }
    
}
