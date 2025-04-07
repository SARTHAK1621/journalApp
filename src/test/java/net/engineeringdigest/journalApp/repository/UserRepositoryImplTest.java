package net.engineeringdigest.journalApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTest {
    @Autowired
    public UserRepositoryImpl userRepositoryImpl;
    @Test
    public void testMethod(){
        userRepositoryImpl.getuserForSA();
    }
}
