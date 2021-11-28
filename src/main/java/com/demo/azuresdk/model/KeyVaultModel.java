package com.demo.azuresdk.model;


import com.azure.resourcemanager.keyvault.models.AccessPolicy;
import com.azure.resourcemanager.keyvault.models.Keys;
import com.azure.resourcemanager.keyvault.models.Secrets;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class KeyVaultModel {

    @NotNull
    private String name;

    @NotNull
    private String resourceGroupName;

    private String id;
    private String vaultUri;

//    private List<AccessPolicy> accessPolicies;
//    private Keys keys;
//    private List<ClientSecretModel> secrets;

    public KeyVaultModel(@NotNull String name, @NotNull String resourceGroupName) {
        this.name = name;
        this.resourceGroupName = resourceGroupName;
    }
}
