package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemMailAccountQuery;
import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;
import com.fnk.app.system.biz.dal.mapper.SystemMailAccountMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮箱账号服务。
 *
 * @author Enigma
 */
@Service
public class SystemMailAccountService extends BaseService<SystemMailAccountMapper, SystemMailAccountDO> {

    public PageVO<SystemMailAccountDO> page(SystemMailAccountQuery query) {
        return this.basicPage(query, SystemMailAccountDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getMail()), SystemMailAccountDO::getMail, query.getMail())
                .like(StrUtil.isNotBlank(query.getHost()), SystemMailAccountDO::getHost, query.getHost())
                .eq(query.getStatus() != null, SystemMailAccountDO::getStatus, query.getStatus()));
    }

    public List<SystemMailAccountDO> listEnabled() {
        return this.list(new LambdaQueryWrapper<SystemMailAccountDO>()
                .eq(SystemMailAccountDO::getStatus, true)
                .orderByDesc(SystemMailAccountDO::getCreateTime));
    }

    @Override
    public SystemMailAccountDO create(SystemMailAccountDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemMailAccountDO update(String id, SystemMailAccountDO req) {
        req.setId(id);
        SystemMailAccountDO old = this.detail(id);
        if (StrUtil.isBlank(req.getPassword())) {
            req.setPassword(old.getPassword());
        }
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    private void normalizeAndValidate(SystemMailAccountDO account) {
        AssertUtils.isNull(account, "邮箱账号不能为空");
        AssertUtils.isBlank(account.getMail(), "邮箱地址不能为空");
        AssertUtils.isBlank(account.getUsername(), "SMTP 用户名不能为空");
        AssertUtils.isBlank(account.getHost(), "SMTP 主机不能为空");
        AssertUtils.isNull(account.getPort(), "SMTP 端口不能为空");
        if (account.getSslEnable() == null) {
            account.setSslEnable(false);
        }
        if (account.getStarttlsEnable() == null) {
            account.setStarttlsEnable(false);
        }
        if (account.getStatus() == null) {
            account.setStatus(true);
        }
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemMailAccountDO>()
                .eq(SystemMailAccountDO::getMail, account.getMail())
                .ne(StrUtil.isNotBlank(account.getId()), SystemMailAccountDO::getId, account.getId())) > 0;
        AssertUtils.isTrue(duplicated, "邮箱地址不能重复");
    }
}
