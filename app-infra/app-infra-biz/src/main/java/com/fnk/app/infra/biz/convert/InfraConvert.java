package com.fnk.app.infra.biz.convert;

import com.fnk.app.infra.api.model.response.CodeGenFieldVO;
import com.fnk.app.infra.api.model.response.CodeGenRelationVO;
import com.fnk.app.infra.api.model.response.CodeGenTableVO;
import com.fnk.app.infra.api.model.response.InfraConfigVO;
import com.fnk.app.infra.api.model.response.InfraFileConfigVO;
import com.fnk.app.infra.api.model.response.InfraFileVO;
import com.fnk.app.infra.biz.dal.entity.CodeGenFieldDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenRelationDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenTableDO;
import com.fnk.app.infra.biz.dal.entity.InfraConfigDO;
import com.fnk.app.infra.biz.dal.entity.InfraFileConfigDO;
import com.fnk.app.infra.biz.dal.entity.InfraFileDO;
import com.fnk.common.db.vo.PageVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 基础服务对象转换。
 *
 * @author Enigma
 */
public final class InfraConvert {
    private InfraConvert() {
    }

    public static CodeGenTableVO toCodeGenTableVO(CodeGenTableDO source) {
        if (source == null) {
            return null;
        }
        CodeGenTableVO target = new CodeGenTableVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static PageVO<CodeGenTableVO> toCodeGenTablePage(PageVO<CodeGenTableDO> source) {
        PageVO<CodeGenTableVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(toCodeGenTableVOList(source.getRecords()));
        return target;
    }

    public static List<CodeGenTableVO> toCodeGenTableVOList(List<CodeGenTableDO> source) {
        return source.stream().map(InfraConvert::toCodeGenTableVO).toList();
    }

    public static CodeGenFieldVO toCodeGenFieldVO(CodeGenFieldDO source) {
        if (source == null) {
            return null;
        }
        CodeGenFieldVO target = new CodeGenFieldVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<CodeGenFieldVO> toCodeGenFieldVOList(List<CodeGenFieldDO> source) {
        return source.stream().map(InfraConvert::toCodeGenFieldVO).toList();
    }

    public static CodeGenRelationVO toCodeGenRelationVO(CodeGenRelationDO source) {
        if (source == null) {
            return null;
        }
        CodeGenRelationVO target = new CodeGenRelationVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<CodeGenRelationVO> toCodeGenRelationVOList(List<CodeGenRelationDO> source) {
        return source.stream().map(InfraConvert::toCodeGenRelationVO).toList();
    }

    public static InfraConfigVO toInfraConfigVO(InfraConfigDO source) {
        if (source == null) {
            return null;
        }
        InfraConfigVO target = new InfraConfigVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static PageVO<InfraConfigVO> toInfraConfigPage(PageVO<InfraConfigDO> source) {
        PageVO<InfraConfigVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(InfraConvert::toInfraConfigVO).toList());
        return target;
    }

    public static InfraFileConfigVO toInfraFileConfigVO(InfraFileConfigDO source) {
        if (source == null) {
            return null;
        }
        InfraFileConfigVO target = new InfraFileConfigVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static PageVO<InfraFileConfigVO> toInfraFileConfigPage(PageVO<InfraFileConfigDO> source) {
        PageVO<InfraFileConfigVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(InfraConvert::toInfraFileConfigVO).toList());
        return target;
    }

    public static InfraFileVO toInfraFileVO(InfraFileDO source) {
        if (source == null) {
            return null;
        }
        InfraFileVO target = new InfraFileVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static PageVO<InfraFileVO> toInfraFilePage(PageVO<InfraFileDO> source) {
        PageVO<InfraFileVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(InfraConvert::toInfraFileVO).toList());
        return target;
    }
}
