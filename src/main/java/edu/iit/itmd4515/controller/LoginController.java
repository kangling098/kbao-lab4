package edu.iit.itmd4515.controller;

import edu.iit.itmd4515.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JSF Backing Bean for user authentication.
 * Handles login and logout functionality through JSF pages.
 * 
 * @author Lab 8 - Security Implementation
 */
@Named("loginController")
@RequestScoped
public class LoginController {
    
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    
    @EJB
    private UserService userService;
    
    @jakarta.inject.Inject
    private SecurityContext securityContext;
    
    @jakarta.inject.Inject
    private FacesContext facesContext;
    
    @jakarta.inject.Inject
    private ExternalContext externalContext;
    
    // Form fields for login
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(max = 50, message = "Password must not exceed 50 characters")
    private String password;
    
    private String errorMessage;
    
    /**
     * Initialize the controller.
     */
    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "Initializing LoginController");
    }
    
    /**
     * Action method to authenticate user.
     * @return navigation outcome
     */
    public String login() {
        LOG.log(Level.INFO, "Attempting login for user: {0}", username);
        
        try {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            
            // Create credential
            UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
            
            // Authenticate using Jakarta EE Security
            AuthenticationStatus status = securityContext.authenticate(
                request,
                response,
                AuthenticationParameters.withParams()
                    .credential(credential)
                    .newAuthentication(true)
            );
            
            LOG.log(Level.INFO, "Authentication status: {0}", status);
            
            switch (status) {
                case SUCCESS:
                    LOG.log(Level.INFO, "User {0} authenticated successfully", username);
                    addInfoMessage("Login successful. Welcome!");
                    
                    // Redirect based on user role
                    if (securityContext.isCallerInRole("ADMIN")) {
                        return "/admin/dashboard?faces-redirect=true";
                    } else if (securityContext.isCallerInRole("LIBRARIAN")) {
                        return "/librarian/dashboard?faces-redirect=true";
                    } else if (securityContext.isCallerInRole("USER")) {
                        return "/user/dashboard?faces-redirect=true";
                    } else {
                        return "/index?faces-redirect=true";
                    }
                    
                case SEND_CONTINUE:
                    LOG.log(Level.INFO, "Authentication requires additional steps");
                    facesContext.responseComplete();
                    return null;
                    
                case SEND_FAILURE:
                    LOG.log(Level.WARNING, "Authentication failed for user: {0}", username);
                    errorMessage = "Invalid username or password";
                    addErrorMessage(errorMessage);
                    return null;
                    
                case NOT_DONE:
                    LOG.log(Level.WARNING, "Authentication not completed");
                    errorMessage = "Authentication process not completed";
                    addErrorMessage(errorMessage);
                    return null;
                    
                default:
                    LOG.log(Level.WARNING, "Unexpected authentication status: {0}", status);
                    errorMessage = "Unexpected error during authentication";
                    addErrorMessage(errorMessage);
                    return null;
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error during authentication", e);
            errorMessage = "An error occurred during login. Please try again.";
            addErrorMessage(errorMessage);
            return null;
        }
    }
    
    /**
     * Action method to logout user.
     * @return navigation outcome
     */
    public String logout() {
        LOG.log(Level.INFO, "Logging out user");
        
        try {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            request.logout();
            
            // Invalidate session
            request.getSession().invalidate();
            
            addInfoMessage("You have been logged out successfully.");
            
            // Redirect to login page
            return "/login?faces-redirect=true";
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error during logout", e);
            addErrorMessage("An error occurred during logout.");
            return null;
        }
    }
    
    /**
     * Check if user is authenticated.
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return securityContext.getCallerPrincipal() != null;
    }
    
    /**
     * Get the current user's name.
     * @return username or null if not authenticated
     */
    public String getCurrentUsername() {
        if (isAuthenticated()) {
            return securityContext.getCallerPrincipal().getName();
        }
        return null;
    }
    
    /**
     * Check if user has specific role.
     * @param role the role to check
     * @return true if user has role, false otherwise
     */
    public boolean hasRole(String role) {
        return securityContext.isCallerInRole(role);
    }
    
    /**
     * Add info message to faces context.
     * @param message the message to add
     */
    private void addInfoMessage(String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }
    
    /**
     * Add error message to faces context.
     * @param message the message to add
     */
    private void addErrorMessage(String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
    
    /**
     * Reset form fields.
     */
    public void reset() {
        LOG.log(Level.INFO, "Resetting login form fields");
        username = null;
        password = null;
        errorMessage = null;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}