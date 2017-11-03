package com.portal.services;

import com.portal.exceptions.ConflictException;
import com.portal.exceptions.UserNotFoundException;
import com.portal.model.User;
import com.portal.model.Wallet;
import com.portal.repository.UserDao;
import com.portal.repository.WalletDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private WalletDao walletDao;


    public void saveUser(User user){
        if (isUserExist(user)) {
            throw  new ConflictException();
        }
        if(user.getWallet() != null) {
            walletDao.save(user.getWallet());
        }
        userDao.save(user);
    }

    public ResponseEntity<User> updateUser(User user, Long id){
        User realUser = userDao.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        realUser.setUsername(user.getUsername());
        realUser.setLocation(user.getLocation());
        realUser.setEmail(user.getEmail());

        Wallet wallet;
        if(realUser.getWallet() != null) {
             wallet = walletDao.findOne(realUser.getWallet().getId());
            if (wallet != null) {
                wallet.setAmount(user.getWallet().getAmount());
                wallet.setCurrency(user.getWallet().getCurrency());
            }
        }
        else {
            wallet = user.getWallet();
            realUser.setWallet(wallet);
        }
      if(wallet != null) {
          walletDao.save(wallet);
      }
        userDao.save(realUser);
        return ResponseEntity.ok(realUser);
    }

    public void deleteUser(User user){
        Wallet wallet = null;
        if(user.getWallet() != null) {
            wallet = walletDao.findOne(user.getWallet().getId());
        }
            userDao.delete(user);
        if(wallet != null){
            walletDao.delete(wallet);
        }
    }

    public boolean isUserExist(User user){
        return userDao.findOne(user.getId()) != null;
    }

}
