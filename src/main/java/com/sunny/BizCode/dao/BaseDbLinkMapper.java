package com.sunny.fims.mapper;
import java.util.List;
import com.sunny.fims.entity.BaseDbLink;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;


public interface BaseDbLinkMapper {
BaseDbLink getObjectById(@Param("id") BigDecimal id);
int deleteByIds(@Param("idList") List<String> idList);
    int updateByPrimaryKeySelective(BaseDbLink  baseDbLink);
    int insert(BaseDbLink  baseDbLink);
    List<BaseDbLink > queryList(BaseDbLink  baseDbLink);
    int count(BaseDbLink  baseDbLink);

    List<BaseDbLink> selectForPage(Page<BaseDbLink> page, @Param("baseDbLink")BaseDbLink baseDbLink);
}
