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
public interface DebugEntity01NestedForJunitPurpose {

    /**
     * 这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    interface DebugEntity01NestedHeader {
        int getMagicNumber();

        void setMagicNumber(int magicNumber);

        short getMajorVersion();

        void setMajorVersion(short majorVersion);

        short getMinorVersion();

        void setMinorVersion(short minorVersion);

        int getMsgType();

        void setMsgType(int msgType);
    }


    /**
     * 这个接口仅仅是为了 Junit 中赋值、取值方便（没有其他任何目的）
     */
    interface DebugEntity01NestedBody {
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

}
