package io.github.jikuja.TroubleshootingMod;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.*;
import java.util.Set;

public class Main {
    JMXConnector connector;
    MBeanServerConnection mbsc;
    ThreadMXBean threadBean;
    MemoryMXBean memoryMXBean;
    MemoryPoolMXBean memoryPoolMXBeans;

    public static void main (String[] args) throws Exception{
        final int port = 6060;

        Main m  = new Main();
        m.connect(port);
        m.execute();
        m.disconnect();
    }

    private void connect(int port) throws Exception{
        JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
        connector = JMXConnectorFactory.connect(serviceURL);
        mbsc = connector.getMBeanServerConnection();
        try {
            threadBean = getThreadMXBean();
            //TODO: other beans. Write generic bean getter
        } catch (MalformedObjectNameException e) {
            throw new IOException("Bad MX bean name", e);
        }
    }

    private void disconnect() throws IOException{
        connector.close();
    }

    private void execute() throws Exception{
        ThreadInfo[] threadDumps = threadBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadDumps) {
            System.out.println(info.toString());
        }
    }

    private ThreadMXBean getThreadMXBean() throws IOException, MalformedObjectNameException {
        ObjectName objName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
        Set<ObjectName> mbeans = mbsc.queryNames(objName, null);
        for (ObjectName name : mbeans) {
            return ManagementFactory.newPlatformMXBeanProxy(
                    mbsc, name.toString(), ThreadMXBean.class);
        }
        throw new IOException("No thread MX bean found");
    }
}
