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

package io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.configuration.minio;

import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.configuration.props.DemoAppProps;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.service.ObjectStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 当前类是为了给 Minio 做初始化
 *
 * <ol>
 *     <li>bucket 不存在时
 *     <ol>
 *         <li>创建 bucket</li>
 *         <li>设置 bucket 可以匿名访问(仅仅是为了示例方便，生产环境你不应该这么做！！！)</li>
 *     </ol>
 *     </li>
 *     <li>bucket 存在时，不做其他额外操作</li>
 * </ol>
 *
 * @author hylexus
 */
@Component
public class MinioInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MinioInitializer.class);
    private final DemoAppProps demoAppProps;
    private final ObjectStorageService minioService;

    public MinioInitializer(DemoAppProps demoAppProps, ObjectStorageService minioService) {
        this.demoAppProps = demoAppProps;
        this.minioService = minioService;
    }

    @Override
    public void run(String... args) {
        // 注意：这里是在项目启动阶段，所以可以调用 .block() 这种[阻塞]方法
        // 运行时，请不要直接或间接调用阻塞方法
        final String attachmentFileBucket = this.demoAppProps.getAttachmentServer().getRemoteStorageBucketName();
        if (Boolean.TRUE.equals(this.minioService.bucketExists(attachmentFileBucket).block())) {
            log.info("Bucket [{}] already exists", attachmentFileBucket);
        } else {
            log.info("Bucket [{}] does not exist, creating it ...", attachmentFileBucket);
            this.minioService.makeBucket(attachmentFileBucket).block();
            log.info("Bucket [{}] created", attachmentFileBucket);

            log.info("Setting bucket [{}]] policy(accessible with anonymous for debug purpose) ...", attachmentFileBucket);
            this.minioService.makeBucketAccessibleWithAnonymous(attachmentFileBucket).block();
            log.info("Bucket [{}] accessible with anonymous", attachmentFileBucket);
        }
    }
}
