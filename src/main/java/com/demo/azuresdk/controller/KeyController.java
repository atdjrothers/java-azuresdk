package com.demo.azuresdk.controller;

import com.demo.azuresdk.model.KeyModel;
import com.demo.azuresdk.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("keys")
public class KeyController {

    @Autowired
    KeyService service;

    @PostMapping
    public ResponseEntity<KeyModel> createKey(@RequestBody KeyModel data) {
        return new ResponseEntity<>(service.createKey(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<KeyModel> updateKey(@RequestBody KeyModel data) {
        return new ResponseEntity<>(service.updateKey(data), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{keyVaultName}")
    public ResponseEntity<List<KeyModel>> getKeys(@PathVariable String keyVaultName) {
        return new ResponseEntity<>(service.getKeys(keyVaultName), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<KeyModel> getKey(@RequestParam String name, @RequestParam String keyVaultName){
        KeyModel KeyModel = service.getKey(name, keyVaultName);
        if (KeyModel != null) {
            return new ResponseEntity<>(service.getKey(name, keyVaultName), new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteKey(@RequestParam String name, @RequestParam String keyVaultName){
        return new ResponseEntity<>(service.deleteKey(name, keyVaultName), new HttpHeaders(), HttpStatus.OK);
    }

}
