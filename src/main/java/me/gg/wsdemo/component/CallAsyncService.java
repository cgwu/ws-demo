package me.gg.wsdemo.component;

import lombok.extern.slf4j.Slf4j;
import me.gg.wsdemo.entity.LockEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sam on 18-12-24.
 */
@Service
@Slf4j
public class CallAsyncService {

    private static Map<String, LockEntity> mapLock = new ConcurrentHashMap<String, LockEntity>();

    public String doAction(String id) {
        log.info("开始doAction: {}", id);
        LockEntity lock = new LockEntity();
        mapLock.put(id, lock);

        synchronized (lock) {
            try {
                lock.wait(10000);
            } catch (InterruptedException e) {
                log.info("等待已超时: {}", id);
                return "doAction Timeout:" + id;
            } finally {
                log.info("before删除锁对象: {},大小: {} ", id, mapLock.size());
                mapLock.remove(id);
                log.info("after删除锁对象: {},大小: {} ", id, mapLock.size());
            }
        }
        log.info("doAction Success: {}, result: {}", id, lock.getData());
        return "doAction Success:" + id + ", Result:" + lock.getData();
    }

    public boolean notifyAction(String id, String data) {
        log.info("开始notifyAction: {}, data: {}", id, data);
        if (!mapLock.containsKey(id)) return false;

        LockEntity lock = mapLock.get(id);
        lock.setData(data); // set result data

        synchronized (lock) {
            lock.notify();
        }
        return true;
    }

}
