package com.sunny.BizCode.dao;

import com.sunny.BizCode.entity.Table;

import java.util.List;

public interface TableDao {
    List<Table> getList(Table table);
}
