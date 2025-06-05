package com.whisper.pro.service;

import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TextResult;

public interface SttJobService {
    /**
     * 添加一个STT任务
     * @param job
     */
	public void addWhisperJob(SttJob job);

    /**
     * 查询STT任务详情
     * @param id
     * @return
     */
    public SttJob get(Integer id);

    /**
     * 更新STT任务详情
     * @param job
     * @return
     */
    public void updateJob(SttJob job);

    /**
     * 取消任务
     *  1、设置任务状态为取消状态
     * @param id    STTJob标识
     */
    public void cancleJob(Integer id);

    /**
     * 启用任务
     *  1、设置任务状态为初始状态
     * @param id    STTJob标识
     */
    public void enableJob(Integer id);

    /**
     * 计算对应音频文件原始文本与STT文本相识度
     * @param type  计算方法
     * @param id    STTJob标识
     * @return
     */
    public Double textRecognition(Integer type, Integer id);

    /**
     * 根据STTJob标识获取文本详情
     * @param id
     * @return
     */
    public TextResult getTextResult(Integer id);

    /**
     * 查询相应⽂本内容
     * @param language
     * @param id
     * @param ori
     * @return
     */
    public String query(String language, Integer id, String ori);
}
