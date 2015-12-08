package io.github.jikuja.TroubleshootingMod;

import cpw.mods.fml.common.FMLLog;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class DumperThread extends Thread {
    private long sleepSeconds;

    public DumperThread (int sleep) {
        sleepSeconds = sleep;
        setName("TroubleshootingMod: DumperThread");
    }

    @Override
    public void run () {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        while (true) {
            FMLLog.info("\n%s", Utils.beansToString(threadMXBean, memoryMXBean, memoryPoolMXBeans));

            try {
                Thread.sleep(sleepSeconds * 1000);
            } catch (InterruptedException e) {
                FMLLog.warning(e.toString());
            }
        }
    }
}
