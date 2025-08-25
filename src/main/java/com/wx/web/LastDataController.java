package com.wx.web;

import com.wx.storage.LastDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 提供查询所有产线所有设备最后一条数据的接口。
 */
@RestController
public class LastDataController {

    private final LastDataService service;

    public LastDataController(LastDataService service) {
        this.service = service;
    }

    /**
     * 获取所有缓存的最后一条数据。
     *
     * @return port -> (line-device -> data)
     */
    @GetMapping("/last-data")
    public Map<Integer, Map<String, Object>> all() {
        return service.getAll();
    }
}

