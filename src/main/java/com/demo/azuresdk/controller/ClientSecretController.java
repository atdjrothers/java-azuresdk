package com.demo.azuresdk.controller;

import com.demo.azuresdk.model.ClientSecretModel;
import com.demo.azuresdk.service.ClientSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client-secrets")
public class ClientSecretController {

    @Autowired
    ClientSecretService service;

    @PostMapping
    public ResponseEntity<ClientSecretModel> createClientSecret(@RequestBody ClientSecretModel data) {
        return new ResponseEntity<>(service.createSecret(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ClientSecretModel> updateClientSecret(@RequestBody ClientSecretModel data) {
        return new ResponseEntity<>(service.createSecret(data), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{keyVaultName}")
    public ResponseEntity<List<ClientSecretModel>> getClientSecrets(@PathVariable String keyVaultName) {
        return new ResponseEntity<>(service.getSecrets(keyVaultName), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ClientSecretModel> getClientSecret(@RequestParam String name, @RequestParam String keyVaultName){
        ClientSecretModel clientSecretModel = service.getSecret(name, keyVaultName);
        if (clientSecretModel != null) {
            return new ResponseEntity<>(service.getSecret(name, keyVaultName), new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteClientSecret(@RequestParam String name, @RequestParam String keyVaultName){
        return new ResponseEntity<>(service.deleteSecret(name, keyVaultName), new HttpHeaders(), HttpStatus.OK);
    }

}
