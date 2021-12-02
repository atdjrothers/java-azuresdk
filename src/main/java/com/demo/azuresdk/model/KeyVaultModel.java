package com.demo.azuresdk.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class KeyVaultModel {

    @NotNull
    private String name;

    @NotNull
    private String resourceGroupName;

    private String id;
    private String vaultUri;

    public KeyVaultModel(@NotNull String name, @NotNull String resourceGroupName) {
        this.name = name;
        this.resourceGroupName = resourceGroupName;
    }
}
