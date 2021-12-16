package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的资源模块信息 三个字段
    public List<TreeDto> selectModules();

    //查询所有资源模块信息 所有字段
    List<Module> selectAllModules();

}







