package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    // 角色名
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleQuery{" +
                "roleName='" + roleName + '\'' +
                "} " + super.toString();
    }
}
