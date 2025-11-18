package edu.iit.itmd4515.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * REST application configuration class.
 * This class defines the base URL for all REST endpoints.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // This class extends Application and is annotated with @ApplicationPath
    // to define the base URL for all REST endpoints in the application.
    // All REST endpoints will be accessible under the /api path.
}