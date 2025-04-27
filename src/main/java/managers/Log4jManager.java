package managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jManager {
    private static Logger logger;
    
    public static Logger getLogger() {
        if (logger == null) {
            logger = LogManager.getLogger(Log4jManager.class);
        }
        return logger;
    }
}