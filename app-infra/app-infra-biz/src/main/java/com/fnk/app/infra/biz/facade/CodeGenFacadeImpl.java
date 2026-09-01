package com.fnk.app.infra.biz.facade;

import com.fnk.app.infra.api.facade.CodeGenFacade;
import com.fnk.app.infra.api.model.query.CodeGenTableQuery;
import com.fnk.app.infra.api.model.request.CodeGenFieldBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenRelationBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenTableImportAO;
import com.fnk.app.infra.api.model.request.CodeGenTableUpdateAO;
import com.fnk.app.infra.api.model.response.CodeGenFieldVO;
import com.fnk.app.infra.api.model.response.CodeGenPreviewVO;
import com.fnk.app.infra.api.model.response.CodeGenRelationVO;
import com.fnk.app.infra.api.model.response.CodeGenTableVO;
import com.fnk.app.infra.api.model.response.DatabaseTableVO;
import com.fnk.app.infra.biz.convert.InfraConvert;
import com.fnk.app.infra.biz.service.CodeGenFieldService;
import com.fnk.app.infra.biz.service.CodeGenPreviewService;
import com.fnk.app.infra.biz.service.CodeGenRelationService;
import com.fnk.app.infra.biz.service.CodeGenTableService;
import com.fnk.common.db.vo.PageVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代码生成 facade 实现。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class CodeGenFacadeImpl implements CodeGenFacade {
    private final CodeGenTableService tableService;
    private final CodeGenFieldService fieldService;
    private final CodeGenRelationService relationService;
    private final CodeGenPreviewService previewService;

    @Override
    public List<DatabaseTableVO> databaseTables(String tableName, boolean excludeImported) {
        return tableService.listDatabaseTables(tableName, excludeImported);
    }

    @Override
    public PageVO<CodeGenTableVO> page(CodeGenTableQuery query) {
        return InfraConvert.toCodeGenTablePage(tableService.page(query));
    }

    @Override
    public List<CodeGenTableVO> importTables(CodeGenTableImportAO req) {
        return tableService.importTables(req).stream()
                .map(InfraConvert::toCodeGenTableVO)
                .toList();
    }

    @Override
    public CodeGenTableVO detail(String id) {
        return InfraConvert.toCodeGenTableVO(tableService.detail(id));
    }

    @Override
    public CodeGenTableVO updateConfig(String id, CodeGenTableUpdateAO req) {
        return InfraConvert.toCodeGenTableVO(tableService.updateConfig(id, req));
    }

    @Override
    public List<CodeGenFieldVO> syncFields(String id) {
        tableService.syncFields(id);
        return fields(id);
    }

    @Override
    public List<CodeGenFieldVO> fields(String id) {
        return InfraConvert.toCodeGenFieldVOList(fieldService.listByTableId(id));
    }

    @Override
    public List<CodeGenFieldVO> updateFields(String id, CodeGenFieldBatchUpdateAO req) {
        return InfraConvert.toCodeGenFieldVOList(fieldService.batchUpdate(id, req));
    }

    @Override
    public List<CodeGenRelationVO> relations(String id) {
        return InfraConvert.toCodeGenRelationVOList(relationService.listByTableId(id));
    }

    @Override
    public List<CodeGenRelationVO> analyzeRelations(String id) {
        return InfraConvert.toCodeGenRelationVOList(relationService.analyzeAndSave(id));
    }

    @Override
    public List<CodeGenRelationVO> updateRelations(String id, CodeGenRelationBatchUpdateAO req) {
        return InfraConvert.toCodeGenRelationVOList(relationService.batchUpdate(id, req));
    }

    @Override
    public CodeGenPreviewVO preview(String id) {
        return previewService.preview(id);
    }

    @Override
    public byte[] download(String id) {
        return previewService.downloadZip(id);
    }
}
