package ${basePackageUrl}.service;
import ${basePackageUrl}.base.service.BaseService;
import ${basePackageUrl}.entity.${entityName};
import com.github.pagehelper.PageInfo;
import ${basePackageUrl}.dto.${entityName}Dto;
public interface ${entityName}Service extends BaseService<${entityName}> {

PageInfo selectForPage(${entityName}Dto ${entityNameLower}Dto);
PageInfo selectForExport(${entityName}Dto ${entityNameLower}Dto);


}
