package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param userPwd 用户密码
     * @return 结果信息
     */
    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();

        // 调用Service层的登录方法，得到返回的用户对象
        UserModel userModel = userService.userLogin(userName, userPwd);
        /**
         * 登录成功后，有两种处理：
         * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
         需要重复登录 ）
         * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
         */
        // 将返回的UserModel对象设置到 ResultInfo 对象中
        resultInfo.setResult(userModel);

        return resultInfo;
    }

    /**
     * 用户密码修改
     *
     * @param request 用来调用工具类获取userID
     * @param oldPassword 旧密码
     * @param newPassword   新密码
     * @param confirmPassword 确认密码
     * @return 结果消息
     */
    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();

        // 获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用Service层的密码修改方法
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);

        return resultInfo;
    }

    /**
     * 修改密码
     * @return 跳转修改密码页面
     */
    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 加载基本信息
     * @param req
     * @return
     */
    @RequestMapping("user/toSettingPage")
    public String setting(HttpServletRequest req){
        //调用工具类获取用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);

        User user = userService.selectByPrimaryKey(userId);

        req.setAttribute("user",user);

        return "user/setting";
    }

    /**
     * 修改基本信息
     * @param user
     * @return
     */
    @RequestMapping("user/updateSetting")
    @ResponseBody
    public ResultInfo updateSetting(User user){
        ResultInfo resultInfo = new ResultInfo();
        userService.updateByPrimaryKeySelective(user);
        return resultInfo;
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("user/queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }
    /**
     * 多条件查询用户数据
     * @param userQuery 分页查询类
     * @return 查询结果返回到前端user.js
     */
    @RequestMapping("user/list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }
    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("user/index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("user/save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("user/addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }


    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUserByIds(ids);
        return success("用户记录删除成功");
    }
}





