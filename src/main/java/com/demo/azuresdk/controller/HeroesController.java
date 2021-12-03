package com.demo.azuresdk.controller;

import com.demo.azuresdk.service.HeroesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("heroes")
public class HeroesController {

    @Autowired
    HeroesService service;

    @GetMapping
    public ResponseEntity<String> getHeroes(){
        return new ResponseEntity<>(service.getHeroes(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
