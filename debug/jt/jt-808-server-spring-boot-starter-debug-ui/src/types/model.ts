export interface SubPackageMetadata {
    bodyHexString: string;
    header: {
        messageId: number;
        protocolVersion: string;
        bodyProps: {
            messageBodyLength: number;
            encryptionType: number;
            hasSubPackage: boolean;
            versionIdentifier: number;
        };
        terminalId: string;
        flowId: number;
        subPackageProps: {
            totalSubPackageCount: number;
            currentPackageNo: number;
        }
    }
}

export interface DecodeResult {
    single: {
        escapedHexString: string;
        rawHexString: string;
        details: {}
    };
    multiple: {
        mergedHexString: string;
        details: {},
        subPackageMetadata: Array<SubPackageMetadata>
    };
}

export interface EncodeResult {
    rawHexString: string;
    escapedHexString: string;
    details: object;
}

export interface ClassMetadata {
    targetClass: string;
    messageId: number;
    encryptionType: number;
    maxPackageSize: number;
    reversedBit15InHeader: number;
    desc: string;
}
