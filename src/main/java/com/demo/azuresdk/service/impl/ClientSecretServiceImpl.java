package com.demo.azuresdk.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.DeletedSecret;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;
import com.demo.azuresdk.model.ClientSecretModel;
import com.demo.azuresdk.service.ClientSecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientSecretServiceImpl implements ClientSecretService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSecretServiceImpl.class);
    private static final String VAULT_URL_FORMAT = "https://%s.vault.azure.net";

    @Override
    public ClientSecretModel createSecret(ClientSecretModel data) {
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl(String.format(VAULT_URL_FORMAT, data.getKeyVaultName()))
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        secretClient.setSecret(new KeyVaultSecret(data.getName(), data.getValue()));

        KeyVaultSecret keyVaultSecret = secretClient.getSecret(data.getName());
        LOGGER.debug(keyVaultSecret.getName() + " " + keyVaultSecret.getId());
        return new ClientSecretModel(keyVaultSecret.getId(), keyVaultSecret.getName(), keyVaultSecret.getValue(), data.getKeyVaultName());
    }

    @Override
    public ClientSecretModel getSecret(String name, String keyVaultName) {
        KeyVaultSecret keyVaultSecret = null;
        try {
            SecretClient secretClient = new SecretClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
            keyVaultSecret = secretClient.getSecret(name);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        if (keyVaultSecret != null) {
            return new ClientSecretModel(keyVaultSecret.getId(), keyVaultSecret.getName(), keyVaultSecret.getValue(), keyVaultName);
        }

        return null;
    }

    @Override
    public List<ClientSecretModel> getSecrets(String keyVaultName) {
        List<ClientSecretModel> secrets = new ArrayList<>();
        try {
            SecretClient secretClient = new SecretClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
            PagedIterable<SecretProperties> secretProperties = secretClient.listPropertiesOfSecrets();
            secretProperties.stream().forEach(s -> {
                KeyVaultSecret keyVaultSecret = secretClient.getSecret(s.getName());
                if (keyVaultSecret != null) {
                    secrets.add(new ClientSecretModel(keyVaultSecret.getId(), keyVaultSecret.getName(), keyVaultSecret.getValue(), keyVaultName));
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return secrets;
    }

    @Override
    public boolean deleteSecret(String name, String keyVaultName) {
        boolean isDeleted;
        try {
            SecretClient secretClient = new SecretClientBuilder()
                    .vaultUrl(String.format(VAULT_URL_FORMAT, keyVaultName))
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
            DeletedSecret deletedSecret = secretClient.beginDeleteSecret(name).poll().getValue();
            isDeleted = deletedSecret != null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            isDeleted = false;
        }
 
        return isDeleted;
    }
}
