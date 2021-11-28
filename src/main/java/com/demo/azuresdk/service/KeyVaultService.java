package com.demo.azuresdk.service;

import com.demo.azuresdk.model.KeyVaultModel;

import java.util.List;

public interface KeyVaultService {
    KeyVaultModel createVault(String name, String resourceGroupName);
    KeyVaultModel getVault(String name, String resourceGroupName);
    List<KeyVaultModel> getVaults(String name);
    boolean deleteVault(String id, String resourceGroupName);
}
