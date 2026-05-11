package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import com.fnk.common.db.impl.BaseService;
import org.springframework.stereotype.Service;

/**
* ${table.comment!} 服务层
*
* @author ${author}
*/
@Service
public class ${table.serviceName} extends BaseService<${table.mapperName}, ${entity}> {

}
