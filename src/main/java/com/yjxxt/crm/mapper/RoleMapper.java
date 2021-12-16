package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    // 查询角色列表 用户模块中的下拉框
    @MapKey("")
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    /**
     * 根据名称查询数据
     * @param roleName
     * @return
     */
    Role queryRoleByRoleName(String roleName);

}