<#assign daoName = table.className?uncap_first + "Mapper" />
package ${basePackageName}.service;

import ${basePackageName}.model.${table.className};
import ${basePackageName}.mapper.${table.className}Mapper;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author ${author}
 */
@Service
public class ${table.className}WyService extends BaseService<${table.className}> {

    @Resource
    private ${table.className}Mapper ${daoName};

    @Override
    public BaseMapper<${table.className}> getMapper() {
        return ${daoName};
    }
    
}
