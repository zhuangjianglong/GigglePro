package com.whisper.pro.service;

import com.whisper.pro.pojo.bo.TranslateJob;

public interface TranslateJobService {

    /**
     * 添加一个翻译任务
     * @param job
     */
    public void addJob(TranslateJob job);

    /**
     * 获取翻译任务详情
     * @param id
     * @return
     */
    public TranslateJob get(Integer id);

    /**
     * 更新翻译任务信息
     * @param job
     */
    public void updateJob(TranslateJob job);

}
