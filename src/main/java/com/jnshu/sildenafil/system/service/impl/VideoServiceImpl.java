package com.jnshu.sildenafil.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jnshu.sildenafil.system.domain.Teacher;
import com.jnshu.sildenafil.system.domain.Video;
import com.jnshu.sildenafil.system.mapper.TeacherDao;
import com.jnshu.sildenafil.system.mapper.VideoDao;
import com.jnshu.sildenafil.system.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnshu.sildenafil.util.MyPage;
import com.jnshu.sildenafil.util.ServiceExcetpion;
import com.jnshu.sildenafil.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Taimur
 * @since 2018-10-31
 */
@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {
    private static final long NOW = System.currentTimeMillis();

    private final VideoDao videoDao;
    private final TeacherDao teacherDao;

    @Autowired(required = false)
    public VideoServiceImpl(VideoDao videoDao, TeacherDao teacherDao) {
        this.videoDao = videoDao;
        this.teacherDao = teacherDao;
    }

    /**
     *后台视频列表（分页）
     * @param page 第几页
     * @param size 每页条数
     * @param title 标题
     * @param type 视频类型：0card，1banner
     * @param grade 年级
     * @param subject 科目
     * @param likeStart 点赞数
     * @param likeEnd 点赞数
     * @param collectStart 收藏数
     * @param collectEnd 收藏数
     * @param teacher 老师名
     * @param status 上下架状态
     * @return 视频每页详情
     */
    @Override
    public IPage getPage(Integer page, Integer size, String title, Integer type, Integer grade, Integer subject,
                         Integer likeStart, Integer likeEnd, Integer collectStart, Integer collectEnd,
                         String teacher, Integer status) {
        log.info("args for getPage is: {}", page, size, title, type, grade, subject,
                                            likeStart, likeEnd, collectStart, collectEnd, teacher, status);
        IPage<Video> findPage = new MyPage<Video>(page, size).setDesc("update_at");
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        if (teacher != null) {
            QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
            teacherQueryWrapper.select("id").like("nickname", teacher);
            List<Teacher> teacherList = teacherDao.selectList(teacherQueryWrapper);
            List idList = new ArrayList<>();
            for (Teacher s: teacherList) {
                idList.add(s.getId());
            }
            videoQueryWrapper.in("teacher_id", idList);
        }
        videoQueryWrapper
                    .like(StringUtils.isNotEmpty(title), "title", title)
                    .eq(type != null, "type", type)
                    .eq(grade != null, "grade", grade)
                    .eq(subject != null, "subject", subject)
                    .ge(likeStart != null, "like_amount", likeStart)
                    .le(likeEnd != null, "like_amount", likeEnd)
                    .ge(collectStart != null, "collection_amount", collectStart)
                    .le(collectEnd != null, "collection_amount", collectEnd)
                    .eq(status != null, "status", status);
        IPage videoIPage = videoDao.selectPage(findPage, videoQueryWrapper);
        if (videoIPage.getRecords().size() > 0) {
            log.info("Video size is: {}", videoIPage.getRecords().size());
        } else {
            log.error("List is empty!");
        }
        return videoIPage;
    }

    /**
     * 前台通过id获取视频
     * @param videoId 视频id
     * @return 查询到的视频详情
     */
    @Override
    public Video getVideoById(Long videoId) {
        log.info("args for saveVideo is: {}", videoId);
        if (videoId != null) {
            Video video = videoDao.selectById(videoId);
            String teacherNickname = teacherDao.selectById(video.getTeacherId()).getNickname();

            log.info("result of getVideoById is: {}", video);
            return video;
        } else {
            log.error("args is null");
            return null;
        }
    }

    /**
     * 后台新增视频详情
     * @param video 视频
     * @return 新增视频详情
     */
    @Override
    public Video saveVideo(Video video) {
        log.info("args for saveVideo is: {}", video);
        try {
            ValidationUtils.validate(video);
            video.setCreateAt(NOW);
            video.setUpdateAt(NOW);
            //后台管理员userName
            video.setCreateBy("userName");
            //后台管理员userName
            video.setUpdateBy("userName");
            if (video.getType() == 1) {
                video.setCover(video.getCover());
            } else if (video.getType() == 0) {
                video.setCover(null);
            } else {
                log.info("type false.");
            }
            videoDao.insert(video);
            return video;
        } catch (ServiceExcetpion serviceExcetpion) {
            log.error("服务出错: {}", serviceExcetpion.getMessage());
            return null;
        } catch (NullPointerException npe) {
            log.error("F! NullPointerException : {}", (Object) npe.getStackTrace());
            return null;
        }
    }

    /**
     *后台通过id删除视频
     * @param videoId 视频id
     * @return 是否成功删除视频
     */
    @Override
    public Boolean removeVideoById(Long videoId) {
        log.info("args for removeVideoById is: {}", videoId);
        if (videoId != null) {
            int flag = videoDao.deleteById(videoId);
            log.info("result of removeVideoById is: {}", flag);
            return true;
        } else {
            log.error("args is null");
            return null;
        }
    }

    /**
     *后台更新视频详情
     * @param video 视频
     * @return 更新后视频id
     */
    @Override
    public Long updateVideo(Video video) {
        Long videoId = video.getId();
        log.info("args for updateVideo is: {}", video);
        try {
            ValidationUtils.validate(video);
            video.setUpdateAt(NOW);
            //后台管理员userName
            video.setUpdateBy("admin");
            video.setStatus(0);
            if (video.getType() == 1) {
                video.setCover(video.getCover());
            } else if (video.getType() == 0) {
                video.setCover(null);
            } else {
                log.info("illegal type.");
            }
            videoDao.updateById(video);
            return videoId;
        } catch (ServiceExcetpion serviceExcetpion) {
            log.error("service bug: {}", serviceExcetpion.getMessage());
            return null;
        } catch (NullPointerException npe) {
            log.error("F! NullPointerException: {}", (Object) npe.getStackTrace());
            return null;
        } catch (Throwable t) {
            log.error("unknown error: {}", t.getMessage());
            return null;
        }
    }


}
