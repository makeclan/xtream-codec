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
public interface DebugEntity01ForJunitPurpose {
    int getMagicNumber();

    void setMagicNumber(int magicNumber);

    short getMajorVersion();

    void setMajorVersion(short majorVersion);

    short getMinorVersion();

    void setMinorVersion(short minorVersion);

    int getMsgType();

    void setMsgType(int msgType);

    int getMsgBodyLength();

    void setMsgBodyLength(int msgBodyLength);

    int getUsernameLength();

    void setUsernameLength(int usernameLength);

    String getUsername();

    void setUsername(String username);

    int getPasswordLength();

    void setPasswordLength(int passwordLength);

    String getPassword();

    void setPassword(String password);

    String getBirthday();

    void setBirthday(String birthday);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    int getAge();

    void setAge(int age);

    short getStatus();

    void setStatus(short status);
}
