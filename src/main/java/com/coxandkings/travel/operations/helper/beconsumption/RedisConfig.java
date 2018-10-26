package com.coxandkings.travel.operations.helper.beconsumption;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConfig {

    public static final int DFT_REDIS_PORT = 6379;
    private static String mRedisHost = "10.24.2.248";
    private static int mRedisPort = 6379;
    private static JedisPool mRedisConnPool;

    public static void loadConfig() {
        mRedisConnPool = new JedisPool(mRedisHost, mRedisPort);
    }

    public static Jedis getRedisConnectionFromPool() {
        return mRedisConnPool.getResource();
    }

    public static void releaseRedisConnectionToPool(Jedis conn) {
        conn.close();
    }
}
