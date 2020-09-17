package xyz.seiyaya.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/16 16:14
 */
@Slf4j
public class ApolloStarter {

    public static void main(String[] args) {
        Config config = ConfigService.getAppConfig();
        String someKey = "timeout";
        String someDefaultValue = "0";
        String value = config.getProperty(someKey, someDefaultValue);

        config.addChangeListener(changeEvent -> {
            System.out.println("Changes for namespace " + changeEvent.getNamespace());
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                System.out.println(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
            }
        });


        log.info("key:{} --> value:{}",someKey,value);
    }
}
