package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.ShareMapper;
import com.javastudy.vocabease_common.service.ShareService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 笔记表 业务接口实现
 */
@Service("shareService")
public class ShareServiceImpl implements ShareService {

    @Resource
    private ShareMapper<Share, ShareQuery> shareMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Share> findListByParam(ShareQuery param) {
        return this.shareMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ShareQuery param) {
        return this.shareMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<Share> findListByPage(ShareQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Share> list = this.findListByParam(param);
        PaginationResultVO<Share> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Share bean) {
        return this.shareMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Share> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.shareMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Share> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.shareMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Share bean, ShareQuery param) {
        StringTools.checkParam(param);
        return this.shareMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ShareQuery param) {
        StringTools.checkParam(param);
        return this.shareMapper.deleteByParam(param);
    }

    /**
     * 根据ShareId获取对象
     */
    @Override
    public Share getShareByShareId(Integer shareId) {
        return this.shareMapper.selectByShareId(shareId);
    }

    /**
     * 根据ShareId修改
     */
    @Override
    public Integer updateShareByShareId(Share bean, Integer shareId) {
        return this.shareMapper.updateByShareId(bean, shareId);
    }

    /**
     * 根据ShareId删除
     */
    @Override
    public Integer deleteShareByShareId(Integer shareId) {
        return this.shareMapper.deleteByShareId(shareId);
    }

    /**
     * 保存/新增分享
     */
    @Override
    public void saveShare(Share share, Boolean isAdmin) {
        //新增
        if (share.getShareId() == null) {
            share.setCreateTime(new Date());
            share.setCollectCount(0);
            share.setReadCount(0);
            if (isAdmin)
                share.setPostType(0);
            else
                share.setPostType(1);
            share.setStatus(PostStatusEnum.NO_POST.getStatus());
            this.shareMapper.insert(share);
        }
        //修改
        else {
            Share shareDB = this.shareMapper.selectByShareId(share.getShareId());
            if (!shareDB.getShareId().equals(share.getShareId()) && !isAdmin)
                throw new BusinessException("非管理员或作者无法修改当前笔记");
            share.setCreaterId(null);
            share.setCreateTime(null);
            this.shareMapper.updateByShareId(share, share.getShareId());
        }
    }

    /**
     * 删除分享
     */
    @Override
    public void deleteShareByShareIds(String shareIds, String userId) {
        String[] shareIdArray = shareIds.split(",");

        // 1. 查询这些笔记的详细信息
        ShareQuery query = new ShareQuery();
        query.setShareIds(shareIdArray);
        List<Share> shareList = this.findListByParam(query);

        // 2. 校验：是否有已发布的笔记
        List<String> publishedTitles = new ArrayList<>();

        for (Share share : shareList) {
            // 判断笔记是否已发布
            boolean isPublished = share.getStatus() != null &&
                    share.getStatus().equals(PostStatusEnum.IS_POST.getStatus());

            if (isPublished) {
                // 🔑 关键修改：判断当前用户是否为作者
                // 如果 userId 为空，或者 userId 不等于作者 ID，说明不是本人操作，需要拦截
                // 注意：这里假设 share.getCreaterId() 是 String 类型，如果是 Integer 请相应调整
                boolean isOwner = userId != null && userId.equals(String.valueOf(share.getCreaterId()));

                // 如果不是本人，则加入禁止删除列表
                if (!isOwner) {
                    publishedTitles.add(share.getTitle());
                }
            }
        }

        // 如果还有被拦截的笔记（说明是非本人发布的笔记），则抛出异常
        if (!publishedTitles.isEmpty()) {
            throw new BusinessException("以下笔记已发布且非本人发布，禁止删除（请先下架）：" + String.join(", ", publishedTitles));
        }

        // 3. 权限校验：非管理员只能删自己的
        // (这段逻辑保留，作为双重保险，或者你可以根据需求调整)
        if (userId != null) {
            List<Share> notOwnList = shareList.stream()
                    .filter(item -> !item.getCreaterId().equals(String.valueOf(userId)))
                    .toList();
            if (!notOwnList.isEmpty())
                throw new BusinessException("非管理员或作者无法删除当前笔记");
        }

        // 4. 执行删除
        this.shareMapper.deleteByParam(query);
    }

    /**
     * 修改发布状态
     */
    public void updateShareStatus(String shareIds, Integer status) {
        ShareQuery shareQuery = new ShareQuery();
        shareQuery.setShareIds(shareIds.split(","));
        Share share = new Share();
        share.setStatus(status);
        this.updateByParam(share, shareQuery);
    }

    @Override
    public Share showShareNext(ShareQuery shareQuery, Integer currentId,
                               Integer nextType, Boolean isUpdateReadCount) {
        if (nextType == null)
            shareQuery.setShareId(currentId);
        else {
            shareQuery.setShareId(nextType);
            shareQuery.setCurrentId(currentId);
        }
        Share share = this.shareMapper.showShareNext(shareQuery);
        if (share == null && nextType == null)
            throw new BusinessException("抱歉，没有更多了");
        else if (share == null && nextType == -1)
            throw new BusinessException("已经在第一页");
        else if (share == null && nextType == 1)
            throw new BusinessException("已经在最后一页");
        if (isUpdateReadCount && share != null) {
            this.shareMapper.updateCount(1, null, currentId);
            share.setReadCount(share.getReadCount() + 1);
        }
        return share;
    }

    /**
     * 【新增】获取收藏列表中的下一个/上一个笔记
     * 逻辑：基于 app_collect 表的 create_time 排序
     *
     * @param userId    当前用户ID
     * @param currentId 当前笔记ID
     * @param nextType  1: 下一个 (时间更早), -1: 上一个 (时间更晚)
     */
    @Override
    public Share showNextCollectedShare(String userId, Integer currentId, Integer nextType) {
        if (userId == null || currentId == null || nextType == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        ShareQuery query = new ShareQuery();
        query.setUserId(userId);
        query.setCurrentId(currentId);    // 当前单词ID
        query.setNextType(nextType);      // 方向
        // 确保只查有效的单词
        query.setStatus(1);
        query.setQueryContent(true);
        Share share = this.shareMapper.selectNextCollectedShare(query);
        if (share == null) {
            if (nextType == 1)
                throw new BusinessException("已经是最后一个收藏了");
            else if (nextType == -1)
                throw new BusinessException("已经是第一个收藏了");
        }
        return share;
    }

    @Override
    public void updateCollectCountById(String shareId) {
        this.shareMapper.updateCount(1,1, Integer.valueOf(shareId));
    }

    @Override
    public void updateReadCountById(Integer shareId) {
        this.shareMapper.updateReadCount(shareId);
    }
}