package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired(required = false)
    private RoleService roleService;

    /**
     * 查询角色列表 用户模块中的下拉框
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }


    /**
     * 跳转角色模块
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 为角色模块中加载数据
     * @param roleQuery 条件查询
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "6020")
    public Map<String,Object> userList(RoleQuery roleQuery) {
        return roleService.findRoleByParam(roleQuery);
    }


    /**
     * 执行添加
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("角色记录添加成功");
    }

    /**
     * 执行修改
     * @param role
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色记录更新成功");
    }

    /**
     * 跳转角色添加修改弹出框时,判断要不要加载数据
     * @param id 前端带过来的数据
     * @param model 将从数据库查询的数据,存到域中
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }

    /**
     * 删除数据
     * @param id role.js行监听传过来的id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色记录删除成功");
    }


    /**
     * 授权
     * @param roleId 角色Id
     * @param model
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 角色授权添加
     * @param mids
     * @param roleId
     * @return
     */
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限添加成功");
    }



}
