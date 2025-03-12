package com.itkd.picture.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkd.picture.exception.BusinessException;
import com.itkd.picture.exception.ErrorCode;
import com.itkd.picture.exception.ThrowUtils;
import com.itkd.picture.mapper.SpaceMapper;
import com.itkd.picture.model.dto.space.SpaceQueryRequest;
import com.itkd.picture.model.entity.Picture;
import com.itkd.picture.model.entity.Space;
import com.itkd.picture.model.entity.User;
import com.itkd.picture.model.enums.SpaceLevelEnum;
import com.itkd.picture.model.vo.PictureVO;
import com.itkd.picture.model.vo.SpaceVO;
import com.itkd.picture.model.vo.UserVO;
import com.itkd.picture.service.SpaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kdc
 * @description 针对表【space(空间)】的数据库操作Service实现
 * @createDate 2025-03-11 08:30:02
 */
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
        implements SpaceService {

    @Resource
    private UserServiceImpl userService;

    /**
     * 校验空间信息
     * @param space
     */
    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        //从对象中取值
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum enumByValue = SpaceLevelEnum.getEnumByValue(spaceLevel);
        if(add){
            ThrowUtils.throwIf(spaceName == null, ErrorCode.PARAMS_ERROR, "空间名不能为空");
            ThrowUtils.throwIf(enumByValue == null, ErrorCode.PARAMS_ERROR, "空间等级不能为空");
        }
        if(spaceLevel!=null&&enumByValue==null){
            ThrowUtils.throwIf(enumByValue == null, ErrorCode.PARAMS_ERROR, "没有这个空间级别");
        }
        if(spaceName!=null&&spaceName.length()>30){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"空间名过长");
        }



    }


    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        SpaceLevelEnum enumByValue = SpaceLevelEnum.getEnumByValue(space.getSpaceLevel());
        if (enumByValue!=null){
            long maxSize = enumByValue.getMaxSize();
            if (space.getMaxSize()==null){
                space.setMaxSize(maxSize);
            }
            if (space.getMaxCount()==null){
                space.setMaxCount(enumByValue.getMaxCount());
            }
        }
    }

    /**
     * 获取查询条件
     * @param spaceQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest) {
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        if (spaceQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = spaceQueryRequest.getId();
        Long userId = spaceQueryRequest.getUserId();
        String spaceName = spaceQueryRequest.getSpaceName();
        Integer spaceLevel = spaceQueryRequest.getSpaceLevel();
        String sortField = spaceQueryRequest.getSortField();
        String sortOrder = spaceQueryRequest.getSortOrder();
        // 判断条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(spaceName), "spaceName", spaceName);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spaceLevel), "spaceLevel", spaceLevel);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField),sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    /**
     * 获取空间
     * @param space
     * @param request
     * @return
     */
    @Override
    public SpaceVO getSpaceVO(Space space, HttpServletRequest request) {
        // 对象转封装类
        SpaceVO spaceVO = SpaceVO.objToVo(space);
        // 关联查询用户信息
        Long userId = space.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            spaceVO.setUser(userVO);
        }
        return spaceVO;
    }

    /**
     * 获取空间分页
     * @param spacePage
     * @param request
     * @return
     */
    @Override
    public Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request) {
        List<Space> spaceList = spacePage.getRecords();
        Page<SpaceVO> spaceVOPage = new Page<>(spacePage.getCurrent(), spacePage.getSize(), spacePage.getTotal());
        if (CollUtil.isEmpty(spaceList)) {
            return spaceVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpaceVO> spaceVOList = spaceList.stream().map(SpaceVO::objToVo).collect(Collectors.toList());
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spaceList.stream().map(Space::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        spaceVOList.forEach(spaceVO -> {
            Long userId = spaceVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spaceVO.setUser(userService.getUserVO(user));
        });
        spaceVOPage.setRecords(spaceVOList);
        return spaceVOPage;
    }


}




