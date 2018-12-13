package ${basePackageName}.api.client;

import ${basePackageName}.api.vo.${table.className}Vo;

import com.learnyeai.core.support.BaseFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * ${table.remarks}
 *
 * @author ${author}
 */
@FeignClient(name = "${client_service_name}", path="${client_adminPath}/${table.className}")
public interface ${table.className}Client extends BaseFeignClient<${table.className}Vo> {
}
