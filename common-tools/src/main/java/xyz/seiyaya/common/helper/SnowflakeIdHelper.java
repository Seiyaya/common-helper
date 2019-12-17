package xyz.seiyaya.common.helper;

/**
 * <br>Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * 优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，每秒能够产生26万ID左右。
 * @author Partner
 */
public class SnowflakeIdHelper {

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 机器id所占的位数
     */
    private static long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static long dataCenterIdBits = 5L;

    /**
     * 序列在id中占的位数
     */
    private static long sequenceBits = 12L;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 开始时间戳
     */
    private static long startTime = 1510153222248L;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits ;

    /**
     * 机器id,默认为1
     */
    private static int workerId = 1;

    /**
     * 机器ID向左移12位
     */
    private static long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static long dataCenterIdShift = sequenceBits + workerIdBits;


    public synchronized static long nextId() {
        long timestamp = System.currentTimeMillis();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID

        /**
         * 数据中心ID(0~31)
         */
        long dataCenterId = 0L;
        long resultLong = ((timestamp - startTime) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
        return resultLong;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
