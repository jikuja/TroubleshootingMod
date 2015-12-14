package io.github.jikuja.TroubleshootingMod;

import cpw.mods.fml.common.FMLLog;
import org.lwjgl.input.Keyboard;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class KeyBoardPollerThread extends Thread {
    public KeyBoardPollerThread() {
        setName(TroubleshootingMod.MODID + ": KeyBoardPoller");
    }

    @Override
    public void run () {
        boolean canDump = true;
        while (true) {
            // this might cause some event to be lost from MC but
            // we need to poll keyboard!

            // If we do not poll Keyboard then as soon as main thread hangs
            // we are not getting new keyboard events
            Keyboard.poll();
            boolean keyHFound = Keyboard.isKeyDown(Keyboard.KEY_H);
            boolean keyJFound = Keyboard.isKeyDown(Keyboard.KEY_J);
            if (canDump &&keyHFound && keyJFound ){
                canDump = false;
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

                FMLLog.info("\n%s", Utils.beansToString(threadMXBean, memoryMXBean, memoryPoolMXBeans));
            } else if (!canDump && !keyJFound && !keyHFound) {
                canDump = true;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
