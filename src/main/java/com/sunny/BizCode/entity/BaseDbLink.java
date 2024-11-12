package com.sunny.fims.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

@Data
@ApiModel(value = "")
public class BaseDbLink implements Serializable {

private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "自然主键")
        private String fId;
        @ApiModelProperty(value = "连接名称")
        private String fFullName;
        @ApiModelProperty(value = "连接驱动")
        private String fDbType;
        @ApiModelProperty(value = "主机地址")
        private String fHost;
        @ApiModelProperty(value = "端口")
        private BigDecimal fPort;
        @ApiModelProperty(value = "用户")
        private String fUserName;
        @ApiModelProperty(value = "密码")
        private String fPassword;
        @ApiModelProperty(value = "服务名称")
        private String fServiceName;
        @ApiModelProperty(value = "描述或说明")
        private String fDescription;
        @ApiModelProperty(value = "模式")
        private String fDbSchema;
        @ApiModelProperty(value = "表空间")
        private String fTableSpace;
        @ApiModelProperty(value = "oracle连接参数")
        private String fOracleParam;
        @ApiModelProperty(value = "Oracle扩展开关 1:开启 0:关闭")
        private BigDecimal fOracleExtend;
        @ApiModelProperty(value = "排序")
        private BigDecimal fSortCode;
        @ApiModelProperty(value = "创建时间")
        private String fCreatorTime;
        @ApiModelProperty(value = "创建用户")
        private String fCreatorUserId;
        @ApiModelProperty(value = "修改时间")
        private String fLastModifyTime;
        @ApiModelProperty(value = "修改用户")
        private String fLastModifyUserId;
        @ApiModelProperty(value = "删除时间")
        private String fDeleteTime;
        @ApiModelProperty(value = "删除用户")
        private String fDeleteUserId;
        @ApiModelProperty(value = "删除标志")
        private BigDecimal fDeleteMark;
        @ApiModelProperty(value = "租户id")
        private String fTenantId;
    @ApiModelProperty(value = "ids")
    private String ids;
}
