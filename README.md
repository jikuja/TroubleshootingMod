# Troubleshooting Mod

Simple mod for Minecraft troubleshooting

## Default settings
### Logging via Forge/FML
 * logs JVM thread states and memory usage once per 60 seconds
 * time interval configurable
 * mimics `jstack -l` and `kill -3 <pid of jvm>` but does not require JDK installation or POSIX signals

### HTTP server
 * Address http://localhost:6081 will serve page which has thread states and memory information

## Optional features
 * JMX agent can be enabled by mod setting