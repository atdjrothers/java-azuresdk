package com.demo.azuresdk.service.impl;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.keyvault.models.Vault;
import com.demo.azuresdk.model.KeyVaultModel;
import com.demo.azuresdk.model.KeyVaultModelDTO;
import com.demo.azuresdk.service.KeyVaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeyVaultServiceImpl implements KeyVaultService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyVaultServiceImpl.class);
    private final AzureResourceManager azureResourceManager;

    public KeyVaultServiceImpl() {
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        final TokenCredential credential = new DefaultAzureCredentialBuilder()
                .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                .build();

        azureResourceManager = AzureResourceManager
                .configure()
                .withLogLevel(HttpLogDetailLevel.BASIC)
                .authenticate(credential, profile)
                .withDefaultSubscription();
    }

    @Override
    public KeyVaultModel createVault(String name, String resourceGroupName) {
        LOGGER.debug(String.format("Creating a key vault...%s \n", name));
        KeyVaultModel keyVaultModel = null;

        try {
            Vault javaKeyVaultTest = azureResourceManager.vaults().getById(System.getenv("RESOURCE_ID_JAVAKEYVAULTTEST"));
            LOGGER.debug(String.format("javaKeyVaultTest...%s \n", javaKeyVaultTest.name()));
            Vault vault = azureResourceManager.vaults().define(name)
                    .withRegion(Region.ASIA_SOUTHEAST)
                    .withNewResourceGroup(resourceGroupName)
                    .withAccessPolicy(javaKeyVaultTest.accessPolicies().get(0))
                    .create();


//            String servicePrincipalId = System.getenv("AZURE_SERVICE_PRINCIPAL_ID");
//            LOGGER.debug(String.format("subscriptionId: %s \n", servicePrincipalId));
//            servicePrincipalId = "admin@MMI185.onmicrosoft.com";
//            vault = azureResourceManager.vaults().define(name)
//                    .withRegion(Region.ASIA_SOUTHEAST)
//                    .withNewResourceGroup(resourceGroupName)
//                    .defineAccessPolicy()
//                    .forServicePrincipal(servicePrincipalId)
//                    .allowKeyAllPermissions()
//                    .allowSecretAllPermissions()
//                    .allowCertificateAllPermissions()
//                    .attach()
//                    .create();

            if (vault != null) {
                LOGGER.debug(String.format("Created key vault...%s \n", vault.name()));
                keyVaultModel = KeyVaultModelDTO.getKeyVaultModel(vault);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return keyVaultModel;
    }

    @Override
    public KeyVaultModel getVault(String name, String resourceGroupName) {
        LOGGER.debug(String.format("Retrieve a key vault from resource group name...%s \n", resourceGroupName));
        KeyVaultModel keyVaultModel = null;

        try {
            Vault javaKeyVaultTest = azureResourceManager.vaults().getById(System.getenv("RESOURCE_ID_JAVAKEYVAULTTEST"));
            LOGGER.debug(String.format("javaKeyVaultTest...%s \n", javaKeyVaultTest.name()));
            Vault vault = azureResourceManager.vaults().listByResourceGroup(resourceGroupName).stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
            if (vault != null) {
                LOGGER.debug(String.format("Retrieve key vault...%s \n", vault.name()));
                keyVaultModel = KeyVaultModelDTO.getKeyVaultModel(vault);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return keyVaultModel;
    }

    @Override
    public List<KeyVaultModel> getVaults(String resourceGroupName) {
        List<KeyVaultModel> keyVaultModelList = new ArrayList<>();
        try {
            keyVaultModelList = azureResourceManager.vaults().listByResourceGroup(resourceGroupName).stream().map(KeyVaultModelDTO::getKeyVaultModel).collect(Collectors.toList());
            LOGGER.debug(String.format("Resource Name: %s has %s number of vaults \n", keyVaultModelList.size(), resourceGroupName));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return keyVaultModelList;
    }

    @Override
    public boolean deleteVault(String id, String resourceGroupName) {
        boolean isDeleted = false;
        try {
            azureResourceManager.vaults().deleteById(id);
            isDeleted = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return isDeleted;
    }
}
