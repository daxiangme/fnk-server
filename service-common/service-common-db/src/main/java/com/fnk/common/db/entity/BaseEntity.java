package com.fnk.common.db.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 公共父级实体<br>
 * 此类已继承MyBatis-Plus，即实现已实现ActiveRecord模式，本身拥有强大的CURD功能 <br>
 * 模式介绍：https://baomidou.com/pages/49cc81/#activerecord-模式 <br>
 * 例如：<br>
 * User user = new User();<br>
 * user.insert();<br>
 * user.selectAll();<br>
 * user.updateById();<br>
 * user.deleteById();<br>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T extends Model<T>> extends Model<T> {

    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(name = "id", description = "主键ID")
    private String id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createTime", description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "updatedTime", description = "最后更新时间")
    private Date updateTime;

    @TableLogic
    @Schema(name = "deleted",description = "删除标记")
    @JsonIgnore
    private short deleted;
}
