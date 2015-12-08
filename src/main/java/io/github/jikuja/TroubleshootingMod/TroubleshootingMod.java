package io.github.jikuja.TroubleshootingMod;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = TroubleshootingMod.MODID, version = TroubleshootingMod.VERSION, dependencies = "before:*")
public class TroubleshootingMod
{
    public static final String MODID = "troubleshootingmod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLLog.bigWarning(
                "You have installed TroubleshootingMod (for debugging purposes). Check configuration to enable or disable features ");
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        boolean profiler = config.get(config.CATEGORY_GENERAL, "Enable profile", true).getBoolean(true);
        int sleepTime = config.get(config.CATEGORY_GENERAL, "Sleep between profiler runs", 60).getInt(60);
        boolean jmx = config.get(config.CATEGORY_GENERAL, "Enable JMX agent", false).getBoolean(false);
        int jmxport = config.get(config.CATEGORY_GENERAL, "JMX port", 6060).getInt(6060);
        boolean http = config.get(config.CATEGORY_GENERAL, "Enable HTTP interface", true).getBoolean(true);
        String httpbind = config.get(config.CATEGORY_GENERAL, "Enable bind address", "localhost").getString();
        int httpport = config.get(config.CATEGORY_GENERAL, "HTTP port", 6081).getInt(6081);

        config.save();

        if (profiler) {
            new DumperThread(sleepTime).start();
        }

        if (jmx) {
            new Agent().start(jmxport);
        }

        if (http) {
            new Http(httpport, httpbind).start();
        }
    }
}
