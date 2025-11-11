package edu.iit.itmd4515.config;

import edu.iit.itmd4515.service.DatabaseSeedService;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Security configuration class for Jakarta EE security.
 * Defines authentication mechanisms, identity stores, and security roles.
 */
@CustomFormAuthenticationMechanismDefinition(
    loginToContinue = @LoginToContinue(
        loginPage = "/login.xhtml",
        errorPage = "/error.xhtml",
        useForwardToLogin = false
    )
)

@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:app/jdbc/itmd4515DS",
    callerQuery = "SELECT password FROM users WHERE username = ?",
    groupsQuery = "SELECT g.group_name FROM groups g, user_groups ug, users u WHERE u.username = ? AND u.id = ug.user_id AND g.id = ug.group_id",
    priority = 10
)

@DeclareRoles({"ADMIN", "LIBRARIAN", "USER"})
@Startup
@Singleton
public class SecurityConfig {
    
    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());
    
    @Inject
    private DatabaseSeedService databaseSeedService;
    
    public SecurityConfig() {
        LOG.info("SecurityConfig instantiated");
    }
    
    /**
     * Initialize security configuration.
     * This method is called when the application starts.
     */
    @jakarta.annotation.PostConstruct
    public void init() {
        LOG.info("Initializing security configuration...");
        
        // The DatabaseSeedService will handle creating default security data
        // including users, groups, and role mappings
        LOG.info("Security configuration initialized successfully");
    }
}