package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {


    //根据角色id查询拥有的资源数量
    Integer countPermissionByRoleId(Integer roleId);

    //根据角色id删除数据
    Integer deletePermissionsByRoleId(Integer roleId);

    //根据角色id查询资源id
    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);

    //根据用户id查询资源权限码
    List<String> queryUserHasRolesHasPermissions(Integer userId);
}