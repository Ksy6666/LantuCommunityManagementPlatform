package com.example.lantu_web_java.ShujuKu;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 独立 main 程序：连接指定 Redis 实例，遍历并打印所有 key 及其数据。
 * <p>
 * 零第三方依赖，使用原生 Socket 直接实现 Redis RESP 协议，
 * 通过 {@code javac} + {@code java} 即可直接运行，无需 Spring Boot 或 Maven classpath。
 * <p>
 * 运行方式（在源码所在目录）：
 * <pre>
 *   javac RedisDumpAll.java
 *   java RedisDumpAll
 * </pre>
 * 也可指定 Redis 地址与端口：{@code java RedisDumpAll <host> <port>}
 */
public class RedisDumpAll {

    private static final String DEFAULT_HOST = "192.168.226.132";
    private static final int DEFAULT_PORT = 6379;

    // 使用 UTF-8 输出，避免中文乱码
    private final PrintStream out =
            new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
    private Socket socket;
    private InputStream in;
    private OutputStream outStream;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        new RedisDumpAll().run(host, port);
    }

    public void run(String host, int port) {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(10000);
            in = socket.getInputStream();
            outStream = socket.getOutputStream();

            out.println("=== Redis 数据总览 (" + host + ":" + port + ") ===");
            out.println();

            // 1) 用 SCAN 遍历收集所有 key（避免 KEYS 在大库下阻塞）
            List<String> keys = scanAllKeys();
            Collections.sort(keys);

            out.println("共 " + keys.size() + " 个 key：");
            for (String k : keys) {
                out.println("  - " + k);
            }

            // 2) 逐个打印每个 key 的内容
            for (String key : keys) {
                printKey(key);
            }

            out.println();
            out.println("=== 打印完成 ===");
        } catch (Exception e) {
            out.println("读取 Redis 失败：" + host + ":" + port + " - " + e.getMessage());
            e.printStackTrace(out);
        } finally {
            close();
        }
    }

    // ---------- RESP 协议实现 ----------

    /** 发送一条命令，返回解析后的回复（String / Long / List / null） */
    private Object send(String... args) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeCrlf(bos, "*" + args.length);
        for (String a : args) {
            byte[] b = a.getBytes(StandardCharsets.UTF_8);
            writeCrlf(bos, "$" + b.length);
            bos.write(b);
            bos.write('\r');
            bos.write('\n');
        }
        outStream.write(bos.toByteArray());
        outStream.flush();
        return readReply();
    }

    private void writeCrlf(ByteArrayOutputStream bos, String s) throws IOException {
        bos.write(s.getBytes(StandardCharsets.UTF_8));
        bos.write('\r');
        bos.write('\n');
    }

    /** 解析一个 RESP 回复 */
    private Object readReply() throws IOException {
        int type = in.read();
        if (type == -1) throw new IOException("连接已关闭");
        switch (type) {
            case '+': // 简单字符串
                return readLine();
            case '-': // 错误
                throw new IOException("Redis 错误：" + readLine());
            case ':': // 整数
                return Long.parseLong(readLine());
            case '$': { // 批量字符串
                int len = Integer.parseInt(readLine());
                if (len == -1) return null;
                byte[] data = new byte[len];
                readFully(data);
                readCrlf();
                return new String(data, StandardCharsets.UTF_8);
            }
            case '*': { // 数组
                int count = Integer.parseInt(readLine());
                if (count == -1) return null;
                List<Object> list = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    list.add(readReply());
                }
                return list;
            }
            default:
                throw new IOException("未知 RESP 类型: " + (char) type);
        }
    }

    /** 读取一行（不含末尾 CRLF） */
    private String readLine() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\r') {
                int next = in.read();
                if (next == '\n') break;
                bos.write(b);
                bos.write(next);
            } else {
                bos.write(b);
            }
        }
        return bos.toString(StandardCharsets.UTF_8.name());
    }

    private void readFully(byte[] buf) throws IOException {
        int off = 0;
        while (off < buf.length) {
            int n = in.read(buf, off, buf.length - off);
            if (n == -1) throw new IOException("连接意外关闭");
            off += n;
        }
    }

    private void readCrlf() throws IOException {
        int a = in.read();
        int b = in.read();
        if (a != '\r' || b != '\n') throw new IOException("协议错误：缺少 CRLF");
    }

    // ---------- 业务逻辑 ----------

    /** SCAN 遍历所有 key */
    private List<String> scanAllKeys() throws IOException {
        List<String> keys = new ArrayList<>();
        long cursor = 0;
        do {
            Object reply = send("SCAN", String.valueOf(cursor), "COUNT", "1000");
            List<?> resp = (List<?>) reply;
            cursor = Long.parseLong((String) resp.get(0));
            List<?> batch = (List<?>) resp.get(1);
            for (Object k : batch) {
                keys.add((String) k);
            }
        } while (cursor != 0);
        return keys;
    }

    /** 根据类型打印 key 内容 */
    private void printKey(String key) throws IOException {
        String type = (String) send("TYPE", key);
        out.println();
        out.println("----- key: " + key + "  (类型: " + type + ") -----");
        switch (type) {
            case "string": {
                Object v = send("GET", key);
                out.println("  value = " + (v == null ? "(nil)" : v));
                break;
            }
            case "list": {
                Object v = send("LRANGE", key, "0", "-1");
                List<?> list = (List<?>) v;
                for (int i = 0; i < list.size(); i++) {
                    out.println("  [" + i + "] " + list.get(i));
                }
                break;
            }
            case "hash": {
                Object v = send("HGETALL", key);
                List<?> list = (List<?>) v;
                for (int i = 0; i + 1 < list.size(); i += 2) {
                    out.println("  " + list.get(i) + " = " + list.get(i + 1));
                }
                break;
            }
            case "set": {
                Object v = send("SMEMBERS", key);
                List<?> list = (List<?>) v;
                for (Object o : list) {
                    out.println("  " + o);
                }
                break;
            }
            case "zset": {
                Object v = send("ZRANGE", key, "0", "-1", "WITHSCORES");
                List<?> list = (List<?>) v;
                for (int i = 0; i + 1 < list.size(); i += 2) {
                    out.println("  member=" + list.get(i) + "  score=" + list.get(i + 1));
                }
                break;
            }
            default:
                out.println("  (暂不支持的类型: " + type + ")");
        }
    }

    private void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {
            // 忽略关闭异常
        }
    }
}
