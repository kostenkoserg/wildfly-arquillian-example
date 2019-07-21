package com.fasto.zootester.common;

import com.fasto.zootester.api.TestAPI;
import java.io.File;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author kostenko
 */
public class BaseIT {

    @ArquillianResource
    protected InitialContext initialContext;

    @Deployment
    public static EnterpriseArchive createAthenaStuffBaseDeployment() {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "zoo-tester.ear");
        ear.addAsModules(
                ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../zoo-tester/build/libs/zoo-tester-0.1.jar"))
                        .addAsResource("META-INF/persistence_H2.xml", "META-INF/persistence.xml")
                        .addClass(BaseIT.class)
                        .addPackage("com.fasto.zootester")
        )
                .addAsLibraries(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../commons/build/libs/commons-0.1.jar")))
                .addAsLibraries(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../datamanager/datamanager-api/build/libs/datamanager-api-0.1.jar")))
                ;
        return ear;
    }

    @Deployment(name = "quotes-provider-core", testable = false)
    public static EnterpriseArchive quotesProviderDeploy() {
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("../quotes-provider/quotes-provider-core/build/libs/quotes-provider-core-0.1.ear"));
    }
    
    @Deployment(name = "mock-traider", testable = false)
    public static WebArchive mockTraiderDeploy() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("../mockups/mock-tradier/build/libs/mock-tradier-0.1.war"));
    }

    @Deployment(name = "datamanager-core", testable = false)
    public static EnterpriseArchive datamanagerDeploy() {
        EnterpriseArchive ear = ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("../datamanager/datamanager-core/build/libs/datamanager-core-0.1.ear"));
        ear.getAsType(JavaArchive.class, "/datamanager-core-0.1.jar")
                .addAsResource("META-INF/persistence_H2.xml", "META-INF/persistence.xml");
        return ear;
    }

    @Deployment(name = "wallet-core", testable = false)
    public static EnterpriseArchive walletDeploy() {
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("../wallet/wallet-core/build/libs/wallet-core-0.1.ear"));
    }
    
    @Deployment(name = "tournament-core", testable = false)
    public static EnterpriseArchive tournamentDeploy() {
        return ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File("../tournament/tournament-core/build/libs/tournament-core-0.1.ear"));
    }

    @Deployment(name = "player-endpoint", testable = false)
    public static WebArchive playerEndpointDeploy() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("../player-endpoint/build/libs/player-endpoint-0.1.war"));
    }

    @Deployment(name = "admin-endpoint", testable = false)
    public static WebArchive adminEndpointDeploy() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("../admin-endpoint/build/libs/admin-endpoint-0.1.war"));
    }

    protected TestAPI testAPI() throws NamingException {
        return (TestAPI) initialContext.lookup("java:global/zoo-tester/zoo-tester-0.1/TestAPI");
    }
    
    protected void log(String message) {
        System.out.println("*** [ZOO-TESTER] *** " + message);
    }

}
