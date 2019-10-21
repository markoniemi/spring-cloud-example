package example;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConfigIT extends AbstractIntegrationTestBase {
    private String url = "http://localhost:8083";
    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void localConfig() throws ClientProtocolException, IOException {
        Assume.assumeFalse(isCloudConfigEnabled());
        String body = get(url + "/actuator/env", null);
        Assert.assertTrue(body.contains("runtime.environment"));
        Assert.assertTrue(body.contains("local"));
    }
    @Test
    public void remoteConfig() throws ClientProtocolException, IOException {
        Assume.assumeTrue(isCloudConfigEnabled());
        String body = get(url + "/actuator/env", null);
        Assert.assertTrue(body.contains("runtime.environment"));
        Assert.assertTrue(body.contains("remote"));
    }

    private String getUrl() {
        if (isCloudConfigEnabled()) {
            List<ServiceInstance> instances = discoveryClient.getInstances("user-repository");
            if (CollectionUtils.isNotEmpty(instances)) {
                log.info(instances.get(0).getUri());
                return instances.get(0).getUri().toString();
            }
        }
        return null;
    }
}