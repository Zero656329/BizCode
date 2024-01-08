package com.sunny.BizCode.entity;

import lombok.Data;

@Data
public class Node {
    //节点id
    private String id;
    //节点名称
    private String name;
    //操作类型，查询 ，新增，删除，编辑
    private String type;
    //注释
    private String label;
    //


}
