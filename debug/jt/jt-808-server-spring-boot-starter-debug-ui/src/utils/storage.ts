export const setLocalStorage = <T>(key: string, value: T): void => {
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
        console.error('写入失败:', error);
    }
};

export const getLocalStorage = <T>(key: string): T | null => {
    const value = localStorage.getItem(key);
    if (value) {
        try {
            return JSON.parse(value) as T;
        } catch (error) {
            console.error('解析失败:', error);
            // 如果不是 JSON 格式，直接返回字符串
            return value as unknown as T;
        }
    }
    return null;
};
