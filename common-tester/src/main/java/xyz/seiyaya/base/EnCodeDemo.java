package xyz.seiyaya.base;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/28 14:25
 */
@Slf4j
public class EnCodeDemo {

    public static void main(String[] args) {
        System.out.println(crackToken("6060607B67616766666F67666560646E6E6E63626464607B64626F67616E6E"));


        String s = Encode.byte2HexStr(Encode.encode("666-1804040949326994236-1234567"));
        System.out.println(s);

        long userId = Long.parseLong("5923");
        Jedis jedis = new Jedis("redis.seiyaya.local",6382);
        jedis.auth("#abF%F$c#197#33FF#");
        String userString = jedis.get("USER:INFO:" + userId);
        if(userString != null){
            JSONObject jsonObject = JSONObject.parseObject(userString);
            System.out.println(jsonObject.getString("token"));
        }else{
            log.info("login info is null");
        }
    }

    private static Long crackToken(String token) {
        try {
            byte[] enc = Encode.hexStr2Bytes(token);
            String tmp = Encode.decode(enc);
            String[] res = tmp.split("-");
            return Long.valueOf(res[1]);
        } catch (Exception e) {
            return 0L;
        }
    }

    private static class Encode {
        private static final String key = "ABCQWERQWER##FFQWERAAAAAFDSAFASDF";
        private static final Charset charset = StandardCharsets.UTF_8;
        private static byte[] keyBytes;

        public Encode() {
        }

        public static byte[] encode(String enc) {
            byte[] b = enc.getBytes(charset);
            int i = 0;

            for (int size = b.length; i < size; ++i) {
                byte[] var4 = keyBytes;
                int var5 = var4.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    byte keyBytes0 = var4[var6];
                    b[i] ^= keyBytes0;
                }
            }

            return b;
        }

        public static String getSeven() {
            Random rad = new Random();
            int next = rad.nextInt(10000000);
            StringBuilder sb = new StringBuilder();
            int length = ("" + next).length();
            for (int i = 0; i < 7 - length; i++) {
                sb.append(0);
            }
            sb.append(next);
            return sb.toString();
        }

        public static String decode(byte[] e) {
            byte[] dee = e;
            int i = 0;

            for (int size = e.length; i < size; ++i) {
                byte[] var4 = keyBytes;
                int var5 = var4.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    byte keyBytes0 = var4[var6];
                    e[i] = (byte) (dee[i] ^ keyBytes0);
                }
            }

            return new String(e);
        }

        public static String str2HexStr(String str) {
            char[] chars = "0123456789ABCDEF".toCharArray();
            StringBuilder sb = new StringBuilder();
            byte[] bs = str.getBytes();

            for (int i = 0; i < bs.length; ++i) {
                int bit = (bs[i] & 240) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 15;
                sb.append(chars[bit]);
            }

            return sb.toString();
        }

        public static String hexStr2Str(String hexStr) {
            String str = "0123456789ABCDEF";
            char[] hexs = hexStr.toCharArray();
            byte[] bytes = new byte[hexStr.length() / 2];

            for (int i = 0; i < bytes.length; ++i) {
                int n = str.indexOf(hexs[2 * i]) * 16;
                n += str.indexOf(hexs[2 * i + 1]);
                bytes[i] = (byte) (n & 255);
            }

            return new String(bytes);
        }

        public static String byte2HexStr(byte[] b) {
            String hs = "";
            String stmp = "";

            for (int n = 0; n < b.length; ++n) {
                stmp = Integer.toHexString(b[n] & 255);
                if (stmp.length() == 1) {
                    hs = hs + "0" + stmp;
                } else {
                    hs = hs + stmp;
                }
            }

            return hs.toUpperCase();
        }

        private static byte uniteBytes(String src0, String src1) {
            byte b0 = Byte.decode("0x" + src0);
            b0 = (byte) (b0 << 4);
            byte b1 = Byte.decode("0x" + src1);
            byte ret = (byte) (b0 | b1);
            return ret;
        }

        public static byte[] hexStr2Bytes(String src) {
            int l = src.length() / 2;
            byte[] ret = new byte[l];

            for (int i = 0; i < l; ++i) {
                int m = i * 2 + 1;
                int n = m + 1;
                ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
            }

            return ret;
        }

        public static String str2Unicode(String strText) throws Exception {
            String strRet = "";

            for (int i = 0; i < strText.length(); ++i) {
                char c = strText.charAt(i);
                String strHex = Integer.toHexString(c);
                if (c > 128) {
                    strRet = strRet + "//u" + strHex;
                } else {
                    strRet = strRet + "//u00" + strHex;
                }
            }

            return strRet;
        }

        public static String unicode2Str(String hex) {
            int t = hex.length() / 6;
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < t; ++i) {
                String s = hex.substring(i * 6, (i + 1) * 6);
                String s1 = s.substring(2, 4) + "00";
                String s2 = s.substring(4);
                int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
                char[] chars = Character.toChars(n);
                str.append(new String(chars));
            }

            return str.toString();
        }

        static {
            keyBytes = "ABCQWERQWER##FFQWERAAAAAFDSAFASDF".getBytes(charset);
        }
    }
}
