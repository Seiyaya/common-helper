package xyz.seiyaya.redis.application;

import java.util.HashMap;
import java.util.Map;

/**
 * 漏斗限流
 * @author wangjia
 * @version 1.0
 */
public class FunnelRateLimiter {

    private static class Funnel{
        /**
         * 容量
         */
        int capacity;
        /**
         * 流速
         */
        double leakingRate;
        /**
         * 剩余空间
         */
        int leftQuota;
        /**
         * 上一次访问时间
         */
        long leakingTs;

        public Funnel(int capacity, double leakingRate){
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }

        void makeSpace(){
            long nowTs = System.currentTimeMillis();
            long deltaTs = nowTs - leakingTs;
            int deltaQuota = (int) (deltaTs * leakingRate);
            if(deltaQuota < 0){
                // 间隔时间太长导致整数溢出
                this.leftQuota = capacity;
                this.leakingRate = nowTs;
                return ;
            }
            if(deltaQuota < 1){
                return ;
            }
            this.leftQuota += deltaQuota;
            this.leakingTs = nowTs;
            if(leftQuota > capacity){
                leftQuota = capacity;
            }
        }

        boolean watering(int quota){
            makeSpace();
            if(this.leftQuota >= quota){
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }

    private Map<String, Funnel> funnelMap = new HashMap<>();

    public static void main(String[] args) {
        FunnelRateLimiter rateLimiter = new FunnelRateLimiter();
        for(int i=0;i<20;i++){
            boolean login = rateLimiter.isActionAllowed(1, "login", 5, 3);
            System.out.println(login);
        }
    }


    public boolean isActionAllowed(int userId, String actionKey, int capacity, double leakingRate){
        String key = String.format("%s:%s",userId, actionKey);
        Funnel funnel = funnelMap.get(key);
        if(funnel == null){
            funnel = new Funnel(capacity, leakingRate);
            funnelMap.put(key, funnel);
        }
        return funnel.watering(1);
    }
}
