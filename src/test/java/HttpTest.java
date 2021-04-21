import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;

import static sun.security.krb5.internal.KDCOptions.with;


/**
 * @author libo
 * @version 1.0
 * @date 2017/5/24 13:34
 */
public class HttpTest {

    public void test(){
        // -2^63 -- 2^63-1 64位 4个字节
        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.MIN_VALUE);

        // -2^31 -- 2^31-1 32位 3个字节
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);

        // -2^15 -- 2^15-1 16位 2个字节
        System.out.println(Short.MAX_VALUE);
        System.out.println(Short.MIN_VALUE);

        // -2^7 -- 2^7-1 8位 1个字节
        System.out.println(Byte.MAX_VALUE);
        System.out.println(Byte.MIN_VALUE);

        // -3.4*E38- 3.4*E38 32位 4个字节
        System.out.println(Double.MAX_VALUE);
        System.out.println(Double.MIN_VALUE);

        // 64位
        System.out.println(Float.MAX_VALUE);
        System.out.println(Float.MIN_VALUE);

        // Boolean
        // String
        // Char 16位Unicode字符 2个字节
    }

    @Test
    public void test1(){
        System.out.println(Instant.now());
        System.out.println(LocalDateTime.now().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse("2017-06-02 15:26:32",formatter);
        System.out.println(date.toString());
        System.out.println(formatter.format(LocalDateTime.now()));
        //乐观锁 悲观锁

        LongAdder count = new LongAdder();

        int start=300,end = 500;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Long i = random.nextLong(end) % (end - start + 1) + start;
        System.out.println(i);
        OptionalInt test = OptionalInt.empty();
    }
    @Test
    public void test2(){
        int initialCapacity = 56;
        int MAXIMUM_CAPACITY = 1 << 30;

        int n = initialCapacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int threshold =  (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        System.out.println(threshold);
    }


    @Test
    public void test3() throws UnsupportedEncodingException {
        HashMap map = new HashMap(100);
        map.put("1","2");
        map.put("2","4");
        map.putIfAbsent("2","5");
        System.out.println(map.get("2"));
    }
}
