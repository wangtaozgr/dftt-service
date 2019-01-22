<#assign clientName = table.className?uncap_first + "Client" />
package ${basePackageName}.api.service;

import ${basePackageName}.api.vo.${table.className}Vo;
import ${basePackageName}.api.client.${table.className}Client;

import com.learnyeai.core.support.BaseFeignClient;
import com.learnyeai.learnai.support.BaseClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 *
 * @author ${author}
 */
@Service
public class ${table.className}ApiService extends BaseClientService<${table.className}Vo> {
    @Autowired
    private ${table.className}Client ${clientName};

    @Override
    public BaseFeignClient<${table.className}Vo> getFeignClient() {
        return ${clientName};
    }

    @Override
    public Object getId(Map params) {
        String idKey = "${table.primaryField.name}";
        if(params.containsKey(idKey)){
        return params.get(idKey);
        }
        return null;
    }
}
