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

package io.github.hylexus.xtream.codec.ext.jt808.spec.impl;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;

/**
 * @author hylexus
 */
public class DefaultJt808SubPackageProps implements Jt808RequestHeader.Jt808SubPackageProps {
    private int totalSubPackageCount;
    private int currentPackageNo;

    public DefaultJt808SubPackageProps() {
    }

    public DefaultJt808SubPackageProps(int totalSubPackageCount, int currentPackageNo) {
        this.totalSubPackageCount = totalSubPackageCount;
        this.currentPackageNo = currentPackageNo;
    }

    @Override
    public int totalSubPackageCount() {
        return this.totalSubPackageCount;
    }

    public DefaultJt808SubPackageProps totalSubPackageCount(int totalSubPackageCount) {
        this.totalSubPackageCount = totalSubPackageCount;
        return this;
    }

    @Override
    public int currentPackageNo() {
        return this.currentPackageNo;
    }

    public DefaultJt808SubPackageProps currentPackageNo(int currentPackageNo) {
        this.currentPackageNo = currentPackageNo;
        return this;
    }

}
