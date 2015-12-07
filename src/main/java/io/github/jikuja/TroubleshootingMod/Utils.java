package io.github.jikuja.TroubleshootingMod;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class Utils {
    public static String beansToString(ThreadMXBean threadMXBean, MemoryMXBean memoryMXBean, List<MemoryPoolMXBean> memoryPoolMXBeans) {
        StringBuilder sb = new StringBuilder();

        sb.append("============== Threads ==============\n");
        for (ThreadInfo ti: threadMXBean.dumpAllThreads(true, true)) {
            sb.append(ti.toString());
        }

        sb.append("============== Memory ==============\n");
        sb.append(String.format("HeapMemoryUsage: %s\n", memoryMXBean.getHeapMemoryUsage().toString()));
        sb.append(String.format("NonHeapMemoryUsage: %s\n", memoryMXBean.getNonHeapMemoryUsage().toString()));
        for (MemoryPoolMXBean p: memoryPoolMXBeans) {
            sb.append(String.format("%s: %s\n", p.getName(), p.getUsage().toString()));
        }

       return sb.toString();
    }
}
