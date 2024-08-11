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

package io.github.hylexus.xtream.codec.ext.jt808.spec;

import io.github.hylexus.xtream.codec.ext.jt808.spec.impl.DefaultJt808FlowIdGenerator;

/**
 * @author hylexus
 */
public interface Jt808FlowIdGenerator {

    Jt808FlowIdGenerator DEFAULT = new DefaultJt808FlowIdGenerator();

    int MAX_FLOW_ID = 0xffff;

    /**
     * @param increment 一次性(连续不间断)递增 {@code increment} 个序列号
     * @return 当前流水号
     */
    int flowId(int increment);

    /**
     * @param count 流水号个数
     * @return 一批连续递增的流水号
     */
    default int[] flowIds(int count) {
        int last = this.flowId(count) - 1;
        final int[] ids = new int[count];
        for (int i = count - 1; i >= 0; i--) {
            ids[i] = last--;
        }
        return ids;
    }

    /**
     * @return 当前流水号(不会自增)
     */
    default int currentFlowId() {
        return flowId(0);
    }

    /**
     * @return 下一个流水号
     * @see #flowId(int)
     */
    default int nextFlowId() {
        return flowId(1);
    }
}
