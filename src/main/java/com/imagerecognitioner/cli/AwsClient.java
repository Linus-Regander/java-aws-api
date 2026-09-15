package com.imagerecognitioner.cli;

import com.imagerecognitioner.model.ImageMetadata;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;

/**
 * AWS client configuration for DynamoDB integration.
 */
@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsClient {
    /**
     * Creates a DynamoDbClient bean configured with the specified AWS region and default credentials provider.
     * @param awsProperties
     * @return
     */
    @Bean
    public DynamoDbClient dynamoDbClient(AwsProperties awsProperties, @Value("${app.aws.dynamo-db.endpoint:}") String localEndpoint) {
        var builder = DynamoDbClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (!localEndpoint.isBlank()) {
            builder.endpointOverride(URI.create(localEndpoint));
        }

        return builder.build();
    }

    /**
     * Creates a DynamoDbEnhancedClient bean using the provided DynamoDbClient.
     * @param dynamoDbClient
     * @return
     */
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    /**
     * Creates a DynamoDbTable bean for the ImageMetadata table using the provided DynamoDbEnhancedClient and AWS properties.
     * @param enhancedClient
     * @param props
     * @return
     */
    @Bean
    public DynamoDbTable<ImageMetadata> imageMetadataTable(DynamoDbEnhancedClient enhancedClient, AwsProperties props) {
        return enhancedClient.table(props.getDynamoDB().getTableName(), TableSchema.fromBean(ImageMetadata.class));
    }
}
