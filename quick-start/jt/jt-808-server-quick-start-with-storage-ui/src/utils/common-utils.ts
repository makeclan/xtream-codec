// * <li>0x00：图片</li>
// * <li>0x01：音频</li>
// * <li>0x02：视频</li>
// * <li>0x03：文本</li>
// * <li>0x04：其它</li>
export const fileTypeDescriptor = (type: number) => {
    switch (type) {
        case 0:
            return {type: 'primary', text: '图片'}
        case 1:
            return {type: 'warning', text: '音频'}
        case 2:
            return {type: 'success', text: '音频'}
        case 3:
            return {type: 'danger', text: '文本'}
        default:
            return {type: 'info', text: '其它'}
    }
}

export const jt808VersionTagType = (type: string) => {
    switch (type) {
        case 'V2019':
            return 'success'
        case 'V2013':
            return 'warning'
        case 'V2011':
            return 'danger'
        default:
            return 'info'

    }
}

export const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 B';

    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}
export const toHexString = (n: number, pad: number = 4): string => {
    return "0x" + n.toString(16).padStart(pad, "0")
}
