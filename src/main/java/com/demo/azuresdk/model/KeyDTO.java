package com.demo.azuresdk.model;

import com.azure.security.keyvault.keys.models.KeyVaultKey;

public class KeyDTO {

    public static KeyModel getKeyModel(KeyVaultKey input, String keyVaultName) {
        KeyModel keyModel = new KeyModel(input.getId(), input.getName(), input.getKeyType().toString(), keyVaultName);
        if (input.getProperties().getNotBefore() != null) {
            keyModel.setActivationDate(input.getProperties().getNotBefore().toInstant().toEpochMilli());
        }

        if (input.getProperties().getExpiresOn() != null) {
            keyModel.setExpiryDate(input.getProperties().getExpiresOn().toInstant().toEpochMilli());
        }

        return keyModel;
    }
}
