package utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Velocity工具类，主要用于代码生成
 */
public class VelocityUtils {
    /**
     * velocity配置
     */
    private static final Properties INIT_PROP;

    static {
        INIT_PROP = new Properties();
        INIT_PROP.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        INIT_PROP.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
    }

    private VelocityUtils() {
        throw new UnsupportedOperationException();
    }

    public static String generate(String template, Map<String, Object> map) {
        VelocityEngine velocityEngine = new VelocityEngine(INIT_PROP);
        VelocityContext velocityContext = new VelocityContext();
        if (map != null) {
            map.forEach(velocityContext::put);
        }

        StringWriter stringWriter = new StringWriter();
        try {
            velocityEngine.evaluate(velocityContext, stringWriter, "Velocity Code Generate", template);
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder("在生成代码时，模板发生了如下语法错误：\n");
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            builder.append(writer);
            return builder.toString().replace("\r", "");
        }
        String code = stringWriter.toString();
        StringBuilder sb = new StringBuilder(code);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }
}
