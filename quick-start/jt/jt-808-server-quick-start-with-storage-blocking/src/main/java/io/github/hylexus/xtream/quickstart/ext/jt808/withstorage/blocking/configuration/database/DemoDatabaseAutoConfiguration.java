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

package io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database;

import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.clickhouse.DemoClickhouseConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.logging.DemoLoggingDatabaseConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.mysql.DemoMysqlConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.postgres.DemoPostgresConfiguration;
import io.github.hylexus.xtream.quickstart.ext.jt808.withstorage.blocking.configuration.database.tdengine.mysql.DemoTDengineConfiguration;
import org.springframework.context.annotation.Import;

@Import({
        DemoClickhouseConfiguration.class,
        DemoMysqlConfiguration.class,
        DemoTDengineConfiguration.class,
        DemoPostgresConfiguration.class,
        DemoLoggingDatabaseConfiguration.class,
})
public class DemoDatabaseAutoConfiguration {
}
