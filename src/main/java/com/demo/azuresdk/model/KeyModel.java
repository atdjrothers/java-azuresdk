package com.demo.azuresdk.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class KeyModel {
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private String keyVaultName;

    private long activationDate;

    private long expiryDate;

    public KeyModel(String id, @NotNull String name, @NotNull String type, @NotNull String keyVaultName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.keyVaultName = keyVaultName;
    }

//    public KeyModel(String id, @NotNull String name, @NotNull String type, @NotNull String keyVaultName, long activationDate, long expiryDate) {
//        this.id = id;
//        this.name = name;
//        this.type = type;
//        this.keyVaultName = keyVaultName;
//        this.activationDate = activationDate;
//        this.expiryDate = expiryDate;
//    }
}
