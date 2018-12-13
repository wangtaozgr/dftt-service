<#assign serviceName = table.className?uncap_first + "WyService" />
package ${basePackageName}.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import ${basePackageName}.model.${table.className};
import ${basePackageName}.service.${table.className}WyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ${author}
 */
@RestController
@RequestMapping("${r'${adminPath}'}" + ${table.className}Controller.BASE_URL)
public class ${table.className}Controller extends BaseController<${table.className}> {
    public static final String BASE_URL = "/${table.className}/";

    @Autowired
    private ${table.className}WyService ${serviceName};

    @Override
    protected BaseService<${table.className}> getService() {
        return ${serviceName};
    }
}
