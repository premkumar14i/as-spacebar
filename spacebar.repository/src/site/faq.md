## Installation & Configuration

1. **Exception in thread "main" java.lang.NoClassDefFoundError**

    The AS_HOME environment variable might not be set properly

2. **Error loading as-common dynamic loadable library**

    Check that the environment variable for your platform is correctly set:
    
    * Windows: `PATH`
    * Linux: `LD_LIBRARY_PATH`
    * Mac OSX: `DYLD_LIBRARY_PATH`
    * AIX: `LIBPATH`
    * UX: `SHLIB_PATH`

    Also check that you have the appropriate architecture installed (e.g. 32 vs 64-bit).

3. **How do I set the AS_HOME and DYLD_LIBRARY_PATH environment variables for GUI applications on Mac OSX?**

    Edit `/etc/launchd.conf` (for example `sudo nano /etc/launchd.conf`) and add the following lines:
    
    ~~~bash
    setenv AS_HOME /opt/tibco/as/2.1
    setenv DYLD_LIBRARY_PATH /opt/tibco/as/2.1/lib
    ~~~
    Either restart or run `launchctl < /etc/launchd.conf; sudo launchctl < /etc/launchd.conf`

## Metaspace Connection

1. **SYS_ERROR (multicast_error - (8) grp_iface not a valid multicast interface)**

    Specify a discovery URL e.g. `tcp`
