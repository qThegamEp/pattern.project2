package com.qthegamep.pattern.project2.cache;

import com.qthegamep.pattern.project2.exception.compile.HashServiceException;
import com.qthegamep.pattern.project2.repository.redis.RedisRepository;
import com.qthegamep.pattern.project2.service.ConverterService;
import com.qthegamep.pattern.project2.service.HashService;
import com.qthegamep.pattern.project2.service.KeyBuilderService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
public class CacheAspect {

    private static final Logger LOG = LoggerFactory.getLogger(CacheAspect.class);

    private final boolean enableCacheAspect = Boolean.parseBoolean(System.getProperty("aop.enable.cache.aspect"));

    @Inject
    private HashService hashService;
    @Inject
    private ConverterService converterService;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private KeyBuilderService keyBuilderService;

    public CacheAspect() {
        LOG.warn("Enable cache aspect: {}", enableCacheAspect);
    }

    @Pointcut("@annotation(cacheable)")
    void cacheableAnnotation(Cacheable cacheable) {
    }

    @Around(value = "cacheableAnnotation(cacheable)",
            argNames = "thisJoinPoint,cacheable")
    public Object cache(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) {
        try {
            if (enableCacheAspect) {
                LOG.debug("Cache method {} with arguments {} by {} hash algorithm", thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), cacheable.hashAlgorithm());
                String key = getKey(thisJoinPoint, cacheable);
                Optional<String> resultFromCache = redisRepository.read(key);
                LOG.debug("Has result: {} by key: {}", resultFromCache.isPresent(), key);
                if (resultFromCache.isPresent()) {
                    String result = resultFromCache.get();
                    LOG.info("Result from cache: {} by key: {}", result, key);
                    Class<?> returnType = getReturnType(thisJoinPoint);
                    return converterService.fromJson(result, returnType);
                } else {
                    Object result = thisJoinPoint.proceed();
                    LOG.debug("Result from proceed: {}", result);
                    String jsonResult = converterService.toJson(result);
                    redisRepository.save(key, jsonResult);
                    return result;
                }
            } else {
                return thisJoinPoint.proceed();
            }
        } catch (Throwable e) {
            LOG.error("ERROR", e);
            return e;
        }
    }

    private String getKey(ProceedingJoinPoint thisJoinPoint, Cacheable cacheable) throws HashServiceException {
        String key = keyBuilderService.buildCacheKey(thisJoinPoint, cacheable.keyAlgorithm());
        String encodedKey = hashService.encode(key, cacheable.hashAlgorithm());
        LOG.debug("Encoded key: {}", encodedKey);
        return encodedKey;
    }

    private Class<?> getReturnType(ProceedingJoinPoint thisJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getReturnType();
    }
}
