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

public interface DebugEntity01Nested {
    // DebugEntity01NestedHeader getHeader();
    //
    // void setHeader(DebugEntity01NestedHeader header);
    //
    // int getMsgBodyLength();
    //
    // void setMsgBodyLength(int msgBodyLength);
    //
    // DebugEntity01NestedBody getBody();
    //
    // void setBody(DebugEntity01NestedBody body);

    interface DebugEntity01NestedHeader {
        int getMagicNumber();

        short getMajorVersion();

        short getMinorVersion();

        int getMsgType();

        void setMagicNumber(int magicNumber);

        void setMajorVersion(short majorVersion);

        void setMinorVersion(short minorVersion);

        void setMsgType(int msgType);
    }


    interface DebugEntity01NestedBody {
        int getUsernameLength();

        String getUsername();

        int getPasswordLength();

        String getPassword();

        String getBirthday();

        String getPhoneNumber();

        int getAge();

        short getStatus();


        void setUsernameLength(int usernameLength);

        void setUsername(String username);

        void setPasswordLength(int passwordLength);

        void setPassword(String password);

        void setBirthday(String birthday);

        void setPhoneNumber(String phoneNumber);

        void setAge(int age);

        void setStatus(short status);
    }

}
