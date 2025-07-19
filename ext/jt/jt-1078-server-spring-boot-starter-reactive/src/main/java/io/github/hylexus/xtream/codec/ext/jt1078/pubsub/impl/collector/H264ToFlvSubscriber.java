/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.collector;

import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.audio.AudioPackage;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.audio.PcmToMp3Encoder;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.audio.impl.mp3.DefaultPcmToMp3Encoder;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.FlvEncoder;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.FlvTagHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.flv.tag.AudioFlvTag;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.Jt1078Subscription;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.DefaultJt1078SubscriptionType;
import io.github.hylexus.xtream.codec.ext.jt1078.pubsub.impl.H264Jt1078SubscriberCreator;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;

/**
 * @author hylexus
 * @see <a href="https://gitee.com/ldming/JT1078">https://gitee.com/ldming/JT1078</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
public class H264ToFlvSubscriber extends AbstractInternalSubscriber {

    private static final Logger log = LoggerFactory.getLogger(H264ToFlvSubscriber.class);

    private boolean flvHeaderSent;
    private boolean lastIFrameSent;

    // 每个 subscriber 从零开始
    private long audioTimestamp = 0;
    private long videoTimestamp = 0;
    private long prevAudioTimestamp = 0;
    private long prevVideoTimestamp = 0;

    private final H264Jt1078SubscriberCreator subscriberCreator;
    // 每个 subscriber 需要单独的 mp3 编码器
    private final PcmToMp3Encoder pcmToMp3Encoder = new DefaultPcmToMp3Encoder();
    private final FlvEncoder flvEncoder;

    public H264ToFlvSubscriber(String id, H264Jt1078SubscriberCreator creator, FlvEncoder flvEncoder, FluxSink<Jt1078Subscription> sink) {
        super(id, creator, LocalDateTime.now(), sink);
        this.subscriberCreator = creator;
        this.flvEncoder = flvEncoder;
    }

    public synchronized void sendFlvHeader(byte[] basicFrame) {
        // log.info("basicFrame ======== ");
        final byte[] flvHeader = this.flvEncoder.createFlvHeader(this.subscriberCreator.hasVideo(), subscriberCreator.hasAudio()).toBytes(true);
        // flv-header
        this.sink.next(Jt1078Subscription.fromByteArray(DefaultJt1078SubscriptionType.FLV, flvHeader));
        // sps + pps
        this.sink.next(Jt1078Subscription.fromByteArray(DefaultJt1078SubscriptionType.FLV, basicFrame));
        this.flvHeaderSent = true;
    }

    public void sendIFrameData(Long timestamp, byte[] iframeData) {
        // log.info("lastIFrame ======== ");
        if (this.prevVideoTimestamp == 0) {
            this.prevVideoTimestamp = timestamp;
        }
        resetFlvVideoTagDts(iframeData, this.videoTimestamp);
        this.sink().next(Jt1078Subscription.fromByteArray(DefaultJt1078SubscriptionType.FLV, iframeData));
        this.lastIFrameSent = true;
    }

    public void sendAudioData(long timestamp, AudioPackage audioPackage) {
        if (this.isFlvHeaderPending()) {
            return;
        }
        final AudioPackage mp3Package = this.pcmToMp3Encoder.encode(audioPackage);
        if (mp3Package.isEmpty()) {
            mp3Package.close();
            return;
        }
        final AudioFlvTag.AudioFlvTagHeader flvTagHeader = AudioFlvTag.newTagHeaderBuilder()
                .soundFormat(AudioFlvTag.AudioSoundFormat.MP3)
                .soundRate(AudioFlvTag.AudioSoundRate.RATE_5_5_KHZ)
                .soundSize(AudioFlvTag.AudioSoundSize.SND_BIT_16)
                .soundType(AudioFlvTag.AudioSoundType.MONO)
                .aacPacketType(null)
                .build();

        final ByteBuf flvAudioTag = this.flvEncoder.encodeAudioTag((int) this.audioTimestamp, flvTagHeader, mp3Package.payload());
        try {
            if (this.prevAudioTimestamp == 0) {
                this.prevAudioTimestamp = timestamp;
            }
            // log.info("audio-dts: {}", audioTimestamp);

            audioTimestamp += (timestamp - prevAudioTimestamp);
            prevAudioTimestamp = timestamp;

            this.sink().next(Jt1078Subscription.fromByteArray(DefaultJt1078SubscriptionType.FLV, XtreamBytes.getBytes(flvAudioTag)));
        } finally {
            XtreamBytes.releaseBuf(flvAudioTag);
            mp3Package.close();
        }
    }

    public void sendVideoData(long timestamp, byte[] videoData) {
        if (this.prevVideoTimestamp == 0) {
            this.prevVideoTimestamp = timestamp;
        }

        // 1. flv-header + sps + pps
        if (this.isFlvHeaderPending()) {
            final ByteBuf basicFrame = this.flvEncoder.getFlvBasicFrame();
            if (basicFrame != null) {
                this.sendFlvHeader(XtreamBytes.getBytes(basicFrame));
            }
        }

        // 2. lastIFrame
        if (this.isLastIFramePending()) {
            final ByteBuf lastIFrame = this.flvEncoder.getLastIFrame();
            if (lastIFrame != null) {
                final byte[] iframeData = XtreamBytes.getBytes(lastIFrame);
                this.sendIFrameData(timestamp, iframeData);
            }
        }

        // 确保先把 关键帧 发送出去(中途订阅)
        if (this.isLastIFramePending()) {
            return;
        }
        // 确保先把 flvHeader 发送出去(中途订阅)
        if (this.isFlvHeaderPending()) {
            return;
        }

        // log.info("video-dts: {}", videoTimestamp);

        resetFlvVideoTagDts(videoData, videoTimestamp);
        videoTimestamp += (timestamp - prevVideoTimestamp);
        prevVideoTimestamp = timestamp;

        // 3. data
        this.sink().next(Jt1078Subscription.fromByteArray(DefaultJt1078SubscriptionType.FLV, videoData));
    }

    /**
     * 重置视频 Tag 的 dts；每个 subscriber 从零开始累加；
     *
     * @see FlvTagHeader#timestamp()
     * @see FlvTagHeader#timestampExtended()
     * @see FlvTagHeader#writeTo(ByteBuf)
     */
    private static void resetFlvVideoTagDts(byte[] payload, long dts) {
        // byte[0]: tagType = (0x09) or (0x08)
        // byte[1,2,3]: dataSize
        payload[4] = (byte) (dts >> 16);
        payload[5] = (byte) (dts >> 8);
        payload[6] = (byte) dts;
        payload[7] = (byte) (dts >> 24);
    }

    @Override
    public void close() {
        this.pcmToMp3Encoder.close();
    }

    public boolean isLastIFramePending() {
        return !this.lastIFrameSent;
    }

    public boolean isFlvHeaderPending() {
        return !this.flvHeaderSent;
    }

}
