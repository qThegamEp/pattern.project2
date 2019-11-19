package com.qthegamep.pattern.project2.repository;

import com.qthegamep.pattern.project2.exception.RedisRepositoryException;
import com.qthegamep.pattern.project2.model.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class RedisPoolRepositoryImpl implements RedisRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedisPoolRepositoryImpl.class);

    private final int defaultTtl = 1200;

    private JedisPool jedisPool;

    @Inject
    public RedisPoolRepositoryImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void save(String key, String value) throws RedisRepositoryException {
        save(key, value, defaultTtl, null);
    }

    @Override
    public void save(String key, String value, String requestId) throws RedisRepositoryException {
        save(key, value, defaultTtl, requestId);
    }

    @Override
    public void save(String key, String value, Integer ttl) throws RedisRepositoryException {
        save(key, value, ttl, null);
    }

    @Override
    public void save(String key, String value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Save -> Key: {} Value: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            jedis.expire(key, ttl);
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ERROR);
        }
    }

    @Override
    public void saveAll(String key, Map<String, String> value) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, null);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, String requestId) throws RedisRepositoryException {
        saveAll(key, value, defaultTtl, requestId);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, Integer ttl) throws RedisRepositoryException {
        saveAll(key, value, ttl, null);
    }

    @Override
    public void saveAll(String key, Map<String, String> value, Integer ttl, String requestId) throws RedisRepositoryException {
        LOG.debug("Save all -> Key: {} Values: {} TTL: {} RequestId: {}", key, value, ttl, requestId);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, value);
            jedis.expire(key, ttl);
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_SAVE_ALL_ERROR);
        }
    }

    @Override
    public Optional<String> read(String key) throws RedisRepositoryException {
        return read(key, null);
    }

    @Override
    public Optional<String> read(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Read -> Key: {} RequestId: {}", key, requestId);
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.get(key);
            LOG.debug("Read result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ERROR);
        }
    }

    @Override
    public Optional<Map<String, String>> readAll(String key) throws RedisRepositoryException {
        return readAll(key, null);
    }

    @Override
    public Optional<Map<String, String>> readAll(String key, String requestId) throws RedisRepositoryException {
        LOG.debug("Read all -> Key: {} RequestId: {}", key, requestId);
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> result = jedis.hgetAll(key);
            LOG.debug("Read all result: {} RequestId: {}", result, requestId);
            if (result == null || result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result);
            }
        } catch (Exception e) {
            throw new RedisRepositoryException(e, ErrorType.REDIS_READ_ALL_ERROR);
        }
    }
}