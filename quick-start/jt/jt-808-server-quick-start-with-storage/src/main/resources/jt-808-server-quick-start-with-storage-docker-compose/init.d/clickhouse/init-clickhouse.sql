create database jt_808;
use jt_808;

drop table if exists jt_808_request_trace_log;
create table jt_808_request_trace_log
(
    id                    UUID,
    received_at           DATETIME comment '收到报文的时间',
    net_type              Enum8('TCP' = 0, 'UDP' = 1) comment '网络类型',
    trace_id              String comment '请求ID',
    terminal_id           String comment '终端手机号',
    message_id            Int32 comment '消息ID',
    version               Enum16('unknown' = -2, 'v2011' = -1, 'v2013' = 0, 'v2019' = 1) comment '协议版本',
    flow_id               Int32 comment '流水号',
    message_body_length   Int32 comment '消息体长度',
    message_body_property Int32 comment '消息体属性',
    total_package         Int32 comment '总包数',
    current_package_no    Int32 comment '当前包序号',
    is_subpackage         Bool comment '是否是子包',
    raw_hex               String comment '原始报文',
    escaped_hex           String comment '转义之后的报文',
    created_at            DATETIME comment '数据插入时间'
)
    engine = MergeTree PARTITION BY toYYYYMMDD(received_at)
        ORDER BY received_at
        TTL toDateTime(received_at) + toIntervalMonth(3)
        SETTINGS index_granularity = 8192;


drop table if exists jt_808_response_trace_log;
create table jt_808_response_trace_log
(
    id          UUID,
    sent_at     DATETIME comment '报文发送时间',
    net_type    Enum8('TCP' = 0, 'UDP' = 1) comment '网络类型',
    trace_id    String comment '请求ID',
    terminal_id String comment '终端手机号',
    escaped_hex String comment '转义之后的报文',
    created_at  DATETIME comment '数据插入时间'
)
    engine = MergeTree PARTITION BY toYYYYMMDD(sent_at)
        ORDER BY sent_at
        TTL toDateTime(sent_at) + toIntervalMonth(3)
        SETTINGS index_granularity = 8192;

