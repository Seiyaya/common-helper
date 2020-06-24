package xyz.seiyaya.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
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

    @Override
    public void notify(DelegateTask delegateTask) {
        before();
        doNotify(delegateTask);
        after();
    }

    /**
     * 子类需要实现的查找人的逻辑
     * @param delegateTask
     */
    protected abstract void doNotify(DelegateTask delegateTask);

    public void before(){

    }

    public void after(){
        // 查找到人之后需要同步自身业务系统的数据
    }
}
