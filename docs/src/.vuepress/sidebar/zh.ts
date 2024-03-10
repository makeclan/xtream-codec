import {sidebar} from "vuepress-theme-hope";

export const zhSidebar = sidebar({
    "/core/": [
        {
            text: "入门", icon: 'launch', collapsible: false,
            children: [
                '/core/index.md',
                '/core/quick-start/quick-start.md',
            ]
        },
        // {
        //     text: "核心组件", icon: 'class', collapsible: false,
        //     children: [
        //         '/core/core-component/entity-codec.md',
        //         '/core/core-component/field-codec.md',
        //     ]
        // },
        {
            text: "注解驱动开发", icon: "at", collapsible: false,
            children: [
                '/core/annotation-driven/data-types.md',
                // '/core/annotation-driven/index.md',
                '/core/annotation-driven/xtream-field-annotation.md',
                '/core/annotation-driven/builtin-annotations.md',
                '/core/annotation-driven/custom-annotation.md',
            ]
        },
        {
            text: "示例", icon: "app", collapsible: false,
            children: [
                {
                    text: "示例1", icon: "app", collapsible: false, children: [
                        '/core/samples/custom-protocol-sample-01/index.md',
                        '/core/samples/custom-protocol-sample-01/flatten-style-demo.md',
                        '/core/samples/custom-protocol-sample-01/nested-style-demo.md',
                    ]
                },
                {
                    text: "示例2", icon: "app", collapsible: false, children: [
                        '/core/samples/custom-protocol-sample-02/index.md',
                        '/core/samples/custom-protocol-sample-02/flatten-style-demo.md',
                        '/core/samples/custom-protocol-sample-02/nested-style-demo.md',
                    ]
                },
            ]
        },
    ],
});
