import {sidebar} from "vuepress-theme-hope";

export const zhSidebar = sidebar({
    "/guide/": [
        {
            text: "Core",
            collapsible: true,
            children: [
                {
                    text: "入门", icon: 'info', collapsible: false,
                    children: [
                        '/guide/core/index.md',
                        '/guide/core/quick-start/quick-start.md',
                        '/guide/core/quick-start/code-analysis.md',
                    ]
                },
                // {
                //     text: "核心组件", icon: 'class', collapsible: false,
                //     children: [
                //         '/guide/core/core-component/entity-codec.md',
                //         '/guide/core/core-component/field-codec.md',
                //     ]
                // },
                {
                    text: "注解驱动开发", icon: "at", collapsible: false,
                    children: [
                        '/guide/core/annotation-driven/data-types.md',
                        // '/guide/core/annotation-driven/index.md',
                        '/guide/core/annotation-driven/xtream-field-annotation.md',
                        '/guide/core/annotation-driven/builtin-annotations.md',
                        '/guide/core/annotation-driven/custom-annotation.md',
                    ]
                },
                {
                    text: "示例", icon: "info", collapsible: false,
                    children: [
                        {
                            text: "示例1", icon: "info", collapsible: false, children: [
                                '/guide/core/samples/custom-protocol-sample-01/index.md',
                                '/guide/core/samples/custom-protocol-sample-01/flatten-style-demo.md',
                                '/guide/core/samples/custom-protocol-sample-01/nested-style-demo.md',
                            ]
                        },
                        {
                            text: "示例2", icon: "info", collapsible: false, children: [
                                '/guide/core/samples/custom-protocol-sample-02/index.md',
                                '/guide/core/samples/custom-protocol-sample-02/flatten-style-demo.md',
                                '/guide/core/samples/custom-protocol-sample-02/nested-style-demo.md',
                            ]
                        },
                    ]
                },
            ]
        },
        {
            text: "Server",
            collapsible: false,
            children: [
                {
                    text: "入门", icon: 'info', collapsible: false,
                    children: [
                        '/guide/server/index.md',
                        '/guide/server/quick-start/terminology.md',
                        '/guide/server/quick-start/code-analysis.md',
                    ]
                },
                {
                    text: "核心组件", icon: 'info', collapsible: false, children: [
                        '/guide/server/core-component/netty-handler-adapter.md',
                        '/guide/server/core-component/xtream-handler.md',
                        '/guide/server/core-component/session-manager.md',
                        '/guide/server/core-component/command-sender.md',
                        '/guide/server/core-component/ordered-component.md',
                    ]
                },
                {
                    text: "内置请求处理流程", icon: "info", collapsible: false, children: [
                        '/guide/server/request-processing/README.md',
                        '/guide/server/request-processing/dispatcher-handler.md',
                        '/guide/server/request-processing/filter.md',
                        '/guide/server/request-processing/filtering-xtream-handler.md',
                        '/guide/server/request-processing/request-exception-handler.md',
                        '/guide/server/request-processing/exception-handling-xtream-handler.md',
                    ]
                },
                {
                    text: "注解驱动开发", icon: "at", collapsible: false, children: [
                        '/guide/server/annotation-driven/xtream-request-handler-mapping.md',
                        '/guide/server/annotation-driven/handler-method-argument-resolver.md',
                    ]
                },
            ]
        }
    ],
    "/ext/": [
        {
            text: "JT/T 808 扩展",
            children: [
                {
                    text: "入门", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/README.md',
                        '/ext/jt/jt808/quick-start/quick-start.md',
                        '/ext/jt/jt808/quick-start/code-analysis.md',
                    ]
                },
                {
                    text: "核心组件", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/core-component/README.md',
                    ]
                },
                {
                    text: "辅助工具", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/utilities/README.md',
                    ]
                },
                {
                    text: "定制化", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/customization/README.md',
                    ]
                },
                {
                    text: "地方标准", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/extension/jiangsu.md',
                    ]
                },
                {
                    text: "Dashboard", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/dashboard/README.md',
                    ]
                },
                {
                    text: "配置", icon: "info", collapsible: false, children: [
                        '/ext/jt/jt808/configuration/README.md',
                    ]
                },
            ]
        }
    ],
});
