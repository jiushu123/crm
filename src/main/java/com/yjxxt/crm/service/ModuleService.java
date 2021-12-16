package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {


    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 权限回显
     * @param roleId
     * @return
     */
    public List<TreeDto> findModules(Integer roleId){
        //查询所有的资源
        List<TreeDto> treeDtos = moduleMapper.selectModules();

        //根据角色id查询资源id
        List<Integer> integer = permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);

        if (null != integer && integer.size()>0){
            treeDtos.forEach(treeDto -> {
                if (integer.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }

        return treeDtos;
    }


    /**
     * 加载module数据表格
     * @return
     */
    public Map<String, Object> queryModules() {
        //准备数据
        Map<String,Object> map=new HashMap<String,Object>();
        //查询所有的资源
        List<Module> mlist=moduleMapper.selectAllModules();
        //准备数据项
        map.put("code",0);
        map.put("msg","success");
        map.put("count",mlist.size());
        map.put("data",mlist);
        //返回目标map
        return map;
    }




}
