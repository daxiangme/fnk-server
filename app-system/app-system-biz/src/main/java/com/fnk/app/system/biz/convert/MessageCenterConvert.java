package com.fnk.app.system.biz.convert;

import com.fnk.app.system.api.model.request.SystemMailAccountAO;
import com.fnk.app.system.api.model.request.SystemMailTemplateAO;
import com.fnk.app.system.api.model.request.SystemNotifyTemplateAO;
import com.fnk.app.system.api.model.request.SystemSmsChannelAO;
import com.fnk.app.system.api.model.request.SystemSmsTemplateAO;
import com.fnk.app.system.api.model.response.SystemMailAccountVO;
import com.fnk.app.system.api.model.response.SystemMailLogVO;
import com.fnk.app.system.api.model.response.SystemMailTemplateVO;
import com.fnk.app.system.api.model.response.SystemNotifyMessageVO;
import com.fnk.app.system.api.model.response.SystemNotifyTemplateVO;
import com.fnk.app.system.api.model.response.SystemSmsChannelVO;
import com.fnk.app.system.api.model.response.SystemSmsLogVO;
import com.fnk.app.system.api.model.response.SystemSmsTemplateVO;
import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;
import com.fnk.app.system.biz.dal.entity.SystemMailLogDO;
import com.fnk.app.system.biz.dal.entity.SystemMailTemplateDO;
import com.fnk.app.system.biz.dal.entity.SystemNotifyMessageDO;
import com.fnk.app.system.biz.dal.entity.SystemNotifyTemplateDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsChannelDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsLogDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsTemplateDO;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.common.db.vo.PageVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 消息中心对象转换。
 *
 * @author Enigma
 */
public class MessageCenterConvert {
    private MessageCenterConvert() {
    }

    public static SystemSmsChannelDO toSmsChannelDO(SystemSmsChannelAO source) {
        SystemSmsChannelDO target = new SystemSmsChannelDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemSmsTemplateDO toSmsTemplateDO(SystemSmsTemplateAO source) {
        SystemSmsTemplateDO target = new SystemSmsTemplateDO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.write(MessageJsonUtils.normalizeStringList(source.getParams())));
        return target;
    }

    public static SystemMailAccountDO toMailAccountDO(SystemMailAccountAO source) {
        SystemMailAccountDO target = new SystemMailAccountDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemMailTemplateDO toMailTemplateDO(SystemMailTemplateAO source) {
        SystemMailTemplateDO target = new SystemMailTemplateDO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.write(MessageJsonUtils.normalizeStringList(source.getParams())));
        return target;
    }

    public static SystemNotifyTemplateDO toNotifyTemplateDO(SystemNotifyTemplateAO source) {
        SystemNotifyTemplateDO target = new SystemNotifyTemplateDO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.write(MessageJsonUtils.normalizeStringList(source.getParams())));
        return target;
    }

    public static SystemSmsChannelVO toSmsChannelVO(SystemSmsChannelDO source) {
        if (source == null) {
            return null;
        }
        SystemSmsChannelVO target = new SystemSmsChannelVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemSmsTemplateVO toSmsTemplateVO(SystemSmsTemplateDO source) {
        if (source == null) {
            return null;
        }
        SystemSmsTemplateVO target = new SystemSmsTemplateVO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.readStringList(source.getParams()));
        return target;
    }

    public static SystemSmsLogVO toSmsLogVO(SystemSmsLogDO source) {
        if (source == null) {
            return null;
        }
        SystemSmsLogVO target = new SystemSmsLogVO();
        BeanUtils.copyProperties(source, target, "templateParams");
        target.setTemplateParams(MessageJsonUtils.readStringMap(source.getTemplateParams()));
        return target;
    }

    public static SystemMailAccountVO toMailAccountVO(SystemMailAccountDO source) {
        if (source == null) {
            return null;
        }
        SystemMailAccountVO target = new SystemMailAccountVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemMailTemplateVO toMailTemplateVO(SystemMailTemplateDO source) {
        if (source == null) {
            return null;
        }
        SystemMailTemplateVO target = new SystemMailTemplateVO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.readStringList(source.getParams()));
        return target;
    }

    public static SystemMailLogVO toMailLogVO(SystemMailLogDO source) {
        if (source == null) {
            return null;
        }
        SystemMailLogVO target = new SystemMailLogVO();
        BeanUtils.copyProperties(source, target, "templateParams");
        target.setTemplateParams(MessageJsonUtils.readStringMap(source.getTemplateParams()));
        return target;
    }

    public static SystemNotifyTemplateVO toNotifyTemplateVO(SystemNotifyTemplateDO source) {
        if (source == null) {
            return null;
        }
        SystemNotifyTemplateVO target = new SystemNotifyTemplateVO();
        BeanUtils.copyProperties(source, target, "params");
        target.setParams(MessageJsonUtils.readStringList(source.getParams()));
        return target;
    }

    public static SystemNotifyMessageVO toNotifyMessageVO(SystemNotifyMessageDO source) {
        if (source == null) {
            return null;
        }
        SystemNotifyMessageVO target = new SystemNotifyMessageVO();
        BeanUtils.copyProperties(source, target, "templateParams");
        target.setTemplateParams(MessageJsonUtils.readStringMap(source.getTemplateParams()));
        return target;
    }

    public static PageVO<SystemSmsChannelVO> toSmsChannelPage(PageVO<SystemSmsChannelDO> source) {
        PageVO<SystemSmsChannelVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toSmsChannelVO).toList());
        return target;
    }

    public static PageVO<SystemSmsTemplateVO> toSmsTemplatePage(PageVO<SystemSmsTemplateDO> source) {
        PageVO<SystemSmsTemplateVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toSmsTemplateVO).toList());
        return target;
    }

    public static PageVO<SystemSmsLogVO> toSmsLogPage(PageVO<SystemSmsLogDO> source) {
        PageVO<SystemSmsLogVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toSmsLogVO).toList());
        return target;
    }

    public static PageVO<SystemMailAccountVO> toMailAccountPage(PageVO<SystemMailAccountDO> source) {
        PageVO<SystemMailAccountVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toMailAccountVO).toList());
        return target;
    }

    public static PageVO<SystemMailTemplateVO> toMailTemplatePage(PageVO<SystemMailTemplateDO> source) {
        PageVO<SystemMailTemplateVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toMailTemplateVO).toList());
        return target;
    }

    public static PageVO<SystemMailLogVO> toMailLogPage(PageVO<SystemMailLogDO> source) {
        PageVO<SystemMailLogVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toMailLogVO).toList());
        return target;
    }

    public static PageVO<SystemNotifyTemplateVO> toNotifyTemplatePage(PageVO<SystemNotifyTemplateDO> source) {
        PageVO<SystemNotifyTemplateVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toNotifyTemplateVO).toList());
        return target;
    }

    public static PageVO<SystemNotifyMessageVO> toNotifyMessagePage(PageVO<SystemNotifyMessageDO> source) {
        PageVO<SystemNotifyMessageVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(MessageCenterConvert::toNotifyMessageVO).toList());
        return target;
    }

    public static List<SystemSmsChannelVO> toSmsChannelVOList(List<SystemSmsChannelDO> source) {
        return source.stream().map(MessageCenterConvert::toSmsChannelVO).toList();
    }

    public static List<SystemMailAccountVO> toMailAccountVOList(List<SystemMailAccountDO> source) {
        return source.stream().map(MessageCenterConvert::toMailAccountVO).toList();
    }

    public static List<SystemNotifyMessageVO> toNotifyMessageVOList(List<SystemNotifyMessageDO> source) {
        return source.stream().map(MessageCenterConvert::toNotifyMessageVO).toList();
    }
}
