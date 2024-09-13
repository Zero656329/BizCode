package com.sunny.BizCode.Service;

import com.sunny.BizCode.entity.Flow;
import com.sunny.BizCode.entity.Table;

import java.util.List;

public interface GeneratorService {
   Integer generator(Table table) throws Exception;
   Integer generator(Flow flow) throws Exception;
   Integer getSql(Table table) throws Exception;
}
