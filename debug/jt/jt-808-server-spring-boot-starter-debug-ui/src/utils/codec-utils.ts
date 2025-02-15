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
    }
}
