package com.demo.azuresdk.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
public class ClientSecretModel {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String value;

    @NotNull
    private String keyVaultName;

    public ClientSecretModel (String id, @NotNull String name, @NotNull String value, @NotNull String keyVaultName) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.keyVaultName = keyVaultName;
    }
}
