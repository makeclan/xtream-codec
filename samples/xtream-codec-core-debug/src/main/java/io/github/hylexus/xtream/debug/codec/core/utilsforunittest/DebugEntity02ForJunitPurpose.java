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

package io.github.hylexus.xtream.debug.codec.core.utilsforunittest;

/**
 * 这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
 */
public interface DebugEntity02ForJunitPurpose {
    int getMsgId();

    void setMsgId(int msgId);

    int getMsgBodyProps();

    void setMsgBodyProps(int msgBodyProps);

    byte getProtocolVersion();

    void setProtocolVersion(byte protocolVersion);

    String getTerminalId();

    void setTerminalId(String terminalId);

    int getMsgSerialNo();

    void setMsgSerialNo(int msgSerialNo);

    Long getSubPackageInfo();

    void setSubPackageInfo(Long subPackageInfo);

    long getAlarmFlag();

    void setAlarmFlag(long alarmFlag);

    long getStatus();

    void setStatus(long status);

    long getLatitude();

    void setLatitude(long latitude);

    long getLongitude();

    void setLongitude(long longitude);

    int getAltitude();

    void setAltitude(int altitude);

    int getSpeed();

    void setSpeed(int speed);

    int getDirection();

    void setDirection(int direction);

    String getTime();

    void setTime(String time);

    byte getCheckSum();

    void setCheckSum(byte checkSum);


    /**
     * 这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    interface ExtraItemForJunitPurpose {
        short getId();

        short getContentLength();

        byte[] getContent();
    }
}
