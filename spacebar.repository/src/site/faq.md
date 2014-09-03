## Installation & Configuration

1. `Exception in thread "main" java.lang.NoClassDefFoundError`

    The `AS_HOME` environment variable might not be set properly

2. `Error loading as-common dynamic loadable library`

    Check that the dynamic library path for your platform includes the `lib` folder from your AS installation:
    
    + Windows: `PATH`

        e.g. `PATH="C:\tibco\as\2.1\lib";...`

    + Linux: `LD_LIBRARY_PATH`
    
        e.g. `LD_LIBRARY_PATH=/opt/tibco/as/2.1/lib:...`

    + Mac OSX: `DYLD_LIBRARY_PATH`
    
        e.g. `DYLD_LIBRARY_PATH=/opt/tibco/as/2.1/lib:...`

    + AIX: `LIBPATH`
    
        e.g. `LIBPATH=/opt/tibco/as/2.1/lib:...`

    + UX: `SHLIB_PATH`
    
        e.g. `SHLIB_PATH=/opt/tibco/as/2.1/lib:...`

    Also check that you have the appropriate architecture installed (e.g. 32 vs 64-bit).

3. Mac OSX: How do I set the `AS_HOME` and `DYLD_LIBRARY_PATH` environment variables for GUI applications?

    Edit `/etc/launchd.conf` (for example `sudo nano /etc/launchd.conf`) and add the following lines:
    
    `setenv AS_HOME /opt/tibco/as/2.1`
    
    `setenv DYLD_LIBRARY_PATH /opt/tibco/as/2.1/lib`

   Either restart or run `launchctl < /etc/launchd.conf; sudo launchctl < /etc/launchd.conf`

## Metaspace Connection

1. `SYS_ERROR (multicast_error - (8) grp_iface not a valid multicast interface)`

    Specify a discovery URL in the metaspace connection profile, e.g. `tcp`