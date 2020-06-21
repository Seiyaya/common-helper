package xyz.seiyaya.demo.listener;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author wangjia
 * @version 1.0
 */
public abstract class BaseListener implements TaskListener {

    public String getStringValue(Expression expression){
        return expression.getExpressionText();
    }
}
