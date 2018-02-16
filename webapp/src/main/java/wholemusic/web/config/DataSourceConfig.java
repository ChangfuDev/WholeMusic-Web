package wholemusic.web.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import wholemusic.web.util.FileUtils;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@SuppressWarnings("unused")
public class DataSourceConfig {
    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:" + getDatabaseFile().getAbsolutePath());
        dataSourceBuilder.username("");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }

    private static File getDatabaseFile() {
        File file = new File(FileUtils.getRootDirectory(), "app.db");
        return file;
    }
}
