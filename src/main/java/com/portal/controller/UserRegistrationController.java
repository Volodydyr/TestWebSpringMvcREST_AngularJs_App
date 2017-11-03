package com.portal.controller;

import com.portal.exceptions.ConflictException;
import com.portal.exceptions.UserNotFoundException;
import com.portal.model.User;
import com.portal.repository.UserDao;
import com.portal.repository.WalletDao;
import com.portal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserRegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private WalletDao walletDao;

    @RequestMapping(value = "/user/", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Iterable<User>> listAllUsers() {
            return ResponseEntity.ok(userDao.findAll());
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        System.out.println("Finding User with id " + id);
        User user = userDao.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return  ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        System.out.println("Creating User " + user.getUsername());
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        System.out.println("Updating User with id:" + id);
        return userService.updateUser(user,  id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        System.out.println("Deleting User with id " + id);
        User user = userDao.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}