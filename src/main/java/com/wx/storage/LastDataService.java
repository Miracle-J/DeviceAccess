package com.wx.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将每个产线每台设备的最后一条数据持久化到文件中。
 */
@Component
public class LastDataService {

    /** 内存中的数据缓存：port -> (line-device -> data) */
    private final Map<Integer, Map<String, Object>> cache = new ConcurrentHashMap<>();
    /** JSON 序列化工具 */
    private final ObjectMapper mapper = new ObjectMapper();
    /** 数据文件路径 */
    private final Path filePath;

    public LastDataService(@Value("${lastdata.file:}") String path) {
        if (path == null || StringUtils.isEmpty( path)) {
            this.filePath = Paths.get(System.getProperty("user.home"), "last-data.json");
        } else {
            this.filePath = Paths.get(path);
        }
    }

    /** 启动时尝试读取已有的数据文件 */
    @PostConstruct
    public void init() {
        if (Files.exists(filePath)) {
            try {
                Map<Integer, Map<String, Object>> fromFile = mapper.readValue(
                        filePath.toFile(), new TypeReference<Map<Integer, Map<String, Object>>>() {});
                cache.putAll(fromFile);
            } catch (IOException e) {
                // 读取失败时忽略，使用空缓存
            }
        }
    }

    /**
     * 保存最新数据并写入文件。
     *
     * @param port    数据来源端口
     * @param lineId  产线ID
     * @param deviceId 设备ID
     * @param data    原始数据对象
     */
    public void save(int port, int lineId, int deviceId, Object data) {
        cache.computeIfAbsent(port, p -> new ConcurrentHashMap<>())
                .put(lineId + "-" + deviceId, data);
        try {
            Files.createDirectories(filePath.getParent());
            mapper.writeValue(filePath.toFile(), cache);
        } catch (IOException e) {
            // 写入失败时忽略
        }
    }

    /**
     * 获取所有缓存的数据。
     *
     * @return 内存中的全部数据
     */
    public Map<Integer, Map<String, Object>> getAll() {
        return cache;
    }
}

