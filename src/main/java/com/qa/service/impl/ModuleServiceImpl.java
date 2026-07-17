package com.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qa.entity.Module;
import com.qa.mapper.ModuleMapper;
import com.qa.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleMapper moduleMapper;

    @Override
    public List<Module> getModuleList() {
        LambdaQueryWrapper<Module> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Module::getSort);
        return moduleMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public boolean saveModules(List<Module> modules) {
        // 先删除所有
        moduleMapper.delete(null);
        // 再批量插入
        for (Module module : modules) {
            moduleMapper.insert(module);
        }
        return true;
    }

    @Override
    public boolean updateModule(Module module) {
        return moduleMapper.updateById(module) > 0;
    }
}