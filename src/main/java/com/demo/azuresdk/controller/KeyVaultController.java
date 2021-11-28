package com.demo.azuresdk.controller;

import com.demo.azuresdk.model.KeyVaultModel;
import com.demo.azuresdk.service.KeyVaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("key-vaults")
public class KeyVaultController {

    @Autowired
    KeyVaultService service;

    @PostMapping
    public ResponseEntity<KeyVaultModel> createVault(@RequestParam String name, @RequestParam String resourceGroupName) {
        KeyVaultModel data = service.createVault(name, resourceGroupName);
        return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{resourceGroupName}")
    public ResponseEntity<List<KeyVaultModel>> getVaultsByResourceGroup(@PathVariable String resourceGroupName) {
        return new ResponseEntity<>(service.getVaults(resourceGroupName), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<KeyVaultModel> getVault(@RequestParam String name, @RequestParam String resourceGroupName){
        KeyVaultModel data = service.getVault(name, resourceGroupName);
        if (data != null) {
            return new ResponseEntity<>(service.getVault(name, resourceGroupName), new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteVault(@RequestParam String id, @RequestParam String resourceGroupName){
        return new ResponseEntity<>(service.deleteVault(id, resourceGroupName), new HttpHeaders(), HttpStatus.OK);
    }
}
