package com.atcpl.crowd.api;

import com.atcpl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @author cpl
 * @date 2022/12/30
 * @apiNote
 */
@FeignClient("atcpl-crowd-redis")
@Repository
public interface RedisRemoteService {
    @RequestMapping("/set/redis/ket/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,@RequestParam("value") String value);

    @RequestMapping("/set/redis/ket/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("time") long time, @RequestParam("timeUnit") TimeUnit timeUnit);

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueRemoteByKey(@RequestParam("key") String key);

    @RequestMapping("/remover/redis/key/remove")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);


}
