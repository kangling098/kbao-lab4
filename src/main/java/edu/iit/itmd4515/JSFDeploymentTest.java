package edu.iit.itmd4515;

import org.glassfish.embeddable.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple test to verify WAR file can be deployed and JSF works
 */
public class JSFDeploymentTest {
    
    private static final Logger LOG = Logger.getLogger(JSFDeploymentTest.class.getName());
    
    public static void main(String[] args) {
        LOG.info("=== Lab7 JSF Deployment Test ===");
        
        GlassFish glassfish = null;
        
        try {
            // Create GlassFish instance
            GlassFishProperties gfProps = new GlassFishProperties();
            gfProps.setPort("http-listener", 8080);
            
            LOG.info("Creating GlassFish instance...");
            glassfish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
            
            LOG.info("Starting GlassFish...");
            glassfish.start();
            
            // Deploy WAR file
            LOG.info("Deploying WAR file...");
            String warPath = "target/itmd4515-fp-1.0-SNAPSHOT.war";
            File warFile = new File(warPath);
            
            if (!warFile.exists()) {
                LOG.severe("WAR file not found: " + warFile.getAbsolutePath());
                return;
            }
            
            Deployer deployer = glassfish.getDeployer();
            String appName = deployer.deploy(warFile);
            
            LOG.info("✓ WAR file deployed successfully as: " + appName);
            LOG.info("✓ JSF application should be available at: http://localhost:8080/itmd4515-fp-1.0-SNAPSHOT/");
            
            // Keep server running for a short time
            LOG.info("Server will run for 30 seconds...");
            Thread.sleep(30000);
            
            // Undeploy
            LOG.info("Undeploying application...");
            deployer.undeploy(appName);
            
            LOG.info("✓ Application undeployed successfully");
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Deployment test failed", e);
        } finally {
            if (glassfish != null) {
                try {
                    LOG.info("Stopping GlassFish...");
                    glassfish.stop();
                    glassfish.dispose();
                    LOG.info("✓ GlassFish stopped successfully");
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Error stopping GlassFish", e);
                }
            }
        }
        
        LOG.info("=== JSF Deployment Test Complete ===");
    }
}