package xyz.seiyaya.common.manager.controller;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.config.Constant;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.common.manager.bean.dict.dto.DictDTO;
import xyz.seiyaya.common.manager.bean.dict.model.Dict;
import xyz.seiyaya.common.manager.bean.dict.vo.DictVO;
import xyz.seiyaya.common.manager.helper.EnumHelper;
import xyz.seiyaya.common.manager.service.DictService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:09
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Resource
    private DictService dictService;

    @RequestMapping("/query")
    public ResultBean query(@RequestBody DictDTO dictDTO) {
        ResultBean resultBean = new ResultBean();
        if (StringHelper.isEmpty(dictDTO.getDictNo())) {
            return resultBean.setParamError();
        }
        String[] dictNoArray = dictDTO.getDictNo().split(",");
        List<DictVO> dictList = Lists.newArrayList();
        for (int i = 0; i < dictNoArray.length; i++) {
            String dictNo = dictNoArray[i];
            // 从缓存中获取对应的字典数据
            String dictString = EnumHelper.getListString(dictNo);
            if (StringHelper.isNotEmpty(dictString)) {
                DictVO dictVO = new DictVO(dictString, dictNo);
                dictList.add(dictVO);
            } else {
                // 从数据库获取
                Dict dbDict = dictService.queryDictByDictNo(dictNo);
                if (dbDict == null) {
                    String dictId = dbDict.getDictId();
                    if(Objects.equals(Constant.ByteConstant.BYTE_1, dbDict.getIsDynamic()) ){
                        // 查询动态数据字典
                        DictVO dictVO = dictService.queryDynamicDict(dictId,dictDTO);
                        dictList.add(dictVO);
                    }else{
                        // 查询静态数据字典
                        DictVO dictVO = dictService.queryStaticDict(dictId);
                    }
                }
            }
        }
        return resultBean;
    }
}
