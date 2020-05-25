package xyz.seiyaya.redis.struct.server;

import xyz.seiyaya.redis.struct.common.RedisDb;

import java.util.Arrays;

/**
 * 定时任务相关
 * 默认每100毫秒执行一次
 * @author wangjia
 * @date 2020/5/25 13:48
 */
public class ServerCron {

    /**
     * 默认的数据库数量
     */
    public static final int DEFAULT_DB_NUMBERS = 16;
    /**
     * 每个数据库检查键的数量
     */
    public static final int DEFAULT_KEY_NUMBERS = 20;

    private RedisServer redisServer;

    public ServerCron(RedisServer redisServer){
        this.redisServer = redisServer;
    }

    /**
     * 定期删除
     */
    public void activeExpireCycle(){
        // 初始化要检查的数据库数量
        int dbNums = 0;
        if( redisServer.dbNum < DEFAULT_DB_NUMBERS){
            dbNums = redisServer.dbNum;
        }else{
            dbNums = DEFAULT_DB_NUMBERS;
        }

        for(int i=0;i<dbNums;i++){
            RedisDb redisDb = redisServer.redisDbs[i];
            if( redisDb.expireSize() == 0 ){
                continue;
            }

            redisDb.getExpires().keySet().forEach(model->{
                Long expireValue = redisDb.getExpires().get(model);
                if(System.currentTimeMillis() > expireValue){
                    redisDb.delExpire(model);
                }

                // 如果时间到达，会停止处理
            });
        }
    }


    public void checkRdbUpdate(){
        Arrays.stream(redisServer.saveParams).forEach(model->{
            long diff = System.currentTimeMillis() - redisServer.lastSaveTime;
            if(redisServer.dirty >= model.getUpdateCount() && diff/1000 <= model.getTime()){
                redisServer.bgSave();
            }
        });
    }
}
