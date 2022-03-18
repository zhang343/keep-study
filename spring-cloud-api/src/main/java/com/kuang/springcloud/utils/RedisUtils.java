package com.kuang.springcloud.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类，使用之前请确保RedisTemplate成功注入
 */
@Component
public class RedisUtils implements InitializingBean {

    private RedisUtils() {
    }

    @Resource
    private RedisTemplate<String, Object> privateRedisTemplate;

    private static RedisTemplate<String, Object> redisTemplate;

    public static final String ARTICLEVIEWLOCK = "articleViewLock";

    public static final String ARTICLEVIEW = "articleView";

    public static final String ARTICLLE = "article";

    public static final int ARTICLEVIEWTIME = 7;

    public static final String COURSEVIEWLOCK = "courseViewLock";

    public static final String COURSEVIEW = "courseView";

    public static final String COURSE = "course";

    public static final int COURSEVIEWTIME = 7;

    public static final String USERTODATRIGHTLOCK = "userTodayRightLock";

    public static final String USERTODATRIGHT = "userTodayRight";




    public static final String NOTVIPRIGHT = "notVipRight";

    public static final String ALLRIGHTLIST = "allRightList";

    public static final String ALLRIGHTTREEMAP = "allRightTreeMap";

    public static final String ALLVIPMEMBERTREEMAP = "allVipMemberTreeMap";

    public static final String AllUSERNUMBER = "allUserNumber";

    public static final String COURSEORDERBYPRICE = "courseOrderByPrice";

    /**
     *分布式锁，用来保证定时任务不重复执行
     * @param timeout 键值对缓存的时间，单位是秒
     * @return 设置成功返回true，否则返回false
     */
    public static boolean tryArticleLock(long timeout) {
        //底层原理就是Redis的setnx方法
        boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(ARTICLEVIEWLOCK, ARTICLEVIEW);
        if (isSuccess) {
            //设置分布式锁的过期时间
            redisTemplate.expire(ARTICLEVIEWLOCK, timeout, TimeUnit.SECONDS);
        }
        return isSuccess;
    }

    /**
     * 分布式锁，用来保证定时任务不重复执行
     * @return 释放成功返回true，否则返回false
     */
    public static boolean unArticleLock() {
        return redisTemplate.delete(ARTICLEVIEWLOCK);
    }

    /**
     *分布式锁，用来保证定时任务不重复执行
     * @param timeout 键值对缓存的时间，单位是秒
     * @return 设置成功返回true，否则返回false
     */
    public static boolean tryCourseLock(long timeout) {
        //底层原理就是Redis的setnx方法
        boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(COURSEVIEWLOCK, COURSEVIEW);
        if (isSuccess) {
            //设置分布式锁的过期时间
            redisTemplate.expire(COURSEVIEWLOCK, timeout, TimeUnit.SECONDS);
        }
        return isSuccess;
    }

    /**
     * 分布式锁，用来保证定时任务不重复执行
     * @return 释放成功返回true，否则返回false
     */
    public static boolean unCourseLock() {
        return redisTemplate.delete(COURSEVIEWLOCK);
    }

    /**
     *分布式锁，用来保证定时任务不重复执行
     * @param timeout 键值对缓存的时间，单位是秒
     * @return 设置成功返回true，否则返回false
     */
    public static boolean tryUserTodayRightLock(long timeout) {
        //底层原理就是Redis的setnx方法
        boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(USERTODATRIGHTLOCK, USERTODATRIGHT);
        if (isSuccess) {
            //设置分布式锁的过期时间
            redisTemplate.expire(USERTODATRIGHTLOCK, timeout, TimeUnit.SECONDS);
        }
        return isSuccess;
    }

    /**
     * 分布式锁，用来保证定时任务不重复执行
     * @return 释放成功返回true，否则返回false
     */
    public static boolean unUserTodayRightLock() {
        return redisTemplate.delete(USERTODATRIGHTLOCK);
    }




    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    /**
     * 删除单个key
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean delKey(final String key) {
        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }

    /**
     * 删除多个key
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static Long delKeys(final Collection<String> keys) {
        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }

    /**
     * 存入普通对象
     * @param key Redis键
     * @param value 值
     */
    public static void setValue(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.MINUTES);
    }

    // 存储普通对象操作

    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public static void setValueTimeout(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public static Object getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 存储Hash操作

    /**
     * 确定哈希hashKey是否存在
     *
     * @param key 键
     * @param hkey hash键
     * @return true=存在；false=不存在
     */
    public static boolean hasHashKey(final String key,String hkey) {
        Boolean ret = redisTemplate.opsForHash().hasKey(key,hkey);
        return ret != null && ret;
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public static void hashPut(final String key, final String hKey, final Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 往Hash中存入多个数据
     *
     * @param key Redis键
     * @param values Hash键值对
     */
    public static void hashPutAll(final String key, final Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static Object hashGet(final String key, final String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @return Hash对象
     */
    public static Map<Object, Object> hashGetAll(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hashMultiGet(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static long hashDeleteKeys(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().delete(key,hKeys);
    }

    // 存储Set相关操作

    /**
     * 往Set中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long setSet(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }


    /**
     * 统计set的数据条数
     *
     * @param key Redis键
     * @return set的数据条数
     */
    public static long getSetSize(final String key) {
        Long count = redisTemplate.opsForSet().size(key);
        return count == null ? 0 : count;
    }

    /**
     * 删除Set中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long setDel(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 获取set中的所有对象
     *
     * @param key Redis键
     * @return set集合
     */
    public static Set<Object> getSetAll(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // 存储ZSet相关操作

    /**
     * 往ZSet中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long zsetSet(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除ZSet中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long zsetDel(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }

    // 存储List相关操作

    /**
     * 往List中存入数据
     *
     * @param key Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public static long listPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long listPushAll(final String key, final Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }



    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public static List<Object> listGet(final String key, final int start, final int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void afterPropertiesSet() throws Exception {
        RedisUtils.redisTemplate = privateRedisTemplate;
    }
}
