package com.city.city_collector.admin.city.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 */
public class Logger {

    public static Integer LOG_LEVEL_ERROR     = 1;
    public static Integer LOG_LEVEL_WARNING   = 2;
    public static Integer LOG_LEVEL_INFO      = 3;
    private static final Logger m_logger = new Logger();

    private Integer m_currentLevel  = -1;
    private Logger() {
        m_currentLevel = Logger.LOG_LEVEL_INFO;
    }

    public void Log(Integer logLevel,String info) {
        String fiter="(U)";
        if(logLevel<=this.getLogLevel()){
            if(logLevel==Logger.LOG_LEVEL_ERROR)
            {
                fiter="(E)";
            }else if(logLevel==Logger.LOG_LEVEL_WARNING)
            {
                fiter="(W)";
            }else if(logLevel==Logger.LOG_LEVEL_INFO)
            {
                fiter="(I)";
            }
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + fiter +" : " +info);
    }

    public void setLogLevel(Integer level){
        this.m_currentLevel = level;
    }
    public Integer getLogLevel(){
        return this.m_currentLevel;
    }
    public static Logger getInstance() {
        return m_logger;
    }
}
