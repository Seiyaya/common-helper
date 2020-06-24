package xyz.seiyaya.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import xyz.seiyaya.activiti.listener.BaseListener;

/**
 * 根据指定角色查找对应的人
 * @author wangjia
 * @version 1.0
 */
@Slf4j
public class FindUserByRoleListener extends BaseListener {

    /**
     * 角色的英文名称
     */
    private Expression roleEnNameExpression;

    @Override
    protected void doNotify(DelegateTask delegateTask) {
        String roleEnName = getStringValue(roleEnNameExpression);
        log.info("roleEnName: {}",roleEnName);
    }
}
