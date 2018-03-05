package org.lawlsec.geocraft;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sun.util.logging.PlatformLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

public class GeoCraft extends JavaPlugin {
    private static GeoCraft Instance;
    private static LookupService service;

    @Override
    public void onEnable() {
        Instance = this;

        saveDefaultConfig();

        try {
            service = new LookupService(
                getDataFolder() + "/GeoLiteCity.dat",
                LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE
            );

        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Failed to find location database: " + getDataFolder() + "/GeoLiteCity.dat");

            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() { }

    private static Location getLocation(InetSocketAddress address) {
        return service.getLocation(address.getAddress().toString().split("/")[1]);
    }

    /**
     * Gets the client time zone
     * @return client time zone
     */
    public static String getTimezone(InetSocketAddress address) {
        return timeZone.timeZoneByCountryAndRegion(
            getLocation(address).countryCode,
            getLocation(address).region
        );
    }

    /**
     * Gets the client country
     * @return client country
     */
    public static String getCountryName(InetSocketAddress address) {
        return getLocation(address).countryName;
    }

    /**
     * Gets the client region
     * @return client region
     */
    public static String getRegionName(InetSocketAddress address) {
        return getLocation(address).region;
    }

    /**
     * Gets the client city
     * @return client city
     */
    public static String getCityName(InetSocketAddress address) {
        return getLocation(address).city;
    }

    public static GeoCraft getInstance() {
        return Instance;
    }
}
