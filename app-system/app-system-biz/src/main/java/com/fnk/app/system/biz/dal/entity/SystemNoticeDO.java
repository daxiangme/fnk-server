package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 通知公告。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_notice")
@Schema(name = "SystemNoticeDO", description = "通知公告")
public class SystemNoticeDO extends BaseEntity<SystemNoticeDO> {
    private String title;

    @TableField("notice_type")
    private String noticeType;

    private String content;

    @TableField("publish_status")
    private Boolean publishStatus;

    @TableField("publish_time")
    private Date publishTime;
}
