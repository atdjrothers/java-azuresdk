package com.demo.azuresdk.service;

import com.demo.azuresdk.model.KeyModel;

import java.util.List;

public interface KeyService {
    KeyModel createKey(KeyModel data);
    KeyModel getKey(String name, String keyVaultName);
    List<KeyModel> getKeys(String keyVaultName);
    KeyModel updateKey(KeyModel data);
    boolean deleteKey(String name, String keyVaultName);
}
