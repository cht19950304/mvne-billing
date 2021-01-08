package com.cmit.mvne.billing.rating.gprs.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/20
 */
@Slf4j
public class PropertiesUtil {

    private static final String PLACEHOLDER_START = "${";

    private static Properties props;

    static {
        String filename = "rating-gprs.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename), "UTF-8"));
            resolvePlaceHolders(props);

        } catch (IOException e) {
            log.error("Load {} failed ! ", filename, e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key);
        if(!StringUtils.isEmpty(value)) {
            return value;
        }
        return null;
    }

    public static String getProperty(String key, String defaultValue){
        String value = props.getProperty(key);
        if( !StringUtils.isEmpty(value)){
            return value;
        }
        return defaultValue;
    }


    /**
     * Handles interpolation processing for all entries in a properties object.
     *
     * @param configurationValues The configuration map.
     */
    private static void resolvePlaceHolders(Map<?,?> configurationValues) {
        Iterator itr = configurationValues.entrySet().iterator();
        while ( itr.hasNext() ) {
            final Map.Entry entry = ( Map.Entry ) itr.next();
            final Object value = entry.getValue();
            if ( value != null && String.class.isInstance( value ) ) {
                final String resolved = resolvePlaceHolder( ( String ) value );
                if ( !value.equals( resolved ) ) {
                    if ( resolved == null ) {
                        itr.remove();
                    }
                    else {
                        entry.setValue( resolved );
                    }
                }
            }
        }
    }


    /**
     * Handles interpolation processing for a single property.
     *
     * @param property The property value to be processed for interpolation.
     * @return The (possibly) interpolated property value.
     */
    private static String resolvePlaceHolder(String property) {
        if ( property.indexOf( PLACEHOLDER_START ) < 0 ) {
            return property;
        }
        StringBuilder buff = new StringBuilder();
        char[] chars = property.toCharArray();
        for ( int pos = 0; pos < chars.length; pos++ ) {
            if ( chars[pos] == '$' ) {
                // peek ahead
                if ( chars[pos+1] == '{' ) {
                    // we have a placeholder, spin forward till we find the end
                    String systemPropertyName = "";
                    int x = pos + 2;
                    for (  ; x < chars.length && chars[x] != '}'; x++ ) {
                        systemPropertyName += chars[x];
                        // if we reach the end of the string w/o finding the
                        // matching end, that is an exception
                        if ( x == chars.length - 1 ) {
                            throw new IllegalArgumentException( "unmatched placeholder start [" + property + "]" );
                        }
                    }
                    String systemProperty = extractFromSystem( systemPropertyName );
                    buff.append( systemProperty == null ? "" : systemProperty );
                    pos = x + 1;
                    // make sure spinning forward did not put us past the end of the buffer...
                    if ( pos >= chars.length ) {
                        break;
                    }
                }
            }
            buff.append( chars[pos] );
        }
        String rtn = buff.toString();
        return StringUtils.isEmpty( rtn ) ? null : rtn;
    }

    private static String extractFromSystem(String systemPropertyName) {
        try {
            return System.getProperty( systemPropertyName );
        }
        catch( Throwable t ) {
            return null;
        }
    }

}
