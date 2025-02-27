export const toHexString = (n: number, pad: number = 4): string => {
    return "0x" + n.toString(16).padStart(pad, "0")
}

export const toBinaryString = (n: number, pad: number = 4): string => {
    return "0b" + n.toString(2).padStart(pad, "0")
}

export type CodecData = {
    [key in string]: {
        messageId: number;
        hexString: string;
        bodyJson?: any;
        hasBodyData?: boolean;
    };
}
export const codecMockData: CodecData = {
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0704": {
        messageId: 0x0704,
        hexString: "7e0704403f01000000000139123443290000000200001c000000010000000101d907f2073d336c029a00210059241029213903001c000000010000000101d907f3073d336d03090043005a241029213903f87e",
        bodyJson: {
            "count": 2,
            "type": 0,
            "itemList": [{
                "locationData": {
                    "alarmFlag": 1,
                    "status": 1,
                    "latitude": 31000562,
                    "longitude": 121451372,
                    "altitude": 666,
                    "speed": 33,
                    "direction": 89,
                    "time": "2024-10-29T21:39:03Z"
                }
            }, {
                "locationData": {
                    "alarmFlag": 1,
                    "status": 1,
                    "latitude": 31000563,
                    "longitude": 121451373,
                    "altitude": 777,
                    "speed": 67,
                    "direction": 90,
                    "time": "2024-10-29T21:39:03Z"
                }
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200": {
        messageId: 0x0200,
        hexString: "7e02004026010000000001391234432900000000006f0000001601dc9a0707456246007b004200032501292233440202006401040000000c417e",
        bodyJson: {
            "alarmFlag": 111,
            "status": 22,
            "latitude": 31234567,
            "longitude": 121987654,
            "altitude": 123,
            "speed": 66,
            "direction": 3,
            "time": "2025-01-29T22:33:44Z",
            "extraItems": {
                "1": 12,
                "2": 100
            }
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200Sample2": {
        messageId: 0x0200,
        hexString: "7e0200407c010000000001391234432900000000007b000000de01d907f2073d336c029a004e000014102119510901040000006f020200de0302029a0402014d2504000003093001211206010000000001130700000001000000310137642f000000dedf6f03420b0c0d0e0f04d201d907f2073d336c1410211951090001313233343536371410211951090101008e7e",
        bodyJson: {
            "alarmFlag": 111,
            "status": 22,
            "latitude": 31234567,
            "longitude": 121987654,
            "altitude": 123,
            "speed": 66,
            "direction": 3,
            "time": "2025-01-29T22:33:44Z",
            "extraItems": {
                "1": 12,
                "2": 100
            }
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0005": {
        messageId: 0x0005,
        hexString: "7e0005400a01000000000139123443290000006f0003000200030004537e",
        bodyJson: {"originalMessageFlowId": 111, "packageCount": 3, "packageIdList": [2, 3, 4]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8004": {
        messageId: 0x8004,
        hexString: "7e8004400601000000000139123443290000241003155106c27e",
        bodyJson: {"serverSideDateTime": "2025-02-15T19:10:35Z"}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0705": {
        messageId: 0x0705,
        hexString: "7e0705401f0100000000013912344329000000022023451122800000000102030405060708200000001122334455667708ff7e",
        bodyJson: {
            "count": 2,
            "receiveTime": [20, 23, 45, 112299999],
            "itemList": [{"canId": 2147483648, "canData": [1, 2, 3, 4, 5, 6, 7, 8]}, {
                "canId": 536870912,
                "canData": [17, 34, 51, 68, 85, 102, 119, 8]
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0800": {
        messageId: 0x0800,
        hexString: "7e08004008010000000001391234432900000000006f0000077b267e",
        bodyJson: {
            "multimediaDataID": 111,
            "multimediaType": 0,
            "multimediaFormatCode": 0,
            "eventItemCode": 7,
            "channelId": 123
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0801": {
        messageId: 0x0801,
        hexString: "7e08014029010000000001391234432900000000006f0000077b000000010000000201d907f2073d336c0064003c004e14102119510901020304053e7e",
        bodyJson: {
            "multimediaDataID": 111,
            "multimediaType": 0,
            "multimediaFormatCode": 0,
            "eventItemCode": 7,
            "channelId": 123,
            "location": {
                "alarmFlag": 1,
                "status": 2,
                "latitude": 31000562,
                "longitude": 121451372,
                "altitude": 100,
                "speed": 60,
                "direction": 78,
                "time": "2014-10-21T19:51:09Z"
            },
            "mediaData": [1, 2, 3, 4, 5]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0802": {
        messageId: 0x0802,
        hexString: "7e0802404a01000000000139123443290000006f000200000001001600000000010000000101d907f3073d336d03090043005a21092715303300000001001600000000010000000101d907f5073d336f03090043005a2109271530331c7e",
        bodyJson: {
            "flowId": 111,
            "multimediaDataItemCount": 2,
            "itemList": [{
                "multimediaId": 1,
                "multimediaType": 0,
                "channelId": 22,
                "eventItemCode": 0,
                "location": {
                    "alarmFlag": 1,
                    "status": 1,
                    "latitude": 31000563,
                    "longitude": 121451373,
                    "altitude": 777,
                    "speed": 67,
                    "direction": 90,
                    "time": "2021-09-27T15:30:33Z"
                }
            }, {
                "multimediaId": 1,
                "multimediaType": 0,
                "channelId": 22,
                "eventItemCode": 0,
                "location": {
                    "alarmFlag": 1,
                    "status": 1,
                    "latitude": 31000565,
                    "longitude": 121451375,
                    "altitude": 777,
                    "speed": 67,
                    "direction": 90,
                    "time": "2021-09-27T15:30:33Z"
                }
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0805": {
        messageId: 0x0805,
        hexString: "7e0805400d01000000000139123443290000006f00000200000001000000025b7e",
        bodyJson: {"flowId": 111, "result": 0, "multimediaIdCount": 2, "multimediaIdList": [1, 2]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0A00": {
        messageId: 0x0A00,
        hexString: "7e0a00408401000000000139123443290000000000120707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707070707a97e",
        bodyJson: {
            "e": 18,
            "n": [7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8001": {
        messageId: 0x8001,
        hexString: "7e8001400501000000000139123443290000006f020000dc7e",
        bodyJson: {"clientFlowId": 111, "clientMessageId": 512, "result": 0}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.ServerCommonReplyMessage": {
        messageId: 0x8001,
        hexString: "7e8001400501000000000139123443290000006f020000dc7e",
        bodyJson: {"clientFlowId": 111, "clientMessageId": 512, "result": 0}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8003V2013": {
        messageId: 0x8003,
        hexString: "7e800300090139123443230000006f030002000300049d7e",
        bodyJson: {"originalMessageFlowId": 111, "packageCount": 3, "packageIdList": [2, 3, 4]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8003V2019": {
        messageId: 0x8003,
        hexString: "7e8003400a01000000000139123443290000006f0003000200030004d57e",
        bodyJson: {"originalMessageFlowId": 111, "packageCount": 3, "packageIdList": [2, 3, 4]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8100": {
        messageId: 0x8100,
        hexString: "7e8100400d01000000000139123443290000006f006f6b2e2e6f6b2e2e6f6bd27e",
        bodyJson: {"clientFlowId": 111, "result": 0, "authCode": "ok..ok..ok"}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8104": {
        messageId: 0x8104,
        hexString: "7e8104400001000000000139123443290000b07e",
        bodyJson: {},
        hasBodyData: false
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8106": {
        messageId: 0x8106,
        bodyJson: {"parameterCount": 2, "parameterIdList": [1, 2]},
        hexString: "7e8106400901000000000139123443290000020000000100000002ba7e"
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8107": {
        messageId: 0x8107,
        hexString: "7e8107400001000000000139123443290000b37e",
        bodyJson: {},
        hasBodyData: false
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8108": {
        messageId: 0x8108,
        hexString: "7e81080016013912344323000034313233343507762d312e322e330000000401020304887e",
        bodyJson: {"type": 52, "manufacturerId": "12345", "version": "v-1.2.3", "data": [1, 2, 3, 4]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8201": {
        messageId: 0x8201,
        hexString: "7e8201400001000000000139123443290000b67e",
        bodyJson: {},
        hasBodyData: false
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8202": {
        messageId: 0x8202,
        hexString: "7e8202400601000000000139123443290000001e000003e8467e",
        bodyJson: {"timeDurationInSeconds": 30, "traceValidityDurationInSeconds": 1000}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8203": {
        messageId: 0x8203,
        hexString: "7e8203400601000000000139123443290000014110200000c27e",
        bodyJson: {"flowId": 321, "alarmType": 270532608}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8204": {
        messageId: 0x8204,
        hexString: "7e8204400001000000000139123443290000b37e",
        bodyJson: {},
        hasBodyData: false
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8300": {
        messageId: 0x8300,
        hexString: "7e8300001901391234432300000148656c6c6f576f726c6420babad7d62047424b20b1e0c2ebf27e",
        bodyJson: {"identifier": 1, "text": "HelloWorld 汉字 GBK 编码"}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8300V2019": {
        messageId: 0x8300,
        hexString: "7e8300401a01000000000139123443290000010148656c6c6f576f726c6420babad7d62047424b20b1e0c2ebbb7e",
        bodyJson: {"identifier": 1, "textType": 1, "text": "HelloWorld 汉字 GBK 编码"}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8301": {
        messageId: 0x8301,
        hexString: "7e83010010013912344323000001020104686168610206686569686569ee7e",
        bodyJson: {
            "eventType": 1,
            "eventCount": 2,
            "eventItemList": [{"eventId": 1, "eventContent": "haha"}, {"eventId": 2, "eventContent": "heihei"}]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8302": {
        messageId: 0x8302,
        hexString: "7e8302001701391234432300000107c4e3cac7cbad3f010004d5c5c8fd020004c0eecbc49a7e",
        bodyJson: {
            "identifier": 1,
            "question": "你是谁?",
            "candidateAnswerList": [{"answerId": 1, "answer": "张三"}, {"answerId": 2, "answer": "李四"}]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8303": {
        messageId: 0x8303,
        hexString: "7e8303401d010000000001391234432900000103010006b2e2cad42031020006b2e2cad42032030006b2e2cad42033f27e",
        bodyJson: {
            "type": 1,
            "itemCount": 3,
            "itemList": [{"type": 1, "content": "测试 1"}, {"type": 2, "content": "测试 2"}, {
                "type": 3,
                "content": "测试 3"
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8304": {
        messageId: 0x8304,
        hexString: "7e8304000a0139123443230000010007b2e2cad42e2e2e957e",
        bodyJson: {"type": 1, "length": 7, "content": "测试..."}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8400": {
        messageId: 0x8400,
        hexString: "7e8400400c01000000000139123443290000003133393030303031313131867e",
        bodyJson: {"flag": 0, "phoneNumber": "13900001111"}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8401": {
        messageId: 0x8401,
        hexString: "7e8401403c010000000001391234432900000103030b313339313131313232323204d5c5c8fd020b313339313131313333333308d5c5c8fdb7e8d7d3010b313339313131313636363604c0eecbc4cc7e",
        bodyJson: {
            "type": 1,
            "count": 3,
            "itemList": [
                {"flag": 3, "phoneNumber": "13911112222", "contacts": "张三"},
                {"flag": 2, "phoneNumber": "13911113333", "contacts": "张三疯子"},
                {"flag": 1, "phoneNumber": "13911116666", "contacts": "李四"}
            ]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8500": {
        messageId: 0x8500,
        hexString: "7e85000001013912344323000001fb7e",
        bodyJson: {"type": 1}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8600": {
        messageId: 0x8600,
        hexString: "7e86000038013912344323000001020000006f400301d907f2073d336c0000029a241026183956241026203956006f0a000000de000201d907f2073d336c0000029a006f0a0b7e",
        bodyJson: {
            "type": 1,
            "areaCount": 2,
            "areaList": [{
                "areaId": 111,
                "areaProps": 16387,
                "latitude": 31000562,
                "longitude": 121451372,
                "radius": 666,
                "startTime": "2024-10-26T18:39:56Z",
                "endTime": "2024-10-26T20:39:56Z",
                "topSpeed": 111,
                "durationOfOverSpeed": 10
            }, {
                "areaId": 222,
                "areaProps": 2,
                "latitude": 31000562,
                "longitude": 121451372,
                "radius": 666,
                "startTime": "2024-10-26T18:39:56Z",
                "endTime": "2024-10-26T20:39:56Z",
                "topSpeed": 111,
                "durationOfOverSpeed": 10
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8601": {
        messageId: 0x8601,
        hexString: "7e8601400d01000000000139123443290000030000006f000000de0000014d417e",
        bodyJson: {"areaCount": 3, "areaIdList": [111, 222, 333]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8602": {
        messageId: 0x8602,
        hexString: "7e86020040013912344323000001020000006f400301d907f2073d336c01d907f3073d336d241026183956241026203956006f0a000000de000201d907f2073d336c01d907f3073d336d006f0a717e",
        bodyJson: {
            "type": 1,
            "areaCount": 2,
            "areaList": [{
                "areaId": 111,
                "areaProps": 16387,
                "leftTopLatitude": 31000562,
                "leftTopLongitude": 121451372,
                "rightBottomLatitude": 31000563,
                "rightBottomLongitude": 121451373,
                "startTime": "2024-10-26T18:39:56Z",
                "endTime": "2024-10-26T20:39:56Z",
                "topSpeed": 111,
                "durationOfOverSpeed": 10
            }, {
                "areaId": 222,
                "areaProps": 2,
                "leftTopLatitude": 31000562,
                "leftTopLongitude": 121451372,
                "rightBottomLatitude": 31000563,
                "rightBottomLongitude": 121451373,
                "startTime": "2024-10-26T18:39:56Z",
                "endTime": "2024-10-26T20:39:56Z",
                "topSpeed": 111,
                "durationOfOverSpeed": 10
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8603": {
        messageId: 0x8603,
        hexString: "7e8603400d01000000000139123443290000030000006f000000de0000014d437e",
        bodyJson: {"areaCount": 3, "areaIdList": [111, 222, 333]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8604": {
        messageId: 0x8604,
        hexString: "7e8604002f01391234432300000000006f4003241026183956241026203956006f0a000301d907f2073d336c01d907f3073d336d01d907f4073d336eed7e",
        bodyJson: {
            "areaId": 111,
            "areaProps": 16387,
            "startTime": "2024-10-26T18:39:56Z",
            "endTime": "2024-10-26T20:39:56Z",
            "topSpeed": 111,
            "durationOfOverSpeed": 10,
            "pointCount": 3,
            "pointList": [
                {"latitude": 31000562, "longitude": 121451372},
                {"latitude": 31000563, "longitude": 121451373},
                {"latitude": 31000564, "longitude": 121451374}
            ]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8605": {
        messageId: 0x8605,
        hexString: "7e8605400d01000000000139123443290000030000006f000000de0000014d457e",
        bodyJson: {"areaCount": 3, "areaIdList": [111, 222, 333]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8606": {
        messageId: 0x8606,
        hexString: "7e8606003f01391234432300000000029a000324102618395624102620395600020000006f0000045701d907f1073d336b1403270f045700ea06000000de000008ae01d907f3073d336d1400b47e",
        bodyJson: {
            "routeId": 666,
            "routeProps": 3,
            "startTime": "2024-10-26 18:39:56",
            "endTime": "2024-10-26 20:39:56",
            "count": 2,
            "itemList": [{
                "id": 111,
                "routeId": 1111,
                "latitude": 31000561,
                "longitude": 121451371,
                "routeWidth": 20,
                "routeProps": 3,
                "longDriveThreshold": 9999,
                "shortDriveThreshold": 1111,
                "maxSpeedLimit": 234,
                "speedingDuration": 6
            }, {
                "id": 222,
                "routeId": 2222,
                "latitude": 31000563,
                "longitude": 121451373,
                "routeWidth": 20,
                "routeProps": 0,
                "longDriveThreshold": 678,
                "shortDriveThreshold": 321,
                "maxSpeedLimit": 333,
                "speedingDuration": 66
            }]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8607": {
        messageId: 0x8607,
        hexString: "7e8607400d01000000000139123443290000030000006f000000de0000014d477e",
        bodyJson: {"areaCount": 3, "areaIdList": [111, 222, 333]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8608": {
        messageId: 0x8608,
        hexString: "7e860840110100000000013912344329000001000000030000006f000000de0000014d557e",
        bodyJson: {"type": 1, "count": 3, "idList": [111, 222, 333]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8702": {
        messageId: 0x8702,
        hexString: "7e8702400001000000000139123443290000b07e",
        bodyJson: {},
        hasBodyData: false
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8800": {
        messageId: 0x8800,
        hexString: "7e88004009010000000001391234432900000000000b0201020304b97e",
        bodyJson: {"multimediaId": 11, "retransmittedPackageCount": 2, "retransmittedPackageIdList": [1, 2, 3, 4]}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8801": {
        messageId: 0x8801,
        hexString: "7e8801400c01000000000139123443290000160000000001ff03c764636ff47e",
        bodyJson: {
            "chanelId": 22,
            "command": 0,
            "duration": 0,
            "saveFlag": 1,
            "resolution": 255,
            "quality": 3,
            "brightness": 199,
            "contrastRate": 100,
            "saturation": 99,
            "chroma": 111
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8802": {
        messageId: 0x8802,
        hexString: "7e8802400f01000000000139123443290000020300210412143003210412143004b67e",
        bodyJson: {
            "multimediaType": 2,
            "channelId": 3,
            "eventItemCode": 0,
            "startTime": "2021-04-12 14:30:03",
            "endTime": "2021-04-12 14:30:04"
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8803": {
        messageId: 0x8803,
        hexString: "7e880340100100000000013912344329000002030021041214300321041214300400a87e",
        bodyJson: {
            "multimediaType": 2,
            "channelId": 3,
            "eventItemCode": 0,
            "startTime": "2021-04-12 14:30:03",
            "endTime": "2021-04-12 14:30:04",
            "deleteFlag": 0
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8804": {
        messageId: 0x8804,
        hexString: "7e880440050100000000013912344329000001000a0003b47e",
        bodyJson: {"recordingCommand": 1, "recordingDuration": 10, "saveFlag": 0, "audioSampleRate": 3}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8805": {
        messageId: 0x8805,
        hexString: "7e88054005010000000001391234432900000000000101bd7e",
        bodyJson: {"multimediaId": 1, "deleteFlag": 1}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response.BuiltinMessage8A00": {
        messageId: 0x8A00,
        hexString: "7e8a004084010000000001391234432900000000001106060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606060606062a7e",
        bodyJson: {
            "e": 17,
            "n": [6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6]
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.ext.location.BuiltinMessage9208": {
        messageId: 0x9208,
        hexString: "7e92084052010000000001391234432900000d3139322e3136382e302e313233045708ae313233343536372502212133326f0100313233343536373839303132333435363738393031323334353637383930313230303030303030303030303030303030547e",
        bodyJson: {
            "attachmentServerIp": "192.168.0.123",
            "attachmentServerPortTcp": 1111,
            "attachmentServerPortUdp": 2222,
            "alarmIdentifier": {
                "terminalId": "1234567",
                "time": "2025-02-21 21:33:32",
                "sequence": 111,
                "attachmentCount": 1,
                "reserved": 0
            },
            "alarmNo": "12345678901234567890123456789012",
            "reservedByte16": "0000000000000000"
        }
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0001": {
        messageId: 0x0001,
        hexString: "7e8001400501000000000139123443290000007b020001c97e",
        bodyJson: {"serverFlowId": 123, "serverMessageId": 512, "result": 1}
    },
    "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0002": {
        messageId: 0x0002,
        hexString: "7e0002400001000000000139123443290000377e",
        bodyJson: {},
        hasBodyData: false
    }
}
