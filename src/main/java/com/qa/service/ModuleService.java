package com.qa.service;

import com.qa.entity.Module;
import java.util.List;

public interface ModuleService {
    List<Module> getModuleList();
    boolean saveModules(List<Module> modules);
    boolean updateModule(Module module);
}