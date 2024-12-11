package com.leejp.redistest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.leejp.redistest.service.RedisService;


@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;


    // POST [http://localhost:8080/api/redis/save?key=myKey&value=initialValue]
    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        System.out.println("RedisController.saveData");
        redisService.saveData(key, value);
        return "Data saved successfully!";
    }

    // GET [http://localhost:8080/api/redis/get?key=myKey]
    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        System.out.println("RedisController.getData");
        return redisService.getData(key);
    }

    // PUT [http://localhost:8080/api/redis/update?key=myKey&value=newValue2]
    @PutMapping("/update")
    public String updateData(@RequestParam String key, @RequestParam String value) {
        System.out.println("RedisController.updateData");
        redisService.updateData(key, value);
        return "Data updated successfully!";
    }

    // [http://localhost:8080/api/redis/delete?key=myKey]
    @DeleteMapping("/delete")
    public String deleteData(@RequestParam String key) {
        System.out.println("RedisController.deleteData");
        redisService.deleteData(key);
        return "Data deleted successfully!";
    }
}

