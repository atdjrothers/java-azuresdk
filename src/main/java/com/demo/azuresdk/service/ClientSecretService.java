package com.demo.azuresdk.service;

import com.demo.azuresdk.model.ClientSecretModel;

import java.util.List;

public interface ClientSecretService {
    ClientSecretModel createSecret(ClientSecretModel data);
    ClientSecretModel getSecret(String name, String keyVaultName);
    List<ClientSecretModel> getSecrets(String keyVaultName);
    boolean deleteSecret(String name, String keyVaultName);
}
