/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
