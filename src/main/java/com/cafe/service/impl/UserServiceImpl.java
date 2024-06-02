package com.cafe.service.impl;

import com.cafe.constants.CafeConstants;
import com.cafe.entity.User;
import com.cafe.repository.UserRepository;
import com.cafe.security.CustomUserDetailsService;
import com.cafe.security.JwtRequestFilter;
import com.cafe.security.JwtUtil;
import com.cafe.service.UserService;
import com.cafe.utils.CafeUtils;
import com.cafe.utils.EmailUtils;
import com.cafe.wrapper.UserWrapper;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepository.findByEmail(requestMap.get(CafeConstants.EMAIL));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.EMAIL_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name") && requestMap.containsKey("email")
                && requestMap.containsKey("phoneNumber") && requestMap.containsKey("password");

    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setPhoneNumber(requestMap.get("phoneNumber"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
                    Authentication auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(auth.isAuthenticated()) {
                if(customUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<>("{\"token\":\"" +
                            jwtUtil.generateToken(customUserDetailsService.getUserDetail().getEmail(),
                                    customUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Bad Credentials" + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtRequestFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtRequestFilter.isAdmin()){
                Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmin());
                    return CafeUtils.getResponseEntity("User Status updated successfully!",HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("user id does not exist!", HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtRequestFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendMessage(jwtRequestFilter.getCurrentUser(),"Account Approved", "User:- "+ user +
                    "\n is approved by \nADMIN:-"+jwtRequestFilter.getCurrentUser(), allAdmin);
        }else{
            emailUtils.sendMessage(jwtRequestFilter.getCurrentUser(),"Account Disabled", "User:- "+ user +
                    "\n is Disabled by \nADMIN:-"+jwtRequestFilter.getCurrentUser(), allAdmin);

        }
    }
}
