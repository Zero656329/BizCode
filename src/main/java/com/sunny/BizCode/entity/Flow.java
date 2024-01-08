package com.sunny.BizCode.entity;

import lombok.Data;

import java.util.List;

@Data
public class Flow {
    private String cname;
    private List<Node> nodeList;
    private List<Line> linesList;

}
