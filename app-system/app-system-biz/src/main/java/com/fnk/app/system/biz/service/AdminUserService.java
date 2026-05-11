package com.fnk.app.system.biz.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.response.LoginVO;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.tools.lang.AssertUtils;
import com.fnk.common.tools.utils.SaltUtils;
import com.fnk.starter.web.enums.GenderType;
import com.fnk.app.system.biz.cache.RoleCache;
import com.fnk.app.system.api.model.request.LoginAO;
import com.fnk.app.system.biz.dal.entity.AdminUserDO;
import com.fnk.app.system.biz.dal.mapper.AdminUserMapper;
import com.fnk.app.system.api.model.response.AdminUserVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统用户 服务实现层
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Service
@AllArgsConstructor
public class AdminUserService extends BaseService<AdminUserMapper, AdminUserDO> {


    private UserRoleService userRoleService;



    @Transactional(rollbackFor = Exception.class)
    public void initAdminUser() {
        long count = this.count();
        AssertUtils.isTrue(count >= 1, "系统用户已经初始化");
        AdminUserDO adminUser = new AdminUserDO();
        adminUser.setUsername("admin");
        adminUser.setSalt(SaltUtils.getSalt(4));
        adminUser.setPhone("18888888888");
        adminUser.setPassword("123456");
        adminUser.setSex(GenderType.MAN);
        adminUser.setStatus(true);
        adminUser.setPassword(encryptPassword(adminUser.getPassword(), adminUser.getSalt()));
        this.save(adminUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminUserDO saveAdminUser(AdminUserDO adminUser) {
        AssertUtils.isTrue(isPhoneExist(adminUser.getPhone(), null), "手机号码已存在！");
        adminUser.setSalt(SaltUtils.getSalt(4));
        adminUser.setPassword(encryptPassword(adminUser.getPassword(), adminUser.getSalt()));
        AssertUtils.isFalse(this.save(adminUser), "保存用户失败！");
        userRoleService.saveUserRole(adminUser.getId(), adminUser.getRoleIdList());
        return adminUser;
    }

    public AdminUserDO updateAdminUser(AdminUserDO adminUser) {
        AssertUtils.isTrue(isPhoneExist(adminUser.getPhone(), adminUser.getId()), "手机号码已存在！");
        if (adminUser.getPassword() != null) {
            adminUser.setSalt(SaltUtils.getSalt(4));
            adminUser.setPassword(encryptPassword(adminUser.getPassword(), adminUser.getSalt()));
        }
        AssertUtils.isFalse(this.updateById(adminUser), "更新用户失败！");
        userRoleService.deleteByUserId(adminUser.getId());
        userRoleService.saveUserRole(adminUser.getId(), adminUser.getRoleIdList());
        // 强制退出被修改的用户使其重新登录
        logout(adminUser.getId());
        return adminUser;
    }

    public AdminUserDO queryDetail(String userId) {
        AdminUserDO adminUser = this.getById(userId);
        if (adminUser != null) {
            adminUser.setRoleIdList(userRoleService.queryRoleIdsByUserId(userId));
            return adminUser;
        }
        return null;
    }

    public LoginVO login(LoginAO loginAdminDTO) {
        AdminUserDO adminUser = this.getFirst(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getPhone, loginAdminDTO.phone()));
        AssertUtils.isNull(adminUser, "用户信息不存在！");
        AssertUtils.isFalse(adminUser.getStatus(), "用户已被禁用！");
        String encryptPassword = encryptPassword(loginAdminDTO.password(), adminUser.getSalt());
        AssertUtils.isFalse(adminUser.getPassword().equals(encryptPassword), "密码错误！");
        if (loginAdminDTO.ip() != null) {
            adminUser.setLoginIp(loginAdminDTO.ip());
            adminUser.updateById();
        }
        StpUtil.login(adminUser.getId());
        return toLoginVO(StpUtil.getTokenInfo());
    }

    public void logout(String userId) {
        StpUtil.logout(userId);
        RoleCache.resetUserRoleCache(userId);
    }

    public Boolean isPhoneExist(String phone, String userId) {

        LambdaQueryWrapper<AdminUserDO> wrapper = new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getPhone, phone);
        if (userId != null) {
            wrapper.ne(AdminUserDO::getId, userId);
        }
        return this.count(wrapper) > 1;
    }

    public AdminUserVO getCurrentAdminInfo(String Id) {
        AdminUserDO adminUser = this.getById(Id);
        AssertUtils.isNull(adminUser, "用户信息不存在！");
        AdminUserVO adminUserVO = SystemConvert.toAdminUserVO(adminUser);
        adminUserVO.setMenus(SystemConvert.toSystemMenuVOList(userRoleService.queryMenusByUserId(adminUser.getId())));
        return adminUserVO;
    }

    public List<String> queryUserRoleIds(String userId) {
        return userRoleService.queryRoleIdsByUserId(userId);
    }


    // 加密密码
    private String encryptPassword(String password, String salt) {
        return SecureUtil.md5(SecureUtil.md5(password) + salt);
    }

    private LoginVO toLoginVO(SaTokenInfo tokenInfo) {
        LoginVO loginVO = new LoginVO();
        loginVO.setTokenName(tokenInfo.getTokenName());
        loginVO.setTokenValue(tokenInfo.getTokenValue());
        loginVO.setIsLogin(tokenInfo.getIsLogin());
        loginVO.setLoginId(tokenInfo.getLoginId());
        loginVO.setLoginType(tokenInfo.getLoginType());
        loginVO.setTokenTimeout(tokenInfo.getTokenTimeout());
        loginVO.setSessionTimeout(tokenInfo.getSessionTimeout());
        loginVO.setTokenSessionTimeout(tokenInfo.getTokenSessionTimeout());
        loginVO.setTokenActivityTimeout(tokenInfo.getTokenActivityTimeout());
        loginVO.setLoginDevice(tokenInfo.getLoginDevice());
        return loginVO;
    }
}
