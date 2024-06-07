package com.cafe.controller;

import com.cafe.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Operation(
            summary = "Get Count on Dashboard",
            description = "Endpoint to get count all everything on dashboard."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getCount(){
        try{
            return dashboardService.getCount();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
