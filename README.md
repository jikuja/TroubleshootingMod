# Troubleshooting Mod

Simple mod for Minecraft troubleshooting

## Default settings

### HTTP server
 * Address http://localhost:6081 will serve page which has thread states and memory information

### Keyboard listener
 * Press j and h simultaneously tro trigger thread dump generation
 * Should work as soon as Display is opened and is being updated

## Optional features
### JMX agent
 * JMX agent can be enabled automatically with mod settings

### Logging via Forge/FML logger
 * logs JVM thread states and memory usage once per 60 seconds
 * time interval configurable
 * mimics `jstack -l` and `kill -3 <pid of jvm>` but does not require JDK installation or POSIX signals

# Other methods to get thread dump from JVM
## Install JDK and use tools shipped with it.
   * jconsole
   * jvisualvm
   * jps/stack

Thread dump is shown in the tool.

## If running OS with POSIX signals
 * Use `kill -3 $(pidof java)` to trigger thread dump generation
 * JVM process outputs thread dump into STDOUT. MC/forge logging system won't capture it, you need find thread dump from (log of the) STDOUT

## Use CTRL + break key combination
 * CTRL + break can be used to trigger thread dump generation if JVM is run in the console, e.g. server instance
 * JVM process outputs thread dump into STDOUT. MC/forge logging system won't capture it, you need find thread dump from (log of the) STDOUT