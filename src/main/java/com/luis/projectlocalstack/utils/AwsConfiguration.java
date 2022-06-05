package com.luis.projectlocalstack.utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AwsConfiguration {

    @Bean
//    @Profile({"local", "default"})
    public AmazonS3 localS3(@Value("${localstack.s3.endpoint}") String url) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpoint(url))
                .withCredentials(provider())
                .withPathStyleAccessEnabled(true)
                .build();
    }

    @Bean
    public AmazonDynamoDB localDynamoDB(@Value("${localstack.dynamoDB.endpoint}") String url) {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpoint(url))
                .withCredentials(provider())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(AmazonS3.class)
    public AmazonS3 s3() {
        return AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true).build();
    }

    @Bean
    @ConditionalOnMissingBean(AmazonDynamoDB.class)
    public AmazonDynamoDB dynamoDB(){
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    private AWSCredentialsProvider provider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials("none", "none"));
    }

    private EndpointConfiguration endpoint(String url) {
        return new EndpointConfiguration(url, "sa-east-1");
    }
}
