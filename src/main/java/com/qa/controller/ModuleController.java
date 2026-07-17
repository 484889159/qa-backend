package com.qa.controller;

import com.qa.common.Result;
import com.qa.entity.Module;
import com.qa.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module")
@CrossOrigin
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // 获取所有模块
    @GetMapping("/list")
    public Result getModuleList() {
        return Result.success(moduleService.getModuleList());
    }

    // 保存模块列表
    @PostMapping("/save")
    public Result saveModules(@RequestBody List<Module> modules) {
        boolean success = moduleService.saveModules(modules);
        return success ? Result.success("保存成功") : Result.error("保存失败");
    }

    // 更新单个模块
    @PutMapping("/update")
    public Result updateModule(@RequestBody Module module) {
        boolean success = moduleService.updateModule(module);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }
}