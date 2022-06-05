package com.luis.projectlocalstack.service.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class )
public class BaseIntegrationTest {

    private static final DockerImageName DEFAULT_IMAGE_NAME =
            DockerImageName.parse("localstack/localstack");

    private static LocalStackContainer localStackContainer =
            new LocalStackContainer(DEFAULT_IMAGE_NAME).withServices(LocalStackContainer.Service.S3);

    static{
        localStackContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            var values = TestPropertyValues.of("s3.endpointURI=" +
                    localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3)
                            .getServiceEndpoint());
            values.applyTo(applicationContext);
        }
    }
}
