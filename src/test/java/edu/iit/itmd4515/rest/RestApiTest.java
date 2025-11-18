package edu.iit.itmd4515.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * Simple test to verify REST API configuration.
 */
public class RestApiTest {
    
    @Test
    public void testRestApplicationConfig() {
        RestApplication app = new RestApplication();
        assertNotNull(app, "REST application should be instantiated");
    }
}