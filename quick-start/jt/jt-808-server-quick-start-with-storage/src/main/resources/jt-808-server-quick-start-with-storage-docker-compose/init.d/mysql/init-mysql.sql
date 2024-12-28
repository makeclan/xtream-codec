create database if not exists jt_808 default charset utf8mb4;
use jt_808;
drop table if exists jt_808_request_trace_log;
create table jt_808_request_trace_log
(
    id                    varchar(36) not null comment 'uuid'
        primary key,
    received_at           datetime    null comment '收到报文的时间',
    net_type              int         null comment '网络类型; 0:TCP, 1:UDP',
    trace_id              varchar(36) null comment '请求ID',
    terminal_id           varchar(36) null comment '终端手机号',
    message_id            int         null comment '消息ID',
    version               int         null comment '协议版本; 1:2019, 0:2013, -1:2011, -2:unknown',
    flow_id               int         null comment '流水号',
    message_body_length   int         null comment '消息体长度',
    message_body_property int         null comment '消息体属性',
    total_package         int         null comment '总包数',
    current_package_no    int         null comment '当前包序号',
    is_subpackage         tinyint(1)  null comment '是否是子包',
    raw_hex               text        null comment '原始报文',
    escaped_hex           text        null comment '转义之后的报文',
    created_at            datetime    null comment '数据插入时间'
);


drop table if exists jt_808_response_trace_log;
create table jt_808_response_trace_log
(
    id          varchar(36) not null comment 'uuid'
        primary key,
    sent_at     DATETIME comment '报文发送时间',
    net_type    int         null comment '网络类型; 0:TCP, 1:UDP',
    trace_id    varchar(36) null comment '请求ID',
    terminal_id varchar(36) null comment '终端手机号',
    escaped_hex text        null comment '转义之后的报文',
    created_at  datetime    null comment '数据插入时间'
);
