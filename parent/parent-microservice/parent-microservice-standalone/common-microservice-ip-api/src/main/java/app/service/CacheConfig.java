//package app.service;
//
//import org.springframework.context.annotation.Bean;
//
//import com.github.benmanes.caffeine.cache.CacheLoader;
//
//public class CacheConfig {
//
//	@Bean
//	public CacheLoader<Object, Object> cacheLoader() {
//
//	    CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
//
//	        @Override
//	        public Object load(Object key) throws Exception {
//	            return null;
//	        }
//
//	        // 重写这个方法将oldValue值返回回去，进而刷新缓存
//	        @Override
//	        public Object reload(Object key, Object oldValue) throws Exception {
//	            return oldValue;
//	        }
//	    };
//
//	    return cacheLoader;
//	}
//}