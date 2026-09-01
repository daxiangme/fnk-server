package com.fnk.app.infra.api.facade;

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
import com.fnk.common.db.vo.PageVO;

import java.util.List;

/**
 * 代码生成对外能力。
 *
 * @author Enigma
 */
public interface CodeGenFacade {
    List<DatabaseTableVO> databaseTables(String tableName, boolean excludeImported);

    PageVO<CodeGenTableVO> page(CodeGenTableQuery query);

    List<CodeGenTableVO> importTables(CodeGenTableImportAO req);

    CodeGenTableVO detail(String id);

    CodeGenTableVO updateConfig(String id, CodeGenTableUpdateAO req);

    List<CodeGenFieldVO> syncFields(String id);

    List<CodeGenFieldVO> fields(String id);

    List<CodeGenFieldVO> updateFields(String id, CodeGenFieldBatchUpdateAO req);

    List<CodeGenRelationVO> relations(String id);

    List<CodeGenRelationVO> analyzeRelations(String id);

    List<CodeGenRelationVO> updateRelations(String id, CodeGenRelationBatchUpdateAO req);

    CodeGenPreviewVO preview(String id);

    byte[] download(String id);
}
