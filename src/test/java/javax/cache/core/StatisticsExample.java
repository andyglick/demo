package javax.cache.core;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.spi.CachingProvider;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.lang.management.ManagementFactory;
import java.util.Set;

import static javax.cache.expiry.Duration.ONE_HOUR;

public class StatisticsExample {

  public void accessStatistics() throws MalformedObjectNameException,
      AttributeNotFoundException, MBeanException, ReflectionException,
      InstanceNotFoundException {

    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();

    MutableConfiguration<String, Integer> config =
        new MutableConfiguration<String, Integer>();
    config.setTypes(String.class, Integer.class)
        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(ONE_HOUR))
        .setStatisticsEnabled(true);

    Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);

    Set<ObjectName> registeredObjectNames = null;
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    ObjectName objectName = new ObjectName("javax.cache:type=CacheStatistics"
        + ",CacheManager=" + (cache.getCacheManager().getURI().toString())
        + ",Cache=" + cache.getName());
    System.out.println(mBeanServer.getAttribute(objectName,
        "CacheHitPercentage"));
  }
}
