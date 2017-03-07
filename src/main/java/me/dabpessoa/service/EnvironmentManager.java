package me.dabpessoa.service;

import org.springframework.stereotype.Service;
import me.dabpessoa.ini.IniFileManager;

import java.io.IOException;

/**
 * Created by diego.pessoa on 07/03/2017.
 */
@Service
public class EnvironmentManager {

    public static enum SECTION {

        DEVELOPMENT("development"),
        PRODUCTION("production");

        private String descricao;
        private SECTION(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }

    private IniFileManager iniFile;

    public EnvironmentManager() {}

    public EnvironmentManager(String environmentFilePath) {
        iniFile = new IniFileManager(environmentFilePath);
    }

    public void load(String environmentFilePath) throws IOException {
        iniFile.load(environmentFilePath);
    }

    public String getEnvironmentProperty(String key) {
        return iniFile.getValue(key);
    }

    public String getEnvironmentProperty(SECTION section, String key) {
        return iniFile.getValue(section.getDescricao(), key);
    }

    public IniFileManager getIniFile() {
        return iniFile;
    }

    public static void main(String[] args) {
        EnvironmentManager em = new EnvironmentManager("env.ini");
        System.out.println(em);
        System.out.println(em.getEnvironmentProperty(SECTION.DEVELOPMENT, "dataSource.postgres.url"));
    }

}
