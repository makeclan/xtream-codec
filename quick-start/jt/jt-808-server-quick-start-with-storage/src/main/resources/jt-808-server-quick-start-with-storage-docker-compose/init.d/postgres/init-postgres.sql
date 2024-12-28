-- create database jt_808;
create table jt_808_request_trace_log
(
    id                    varchar(36) not null
        primary key,
    received_at           timestamp,
    net_type              integer,
    trace_id              varchar(36),
    terminal_id           varchar(36),
    message_id            integer,
    version               integer,
    flow_id               integer,
    message_body_length   integer,
    message_body_property integer,
    total_package         integer,
    current_package_no    integer,
    is_subpackage         boolean,
    raw_hex               text,
    escaped_hex           text,
    created_at            timestamp
);

alter table jt_808_request_trace_log
    owner to jt;

comment on column jt_808_request_trace_log.id is 'uuid';

comment on column jt_808_request_trace_log.received_at is '收到报文的时间';

comment on column jt_808_request_trace_log.net_type is '网络类型; 0:TCP, 1:UDP';

comment on column jt_808_request_trace_log.trace_id is '请求ID';

comment on column jt_808_request_trace_log.terminal_id is '终端手机号';

comment on column jt_808_request_trace_log.message_id is '消息ID';

comment on column jt_808_request_trace_log.version is '协议版本; 1:2019, 0:2013, -1:2011, -2:unknown';

comment on column jt_808_request_trace_log.flow_id is '流水号';

comment on column jt_808_request_trace_log.message_body_length is '消息体长度';

comment on column jt_808_request_trace_log.message_body_property is '消息体属性';

comment on column jt_808_request_trace_log.total_package is '总包数';

comment on column jt_808_request_trace_log.current_package_no is '当前包序号';

comment on column jt_808_request_trace_log.is_subpackage is '是否是子包';

comment on column jt_808_request_trace_log.raw_hex is '原始报文';

comment on column jt_808_request_trace_log.escaped_hex is '转义之后的报文';

comment on column jt_808_request_trace_log.created_at is '数据插入时间';

create table jt_808_response_trace_log
(
    id          varchar(36) not null
        primary key,
    sent_at     timestamp,
    net_type    integer,
    trace_id    varchar(36),
    terminal_id varchar(36),
    escaped_hex text,
    created_at  timestamp
);
alter table jt_808_response_trace_log
    owner to jt;

comment on column jt_808_response_trace_log.id is 'uuid';

comment on column jt_808_response_trace_log.sent_at is '报文发送时间';

comment on column jt_808_response_trace_log.net_type is '网络类型; 0:TCP, 1:UDP';

comment on column jt_808_response_trace_log.trace_id is '请求ID';

comment on column jt_808_response_trace_log.terminal_id is '终端手机号';

comment on column jt_808_response_trace_log.escaped_hex is '转义之后的报文';

comment on column jt_808_response_trace_log.created_at is '数据插入时间';
