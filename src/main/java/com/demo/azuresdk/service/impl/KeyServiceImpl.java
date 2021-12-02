package com.demo.azuresdk.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.DeletedKey;
import com.azure.security.keyvault.keys.models.KeyProperties;
import com.azure.security.keyvault.keys.models.KeyType;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.demo.azuresdk.model.KeyDTO;
import com.demo.azuresdk.model.KeyModel;
import com.demo.azuresdk.service.KeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class KeyServiceImpl implements KeyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyServiceImpl.class);
    private static final String VAULT_URL_FORMAT = "https://%s.vault.azure.net";

    @Override
    public KeyModel createKey(KeyModel data) {
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl(String.format(VAULT_URL_FORMAT, data.getKeyVaultName()))
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        keyClient.createKey(data.getName(), KeyType.fromString(data.getType()));

        KeyVaultKey keyVaultKey = keyClient.getKey(data.getName());
        LOGGER.debug(keyVaultKey.getName() + " " + keyVaultKey.getId());
        return KeyDTO.getKeyModel(keyVaultKey, data.getKeyVaultName());
    }

    @Override
    public KeyModel getKey(String name, String keyVaultName) {
        KeyVaultKey keyVaultKey = null;
        try {
            KeyClient keyClient = new KeyClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
            keyVaultKey = keyClient.getKey(name);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        if (keyVaultKey != null) {
            return KeyDTO.getKeyModel(keyVaultKey, keyVaultName);
        }

        return null;
    }

    @Override
    public List<KeyModel> getKeys(String keyVaultName) {
        List<KeyModel> keys = new ArrayList<>();
        try {
            KeyClient keyClient = new KeyClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();

            PagedIterable<KeyProperties> keyProperties = keyClient.listPropertiesOfKeys();
            keyProperties.stream().forEach(s -> {
                KeyVaultKey keyVaultKey = keyClient.getKey(s.getName());
                if (keyVaultKey != null) {
                    keys.add(new KeyModel(keyVaultKey.getId(), keyVaultKey.getName(), keyVaultKey.getKey().toString(), keyVaultName));
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return keys;
    }

    @Override
    public KeyModel updateKey(KeyModel data) {
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl(String.format(VAULT_URL_FORMAT, data.getKeyVaultName()))
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        KeyProperties keyProperties = keyClient.getKey(data.getName()).getProperties();
        ZoneId systemZone = ZoneId.systemDefault();

        LocalDateTime expiryDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(data.getExpiryDate()),
                        TimeZone.getDefault().toZoneId());

        ZoneOffset currentOffsetForExpiry = systemZone.getRules().getOffset(expiryDateTime);
        keyProperties.setExpiresOn(OffsetDateTime.of(expiryDateTime, currentOffsetForExpiry));

        LocalDateTime activationDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(data.getActivationDate()),
                        TimeZone.getDefault().toZoneId());
        ZoneOffset currentOffsetForActivation = systemZone.getRules().getOffset(activationDateTime);
        keyProperties.setNotBefore(OffsetDateTime.of(activationDateTime, currentOffsetForActivation));

        keyClient.updateKeyProperties(keyProperties);

        KeyVaultKey keyVaultKey = keyClient.getKey(data.getName());
        return KeyDTO.getKeyModel(keyVaultKey, data.getKeyVaultName());
    }

    @Override
    public boolean deleteKey(String name, String keyVaultName) {
        boolean isDeleted;
        try {
            KeyClient keyClient = new KeyClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
            DeletedKey deletedKey = keyClient.beginDeleteKey(name).poll().getValue();
            isDeleted = deletedKey != null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            isDeleted = false;
        }
 
        return isDeleted;
    }
}
