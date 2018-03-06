package wholemusic.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import wholemusic.web.util.FileUtils;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@SuppressWarnings("unused")
public class DataSourceConfig {
    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:" + getDatabaseFile().getAbsolutePath());
        dataSourceBuilder.username(env.getProperty("secure.h2.username"));
        dataSourceBuilder.password(env.getProperty("secure.h2.password"));
        return dataSourceBuilder.build();
    }

    private File getDatabaseFile() {
        File file = new File(FileUtils.getRootDirectory(), env.getProperty("secure.h2.filename"));
        return file;
    }
}
