<#assign serviceName = table.className?uncap_first + "WyService" />
package ${basePackageName}.web.api;

import ${basePackageName}.model.${table.className};
import ${basePackageName}.service.${table.className}WyService;
import com.learnyeai.learnai.support.ApiBaseController;
import com.learnyeai.learnai.support.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ${author}
 */
@Component
public class ${table.className}Action extends ApiBaseController<${table.className}> {
    public static final String BASE_URL = "/${table.className}/";

    @Autowired
    private ${table.className}WyService ${serviceName};

    @Override
    protected BaseService<${table.className}> getBaseService() {
        return ${serviceName};
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }
}
