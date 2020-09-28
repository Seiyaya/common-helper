package xyz.seiyaya.activiti.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.activiti.bean.TransactionUser;

@Mapper
public interface TransactionUserMapper extends tk.mybatis.mapper.common.Mapper<TransactionUser> {
}
