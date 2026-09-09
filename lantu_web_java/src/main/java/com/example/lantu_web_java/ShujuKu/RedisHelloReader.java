package com.example.lantu_web_java.ShujuKu;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 独立 main 程序：连接指定 Redis 实例（192.168.226.132:6379），读取 key "hello" 并打印其值。
 * <p>
 * 通过 {@code main} 直接运行，不随 Spring Boot 一起启动。使用 Spring Data Redis 的
 * StringRedisTemplate，与项目现有 Redis 用法保持一致。
 * <p>
 * 独立运行方式（任选其一，需要 classpath 已包含项目依赖）：
 * <pre>
 *   mvn exec:java -Dexec.mainClass=com.example.lantu_web_java.ShujuKu.RedisHelloReader
 *   java -cp "target/classes;target/classes/...依赖..." com.example.lantu_web_java.ShujuKu.RedisHelloReader
 * </pre>
 */
public class RedisHelloReader {

    private static final String REDIS_HOST = "192.168.226.132";
    private static final int REDIS_PORT = 6379;
    private static final String HELLO_KEY = "hello";
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final PrintStream out;
    public RedisHelloReader() {
        this.out = System.out;
    }
    public static void main(String[] args) {
        // 确保 Windows 控制台以 UTF-8 输出中文，避免乱码
        System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
        new RedisHelloReader().run();
    }

    /**
     * 执行读取逻辑。返回 0 表示成功，非 0 表示失败，便于脚本判断。
     */
    public int run() {
        LettuceConnectionFactory factory = null;
        int exitCode = 0;
        try {
            // 单独构造指向 192.168.226.132:6379 的连接工厂，不影响 Spring 默认 localhost 配置
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
            factory = new LettuceConnectionFactory(config);
            factory.afterPropertiesSet();

            StringRedisTemplate redis = new StringRedisTemplate(factory);
            redis.afterPropertiesSet();

            String value = redis.opsForValue().get(HELLO_KEY);

            logLine("=== Redis 读取结果 ===");
            logLine("Redis 地址: " + REDIS_HOST + ":" + REDIS_PORT);
            logLine("key: " + HELLO_KEY);
            logLine("value: " + value);

            if (value == null) {
                logLine("提示：Redis 中不存在 key [" + HELLO_KEY + "]，返回值为 null。");
            }
        } catch (Exception e) {
            exitCode = 1;
            logLine("读取 Redis 失败：" + REDIS_HOST + ":" + REDIS_PORT + " - " + e.getMessage());
        } finally {
            if (factory != null) {
                factory.destroy();
            }
        }
        return exitCode;
    }

    private void logLine(String msg) {
        out.println("[" + LocalDateTime.now().format(TS) + "] " + msg);
    }
}