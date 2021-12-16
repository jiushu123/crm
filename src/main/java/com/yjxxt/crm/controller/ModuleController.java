package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){

        System.out.println(roleId+"----------------as9d4f84sa56d464sd6f46sd4f64sd65f");

        return moduleService.findModules(roleId);
    }

    /**
     *跳转module页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /**
     * 加载module数据表格
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(){
        return moduleService.queryModules();
    }


}
