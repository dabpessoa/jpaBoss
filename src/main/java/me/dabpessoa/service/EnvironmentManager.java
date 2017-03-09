package me.dabpessoa.service;

import me.dabpessoa.ini.IniFileManager;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by diego.pessoa on 07/03/2017.
 */
public class EnvironmentManager {

    public static final String ENVIROMENT_PROPERTIES_FILE_PATH = "environment.ini";
    public static final String PROFILE_DEVELOPMENT_SECTION = "development";
    public static final String PROFILE_PRODUCTION_SECTION = "production";

    public static enum EnvironmentSection {

        DEVELOPMENT(PROFILE_DEVELOPMENT_SECTION),
        PRODUCTION(PROFILE_PRODUCTION_SECTION);

        private String descricao;
        private EnvironmentSection(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }

    private IniFileManager iniFile;

    public EnvironmentManager() {
        this(ENVIROMENT_PROPERTIES_FILE_PATH);
    }

    public EnvironmentManager(String environmentFilePath) {
        iniFile = new IniFileManager(environmentFilePath);
    }

    public void load(String environmentFilePath) throws IOException {
        iniFile.load(environmentFilePath);
    }

    public String getEnvironmentProperty(String key) {
        return iniFile.getValue(key);
    }

    public String getEnvironmentProperty(EnvironmentSection section, String key) {
        return iniFile.getValue(section.getDescricao(), key);
    }

    public IniFileManager getIniFile() {
        return iniFile;
    }

    public Properties createProperties(String... keys) {
        return createProperties(null, keys);
    }

    public Properties createProperties(EnvironmentSection section, String... keys) {
        Properties props = new Properties();
        if (keys != null && keys.length != 0) {
            for (String key : keys) {
                if (section != null) props.put(key, getEnvironmentProperty(section, key));
                else props.put(key, getEnvironmentProperty(key));
            }
        } return props;
    }

    public static void main(String[] args) {
        EnvironmentManager em = new EnvironmentManager("env.ini");
        System.out.println(em);
        System.out.println(em.getEnvironmentProperty(EnvironmentSection.DEVELOPMENT, "dataSource.postgres.url"));
    }

}
