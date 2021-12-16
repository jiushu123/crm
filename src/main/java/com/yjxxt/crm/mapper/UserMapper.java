package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    // 根据用户名查询用户对象
    User queryUserByUserName(String userName);

    // 查询所有的销售人员
    @MapKey("")
    public List<Map<String, Object>> queryAllSales();
}