package com.demo.azuresdk.service.impl;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.demo.azuresdk.service.HeroesService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.minidev.json.JSONArray;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class HeroesServiceImpl implements HeroesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSecretServiceImpl.class);
    private static final String VAULT_URL_FORMAT = "https://%s.vault.azure.net";
    private static final String CONNECTION_URI_MONGODB = "mongodb-connection-uri";
    private static final String KEYVAULT_NAME = "JavaKeyVault1";

    @Override
    public String getHeroes() {

        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl(String.format(VAULT_URL_FORMAT,KEYVAULT_NAME))
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        KeyVaultSecret mongoDbSecret = secretClient.getSecret(CONNECTION_URI_MONGODB);
        String connectionUri = mongoDbSecret.getValue();

        ConnectionString connectionString = new ConnectionString(connectionUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("dota2");
        database.getCollection("heroes").find().forEach((Consumer<? super Document>) d -> System.out.println(d.toString()));

        JSONArray jsonArray = new JSONArray();
        database.getCollection("heroes").find().forEach((Consumer<? super Document>) d -> {
            LOGGER.debug(d.toJson());
            jsonArray.add(d.toJson());
        });

        return jsonArray.toJSONString();
    }

    public static void main(String[] args){
        getDota2Heroes();
    }

    private static void getDota2Heroes(){
        JSONArray jsonArray = new JSONArray();
        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:admin@covidcluster.bojkh.mongodb.net/javapractice?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("dota2");
        database.getCollection("heroes").find().forEach((Consumer<? super Document>) d -> {
            System.out.println(d.toJson());
            jsonArray.add(d.toJson());
        });

        System.out.println(jsonArray.toJSONString());
    }
}
