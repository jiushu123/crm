package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private UserService userService;

    //多条件查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams (SaleChanceQuery query){
        return saleChanceService.querySaleChanceParams(query);
    }
    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index () {
        return "saleChance/sale_chance";
    }

    /**
     * 营销机会数据添加与更新页面视图转发
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if (null != id) {
            // 通过主键查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            // 将数据存到作用域中
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance){

        //获取用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

        //获取用户的真实姓名
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();

        //设置营销机会的创建人
        saleChance.setCreateMan(trueName);

        //添加营销机会的数据
        saleChanceService.saveSaleChance(saleChance);

        //BaseController中的success方法,返回ResultInfo对象
        return success("营销机会数据添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        //更新营销机会的数据
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功");
    }

    /**
     * 删除营销机会数据
     * @param ids 勾选的数据id
     * @return 调用ResultInfo中的方法
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance (Integer[] ids) {
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

}
