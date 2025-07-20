import axios, {AxiosError, AxiosInstance, AxiosRequestConfig, CancelTokenSource, InternalAxiosRequestConfig,} from "axios";
import {ElMessage} from "element-plus";

interface ExtendedAxiosRequestConfig extends AxiosRequestConfig {
    retry?: number; // retry count
    retryDelay?: number; // retry delay time
}

interface LoadingHandler {
    start: () => void;
    end: () => void;
}

const config = {
    BASE_URL: import.meta.env.VITE_API_DASHBOARD_V1,
    TIMEOUT: parseInt(import.meta.env.VITE_BASE_API_TIMEOUT || "30000", 10),
};

// create axios instance
const instance: AxiosInstance = axios.create({
    baseURL: config.BASE_URL,
    timeout: config.TIMEOUT,
    withCredentials: true, // allow cookies
});

// global loading default processing
const defaultLoadingHandler: LoadingHandler = {
    start: () => console.log("Loading started..."),
    end: () => console.log("Loading ended..."),
};

let loadingHandler: LoadingHandler = defaultLoadingHandler;

// Set global loading handler
const setGlobalLoadingHandler = (handler: LoadingHandler) => {
    loadingHandler = handler;
};

// // get token
// const getToken = (): string | null => {
//   return sessionStorage.getItem("token");
// };

// request interception
instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // open global loading
        loadingHandler.start();

        // add auth info, examples: JWT Token request config
        // const token = getToken();

        // if (token) {
        //   config.headers = {
        //     ...config.headers,
        //     Authorization: `Bearer ${token}`,
        //   };
        // }

        return config;
    },
    (error) => {
        // close global loading
        loadingHandler.end();

        // handle request error
        return Promise.reject(error);
    },
);

// Response Interception
instance.interceptors.response.use(
    (response) => {
        loadingHandler.end();

        // handle response data format
        return response;
    },
    async (error: AxiosError) => {
        loadingHandler.end();

        const originalRequest = error.config as ExtendedAxiosRequestConfig;

        if (error.response && error.response.status === 401) {
            // If a 401 error is returned, the logic to retrieve the token can be handled here and the original request sent again.
            // For example, refresh token, log in again, etc.
        }
        if (originalRequest?.retry && originalRequest.retry > 0) {
            originalRequest.retry -= 1;
            const retryDelay = originalRequest.retryDelay || 1000; // set retry delay

            await new Promise((resolve) => setTimeout(resolve, retryDelay)); // delay

            return instance(originalRequest); // re-send request
        }

        return Promise.reject(error);
    },
);

const request = async <T>(
    config: {
        path: string;
        method: string;
        headers?: any;
        data?: any;
        params?: any;
        cancelToken?: CancelTokenSource;
    } & AxiosRequestConfig,
): Promise<T> => {
    const {path, method, headers, data, params, ...rest} = config;

    try {
        const response = await instance({
            url: path,
            method,
            headers,
            data,
            params,
            ...rest,
        });

        return response.data;
    } catch (error: any) {
        // Agree to handle errors
        // console.error("API request error:", error);
        console.error('Failed to request api', error)
        ElMessage({
            message: error.message || `Request Error: path=${path}`,
            type: 'error',
            duration: 3000,
        })
        throw error;
    }
};

// cancel request
const createCancelToken = (): CancelTokenSource => {
    return axios.CancelToken.source();
};

export {request, createCancelToken, setGlobalLoadingHandler};
