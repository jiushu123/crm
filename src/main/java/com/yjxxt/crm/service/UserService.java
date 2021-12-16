package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;


    @Resource
    private UserRoleMapper userRoleMapper;
    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd) {
        // 1. 验证参数
        checkLoginParams(userName, userPwd);
        // 2. 根据用户名，查询用户对象
        User user = userMapper.queryUserByUserName(userName);
        // 3. 判断用户是否存在 (用户对象为空，记录不存在，方法结束)
        AssertUtil.isTrue(null == user, "用户不存在或已注销！");
        // 4. 用户对象不为空（用户存在，校验密码。密码不正确，方法结束）
        checkLoginPwd(userPwd, user.getUserPwd());
        // 5. 密码正确（用户登录成功，返回用户的相关信息）
        return buildUserInfo(user);
    }
    /**
     * 构建返回的用户信息
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        // 设置用户信息（将 userId 加密）
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }
    /**
     * 验证登录密码
     * @param userPwd 前台传递的密码
     * @param upwd 数据库中查询到的密码
     * @return
     */
    private void checkLoginPwd(String userPwd, String upwd) {
// 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作比较
        userPwd = Md5Util.encode(userPwd);
// 比较密码
        AssertUtil.isTrue(!userPwd.equals(upwd), "用户密码不正确！");
    }
    /**
     * 验证用户登录参数
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
// 判断姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户姓名不能为空！");
// 判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空！");
    }

    /**
     * 用户密码修改
     * 1. 参数校验
     * 用户ID：userId 非空 用户对象必须存在
     * 原始密码：oldPassword 非空 与数据库中密文密码保持一致
     * 新密码：newPassword 非空 与原始密码不能相同
     * 确认密码：confirmPassword 非空 与新密码保持一致
     * 2. 设置用户新密码
     * 新密码进行加密处理
     * 3. 执行更新操作
     * 受影响的行数小于1，则表示修改失败
     *
     * 注：在对应的更新方法上，添加事务控制
     */
    public void updateUserPassword (Integer userId, String oldPassword, String newPassword, String confirmPassword ) {
        // 通过userId获取用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user, oldPassword, newPassword, confirmPassword);
        // 2. 设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        // 3. 执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码更新失败！");
    }

    /**
     * 验证用户密码修改参数
     * 用户ID：userId 非空 用户对象必须存在
     * 原始密码：oldPassword 非空 与数据库中密文密码保持一致
     * 新密码：newPassword 非空 与原始密码不能相同
     * 确认密码：confirmPassword 非空 与新密码保持一致
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        // user对象 非空验证
        AssertUtil.isTrue(null == user, "用户未登录或不存在！");
        // 原始密码 非空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入原始密码！");
        // 原始密码要与数据库中的密文密码保持一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))), "原始密码不正确！");
        // 新密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "请输入新密码！");
        // 新密码与原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword), "新密码不能与原始密码相同！");
        // 确认密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "请输入确认密码！");
        // 新密码要与确认密码保持一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码与确认密码不一致！");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }


    /**
     * 用户管理模块
     * 多条件分页查询用户数据
     * @param query 分页查询
     * @return map数据到前端
     */
    public Map<String, Object> queryUserByParams (UserQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 用户模块添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        System.out.println(user.getId()+"---"+user.getRoleIds());
        // 1. 参数校验
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        // 验证用户名是否存在
        User temp = userMapper.queryUserByUserName(user.getUserName());
        //添加用户    temp.getIsValid()!=0 , 防止数据库中失效数据影响添加
        AssertUtil.isTrue(null != temp && temp.getIsValid()!=0, "该用户已存在！");
        // 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertHasKey(user) == null, "用户添加失败！");
        //角色分配
        relaionUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * 分配角色
     * @param userId 当前用户的Id
     * @param roleIds 当前用户担任的所有角色
     */
    private void relaionUserRole(int userId, String roleIds) {
        /**
         * 用户角色分配
         * 原始角色不存在 添加新的角色记录
         * 原始角色存在 添加新的角色记录
         * 原始角色存在 清空所有角色
         * 原始角色存在 移除部分角色
         * 如何进行角色分配???
         * 如果用户原始角色存在 首先清空原始所有角色 添加新的角色记录到用户角色表
         */
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            //重新添加新的角色
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) < userRoles.size(), "用户角色分配失败!");
        }
    }


    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证用户名是否存在
        User user1 = userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue(user1 != null && !(user1.getId().equals(user.getId())), "用户已存在！");
        // 验证参数
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        // 2. 设置默认参数
        temp.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败！");

        System.out.println(user+"1111111111111111111");
        System.out.println(user.getUserName()+"22222222222222222222");
        System.out.println(userMapper.queryUserByUserName(user.getUserName())+"3333333333333333333333333");
        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        relaionUserRole(userId, user.getRoleIds());
    }
    /**
     * 用户模块参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }

    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length == 0,"请选择待删除的用户记录!");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"用户记录删除失败!");
    }


}