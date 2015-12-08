package io.github.jikuja.TroubleshootingMod;

import cpw.mods.fml.common.FMLLog;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

public class Agent {
    public void start(int port) {
        FMLLog.bigWarning("Unencrypted and unauthentication JMX agent port will be opened!");
        startAgent(port);
    }

    /**
     * Starts JMX agent. Encryption and authentication disabled. Don't blame me!
     */
    private void startAgent( int port ) {
        // from oracle documentation: http://docs.oracle.com/javase/7/docs/technotes/guides/management/agent.html#gdfvq
        try {
            // Ensure cryptographically strong random number generator used
            // to choose the object number - see java.rmi.server.ObjID
            //
            System.setProperty("java.rmi.server.randomIDs", "true");

            // Start an RMI registry on port 3000.
            //
            System.out.println("Create RMI registry on port 3000");
            LocateRegistry.createRegistry(port);

            // Retrieve the PlatformMBeanServer.
            //
            System.out.println("Get the platform's MBean server");
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            // Environment map.
            //
            System.out.println("Initialize the environment map");
            HashMap<String,Object> env = new HashMap<String,Object>();

            // Provide SSL-based RMI socket factories.
            //
            // The protocol and cipher suites to be enabled will be the ones
            // defined by the default JSSE implementation and only server
            // authentication will be required.
            //
            /* // change: do not use SSL at all.
            SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
            SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
            env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
            env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
            */
            // Provide the password file used by the connector server to
            // perform user authentication. The password file is a properties
            // based text file specifying username/password pairs.
            //
            /////env.put("jmx.remote.x.password.file", "password.properties");

            // Provide the access level file used by the connector server to
            // perform user authorization. The access level file is a properties
            // based text file specifying username/access level pairs where
            // access level is either "readonly" or "readwrite" access to the
            // MBeanServer operations.
            //
            /////env.put("jmx.remote.x.access.file", "access.properties");

            // Create an RMI connector server.
            //
            // As specified in the JMXServiceURL the RMIServer stub will be
            // registered in the RMI registry running in the local host on
            // port 3000 with the name "jmxrmi". This is the same name the
            // out-of-the-box management agent uses to register the RMIServer
            // stub too.
            //
            System.out.println("Create an RMI connector server");
            // service:jmx:rmi://<TARGET_MACHINE>:<JMX_RMI_SERVER_PORT>/jndi/rmi://<TARGET_MACHINE>:<RMI_REGISTRY_PORT>/jmxrmi
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);

            // Start the RMI connector server.
            //
            System.out.println("Start the RMI connector server");
            cs.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
