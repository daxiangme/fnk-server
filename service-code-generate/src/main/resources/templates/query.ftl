package ${package.Parent}.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* ${table.comment!} 查询对象。
*
* @author ${author}
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entity?replace("DO", "")}Query extends SplitPageDTO {

}
