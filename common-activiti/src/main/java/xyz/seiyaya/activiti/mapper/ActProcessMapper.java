package xyz.seiyaya.activiti.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.bean.dto.ProcessSearchDTO;
import xyz.seiyaya.activiti.bean.vo.ActProcessVO;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/11 16:18
 */
@Mapper
public interface ActProcessMapper extends tk.mybatis.mapper.common.Mapper<ActProcess> {

    /**
     * 获取审核的流程节点列表
     * @param procInsId
     * @return
     */
    List<ActProcess> getAuditProcessList(@Param("procInstId") String procInsId);

    /**
     * 获取待审流程
     * @param processSearchDTO
     * @return
     */
    List<ActProcessVO> getTodoProcessList(ProcessSearchDTO processSearchDTO);
}
