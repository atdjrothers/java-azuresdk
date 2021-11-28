package com.demo.azuresdk.model;

import com.azure.resourcemanager.keyvault.models.Vault;

public class KeyVaultModelDTO {

    public static KeyVaultModel getKeyVaultModel(Vault vault) {
//        KeyVaultModel keyVaultModel = new KeyVaultModel(vault.name(), vault.resourceGroupName());
//        keyVaultModel.setVaultUri(vault.vaultUri());
//        keyVaultModel.setKeys(vault.keys());
//        keyVaultModel.setSecrets(vault.secrets().list().stream().map(s -> new ClientSecretModel(s.id(), s.name(), s.getValue(), vault.name())).collect(Collectors.toList()));
//        keyVaultModel.setAccessPolicies(vault.accessPolicies());

        KeyVaultModel keyVaultModel = new KeyVaultModel(vault.name(), vault.resourceGroupName());
        keyVaultModel.setId(vault.id());
        keyVaultModel.setVaultUri(vault.vaultUri());
        return keyVaultModel;
    }
}
