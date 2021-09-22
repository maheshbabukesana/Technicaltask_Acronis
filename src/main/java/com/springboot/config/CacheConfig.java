package com.springboot.config;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	@Bean(name = "ehCacheManager")
	public net.sf.ehcache.CacheManager ehCacheManager() {
		CacheConfiguration currencyCache = new CacheConfiguration();
		currencyCache.setName("currency-cache");
		currencyCache.setMemoryStoreEvictionPolicy("LRU");
		currencyCache.setMaxEntriesLocalHeap(1000);
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.addCache(currencyCache);
		return net.sf.ehcache.CacheManager.newInstance(config);
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
}