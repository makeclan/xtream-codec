package io.github.hylexus.xtream.debug.ext.jt808.service.impl;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt808.boot.properties.XtreamJt808ServerProperties;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.AlarmIdentifier;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage64;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMsg9208;
import io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamResponse;
import io.github.hylexus.xtream.debug.ext.jt808.service.DemoLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class DemoLocationServiceImpl implements DemoLocationService {

    private static final Logger log = LoggerFactory.getLogger(DemoLocationServiceImpl.class);
    private final XtreamJt808ServerProperties serverProperties;

    public DemoLocationServiceImpl(XtreamJt808ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override
    public Mono<Byte> processLocationMessage(XtreamResponse response, Jt808Session session, BuiltinMessage0200 body) {
        // 处理其他消息属性
        // ...

        // 处理附加项列表
        final Map<Integer, Object> extraItems = body.getExtraItems();
        if (!CollectionUtils.isEmpty(extraItems)) {
            this.processExtraItemList(response, session, extraItems);
        }
        // 成功
        return Mono.just((byte) 0);
    }

    private void processExtraItemList(XtreamResponse response, Jt808Session session, Map<Integer, Object> groupById) {
        // 苏标: 高级驾驶辅助系统报警信息，定义见表 4-15
        // ADAS模块视频通道
        this.process0x64IfNecessary(response, session, (BuiltinMessage64) groupById.get(0x64));
    }

    private void process0x64IfNecessary(XtreamResponse response, Jt808Session session, BuiltinMessage64 message64) {
        if (message64 == null) {
            return;
        }
        log.info("ExtraItem-64==>AlarmIdentifier: {}", message64.getAlarmIdentifier());
        this.doSendMsg9208(response, session, message64.getAlarmIdentifier());
    }

    private void doSendMsg9208(XtreamResponse response, Jt808Session session, AlarmIdentifier alarmIdentifier) {
        final BuiltinMsg9208 msg9208 = new BuiltinMsg9208();

        final String serverIp = "...";
        msg9208.setAttachmentServerIp(serverIp);
        msg9208.setAttachmentServerIpLength((short) serverIp.length());
        msg9208.setAttachmentServerPortTcp(serverProperties.getTcpAttachmentServer().getPort());
        msg9208.setAttachmentServerPortUdp(serverProperties.getUdpAttachmentServer().getPort());
        msg9208.setAlarmIdentifier(alarmIdentifier);
        msg9208.setAlarmNo(XtreamBytes.randomString(32));
        msg9208.setReservedByte16("0000000000000000");

    }

}
