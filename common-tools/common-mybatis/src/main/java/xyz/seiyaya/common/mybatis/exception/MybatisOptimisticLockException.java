package xyz.seiyaya.common.mybatis.exception;

/**
 * 乐观锁失败异常
 * @author wangjia
 * @version v1.0
 * @date 2020/11/10 14:18
 */
public class MybatisOptimisticLockException extends RuntimeException {

    public MybatisOptimisticLockException() {
    }

    public MybatisOptimisticLockException(String message) {
        super(message);
    }

    public MybatisOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisOptimisticLockException(Throwable cause) {
        super(cause);
    }

    public MybatisOptimisticLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
