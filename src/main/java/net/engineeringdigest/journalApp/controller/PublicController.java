package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private UserService userService;
    @RequestMapping("/health-check")
    public String healthCheck(){
        System.out.println("working");
        return "OK";
    }
    @PostMapping("/create-user")
    public ResponseEntity<?> createuser(@RequestBody User user){

        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            return new ResponseEntity<>("User already exist",HttpStatus.CONFLICT);

        }
    }
}
