package io.github.jikuja.TroubleshootingMod;

import java.lang.management.*;
import java.util.List;

public class Utils {
    public static String beansToString(ThreadMXBean threadMXBean, MemoryMXBean memoryMXBean, List<MemoryPoolMXBean> memoryPoolMXBeans) {
        StringBuilder sb = new StringBuilder();

        sb.append("============== Threads ==============\n");
        for (ThreadInfo ti: threadMXBean.dumpAllThreads(true, true)) {
            sb.append(toString(ti));
        }

        /*
        sb.append("============== Threads ==============\n");
        for (ThreadInfo ti: threadMXBean.dumpAllThreads(true, true)) {
            sb.append(ti.toString());
        }
        */

        sb.append("============== Memory ==============\n");
        sb.append(String.format("HeapMemoryUsage: %s\n", memoryMXBean.getHeapMemoryUsage().toString()));
        sb.append(String.format("NonHeapMemoryUsage: %s\n", memoryMXBean.getNonHeapMemoryUsage().toString()));
        for (MemoryPoolMXBean p: memoryPoolMXBeans) {
            sb.append(String.format("%s: %s\n", p.getName(), p.getUsage().toString()));
        }

       return sb.toString();
    }

    private static int MAX_FRAMES=99;

    // micics ThreadImpl.toString:
    // * MAX_FRAMES changed
    public static String toString(ThreadInfo ti) {
        StringBuilder sb = new StringBuilder("\"" + ti.getThreadName() + "\"" +
                " Id=" + ti.getThreadId() + " " +
                ti.getThreadState());
        if (ti.getLockName() != null) {
            sb.append(" on " + ti.getLockName());
        }
        if (ti.getLockOwnerName() != null) {
            sb.append(" owned by \"" + ti.getLockOwnerName() +
                    "\" Id=" + ti.getLockOwnerId());
        }
        if (ti.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (ti.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        for (; i < ti.getStackTrace().length && i < MAX_FRAMES; i++) {
            StackTraceElement ste = ti.getStackTrace()[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            if (i == 0 && ti.getLockInfo() != null) {
                Thread.State ts = ti.getThreadState();
                switch (ts) {
                case BLOCKED:
                    sb.append("\t-  blocked on " + ti.getLockInfo());
                    sb.append('\n');
                    break;
                case WAITING:
                    sb.append("\t-  waiting on " + ti.getLockInfo());
                    sb.append('\n');
                    break;
                case TIMED_WAITING:
                    sb.append("\t-  waiting on " + ti.getLockInfo());
                    sb.append('\n');
                    break;
                default:
                }
            }

            for (MonitorInfo mi : ti.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi);
                    sb.append('\n');
                }
            }
        }
        if (i < ti.getStackTrace().length) {
            sb.append("\t...");
            sb.append('\n');
        }

        // this is not compatible with other tool
        // check jstack etc for proper example
        LockInfo[] locks = ti.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = " + locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- " + li);
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }
}
