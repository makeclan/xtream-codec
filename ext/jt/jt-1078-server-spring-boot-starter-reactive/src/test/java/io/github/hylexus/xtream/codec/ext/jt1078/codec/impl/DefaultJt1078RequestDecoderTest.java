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

package io.github.hylexus.xtream.codec.ext.jt1078.codec.impl;

import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.ext.MockNettyInbound;
import io.github.hylexus.xtream.codec.ext.jt1078.codec.Jt1078RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078DataType;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078Request;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078SubPackageIdentifier;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078PayloadType;
import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamInbound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultJt1078RequestDecoderTest {

    @Test
    void test() {
        final Jt1078RequestDecoder decoder = new DefaultJt1078RequestDecoder((nettyInbound, header) -> "TraceId");
        final String hexString = "81620000013800138999020100000092D6FA4C2E0000000003B600000001674D00149DB85825A100000300010000030008840000000168EE3C800000000106E501AB800000000165B800000303FABC11FF64C622EEDFEDD87FC1C413B3D5A4B602DF6282226EFE3CB96E93820D78E7193C68EBF22F7FB182B5F32A0E35D236EA45A8FB487769B0354AAC0498CCC58E2DD3AF04D28C354D608C9B0B794C10E13AA5C450731378135AAC4AC2780DE4BA66B6108F1EB68F5F256B461C3727C995C219B128B8F03C1EBCF9260FE614FE5910C753CD6B93BEA25AC1A849A3F3FED54897B7D9DA7C2BA45916379764211BEE142306893874834835334EE8CEF218450716764D5D43561DD3A71F2BC464F20B71EF1A679ED170422279CF68B192D7411725F3A256334CC03E52C0398DC1A5477C2359FCFAD05BEF252A8F2A64B47C00E4260FC7F53FB4DCE7975CC56CB742259897E1BBB294BC20530E6D83A77EFA229AEE56A7E55D537044A12A1747B7BAE6919175B0CFA3D4D2CEBAC33DA7B87F46A7B1FA693B99D9D6224457ACCDDC0560A6358C2B1214D7B24E67B289876655C98F4C79A5CBE948C563F8BA913BFF3FFEAD4CD04927709E82F836267C18346285EBA3731C75324E38C53BA3856CF08F3D2BF47B17E7D1D2C4762A45FC2DD09D1CAFB7E7C982C0CE8B08AB4339CC0F50CDD56B1B40E6E82401671925B383047358431D392CF666A3DA83887976DEF40CFB6F517AF9CBD7A9708D9D0156AA9C9D3ADED45B60F03D97C456FDCCF0C3A7C8C2443FCCCE0DCBF348EBEC0975482089FF4F5DC2DB25DC25C1325B45F179E02AE71249A377BD4662D2BAA93E8792E6391471E2AFCED5AE5C92681AF1015B98B6A93F03B0BB56D194E1684FCD29B4E691F0844D13B9787A62F3591CF819DA316C0C62607EFD2C108B91291C7B93E78B09FAB10C4D42D6C119614EE0D2B2F91FFB349522D30B336FE3C3EC683B1748541FC502905DC1C40822C76B618B6F6654909BFCC4D24B8652AEE4CEA8B2707400CED930ABA5E57483882CF16972404D3DAEE6A2FD7CD524A9B8012585600F661311AD77806EE56C882FFA3C1792C16F7775026067F394CC889A09CAD85F4C1A6FBAD42535CD30F8EE267638629043950A31623DD969C3FA9D6FB2138D37593CF68812167C82886815442D9FBADEAF0AC1CECFFFE7C87B95E663EDE4F4A56D6BE924FE8DC8119B3F2D5A16EF44BF07C934138E9CB4A7A07EE6C9F8DD6AAE40C6161C7762920167FE754063D6FC9A2ECF6D12FE3523D57A4EFA8FC9DDEB98206E23D43E2868ABE68C97B9D53B96D63C849DE85C7B8CFEF54D0FE7843564C24FE0E2874C899E8316DA92F8C56A3FFCB75C911FE31C";

        final ByteBuf buffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hexString);
        try {
            final Jt1078Request request = decoder.decode(false, "requestId", ByteBufAllocator.DEFAULT, new MockNettyInbound(), XtreamInbound.Type.TCP, null, buffer);
            doCompare(request);
            request.release();
        } finally {
            XtreamBytes.releaseBuf(buffer);
        }

        final ByteBuf buffer2 = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, "30316364" + hexString);
        try {
            final Jt1078Request request = decoder.decode(true, "requestId", ByteBufAllocator.DEFAULT, new MockNettyInbound(), XtreamInbound.Type.TCP, null, buffer2);
            doCompare(request);
            request.release();
        } finally {
            XtreamBytes.releaseBuf(buffer2);
        }
    }

    private static void doCompare(Jt1078Request request) {
        final Jt1078RequestHeader header = request.header();
        assertEquals(2, header.v());
        assertEquals(0, header.p());
        assertEquals(0, header.x());
        assertEquals(1, header.cc());

        assertEquals(0, header.m());
        assertEquals(DefaultJt1078PayloadType.H264, header.payloadType());

        assertEquals(0, header.sequenceNumber());
        assertEquals("013800138999", header.sim());
        assertEquals(2, header.channelNumber());
        assertEquals(Jt1078DataType.VIDEO_I, header.dataType());
        assertEquals(Jt1078SubPackageIdentifier.FIRST, header.subPackageIdentifier());

        assertTrue(header.timestamp().isPresent());
        assertEquals(630671952942L, header.timestamp().get());

        assertTrue(header.lastIFrameInterval().isPresent());
        assertEquals(0, header.lastIFrameInterval().get());

        assertTrue(header.lastFrameInterval().isPresent());
        assertEquals(0, header.lastFrameInterval().get());

        assertEquals(950, header.msgBodyLength());

        assertEquals(
                "00000001674d00149db85825a100000300010000030008840000000168ee3c800000000106e501ab800000000165b800000303fabc11ff64c622eedfedd87fc1c413b3d5a4b602df6282226efe3cb96e93820d78e7193c68ebf22f7fb182b5f32a0e35d236ea45a8fb487769b0354aac0498ccc58e2dd3af04d28c354d608c9b0b794c10e13aa5c450731378135aac4ac2780de4ba66b6108f1eb68f5f256b461c3727c995c219b128b8f03c1ebcf9260fe614fe5910c753cd6b93bea25ac1a849a3f3fed54897b7d9da7c2ba45916379764211bee142306893874834835334ee8cef218450716764d5d43561dd3a71f2bc464f20b71ef1a679ed170422279cf68b192d7411725f3a256334cc03e52c0398dc1a5477c2359fcfad05bef252a8f2a64b47c00e4260fc7f53fb4dce7975cc56cb742259897e1bbb294bc20530e6d83a77efa229aee56a7e55d537044a12a1747b7bae6919175b0cfa3d4d2cebac33da7b87f46a7b1fa693b99d9d6224457accddc0560a6358c2b1214d7b24e67b289876655c98f4c79a5cbe948c563f8ba913bff3ffead4cd04927709e82f836267c18346285eba3731c75324e38c53ba3856cf08f3d2bf47b17e7d1d2c4762a45fc2dd09d1cafb7e7c982c0ce8b08ab4339cc0f50cdd56b1b40e6e82401671925b383047358431d392cf666a3da83887976def40cfb6f517af9cbd7a9708d9d0156aa9c9d3aded45b60f03d97c456fdccf0c3a7c8c2443fccce0dcbf348ebec0975482089ff4f5dc2db25dc25c1325b45f179e02ae71249a377bd4662d2baa93e8792e6391471e2afced5ae5c92681af1015b98b6a93f03b0bb56d194e1684fcd29b4e691f0844d13b9787a62f3591cf819da316c0c62607efd2c108b91291c7b93e78b09fab10c4d42d6c119614ee0d2b2f91ffb349522d30b336fe3c3ec683b1748541fc502905dc1c40822c76b618b6f6654909bfcc4d24b8652aee4cea8b2707400ced930aba5e57483882cf16972404d3daee6a2fd7cd524a9b8012585600f661311ad77806ee56c882ffa3c1792c16f7775026067f394cc889a09cad85f4c1a6fbad42535cd30f8ee267638629043950a31623dd969c3fa9d6fb2138d37593cf68812167c82886815442d9fbadeaf0ac1cecfffe7c87b95e663ede4f4a56d6be924fe8dc8119b3f2d5a16ef44bf07c934138e9cb4a7a07ee6c9f8dd6aae40c6161c7762920167fe754063d6fc9a2ecf6d12fe3523d57a4efa8fc9ddeb98206e23d43e2868abe68c97b9d53b96d63c849de85c7b8cfef54d0fe7843564c24fe0e2874c899e8316da92f8c56a3ffcb75c911fe31c",
                FormatUtils.toHexString(request.body())
        );
    }

}
