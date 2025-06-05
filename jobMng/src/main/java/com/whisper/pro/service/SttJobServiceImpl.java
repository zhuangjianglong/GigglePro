package com.whisper.pro.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.whisper.pro.cache.TextCache;
import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.dao.SttJobDao;
import com.whisper.pro.dao.TranslateJobDao;
import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TextResult;
import com.whisper.pro.pojo.bo.TranslateItemResult;
import com.whisper.pro.pojo.po.SttJobPO;
import com.whisper.pro.pojo.po.TranslateJobPO;
import com.whisper.pro.utils.FileUtils;
import com.whisper.pro.utils.TextRecognitionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SttJobServiceImpl implements SttJobService {
	
	protected static Logger logger = LoggerFactory.getLogger(SttJobServiceImpl.class);

	@Autowired
	private SttJobDao sttJobDao;
	@Autowired
	private AppConfiguration appConfiguration;
	@Autowired
	private TranslateJobDao translateJobDao;

	@Override
	public void addWhisperJob(SttJob job) {

	}

	@Override
	public SttJob get(Integer id) {
		SttJobPO jobInfo = sttJobDao.selectById(id);

		SttJob whisperJob = new SttJob();
		whisperJob.setId(jobInfo.getId());
		whisperJob.setStatus(jobInfo.getStatus());
		whisperJob.setType(jobInfo.getType());
		whisperJob.setAudioPath(appConfiguration.getBasePath() + jobInfo.getAudioPath());
		whisperJob.setRetryCount(jobInfo.getRetryCount());

		if(StringUtils.isNotBlank(jobInfo.getOriginalTextPath())){
			whisperJob.setOriginalText(FileUtils.readFile(appConfiguration.getBasePath() + jobInfo.getOriginalTextPath()));
		}

		if(StringUtils.isNotBlank(jobInfo.getTextPath())){
			whisperJob.setResultText(FileUtils.readFile(appConfiguration.getBasePath() + jobInfo.getTextPath()));
		}


		return whisperJob;
	}

	@Override
	@Transactional
	public void updateJob(SttJob job) {
		SttJobPO po = new SttJobPO();
		po.setId(job.getId());
		po.setStatus(job.getStatus());

		if(job.getStatus() == 0){
			String textPath = String.format("/%s.txt", UUID.randomUUID().toString());
			FileUtils.createAndWriteFile(appConfiguration.getBasePath()+ textPath, job.getResultText());
			po.setTextPath(textPath);
		}else{
			if(job.getRetryCount() == null){
				po.setRetryCount(1);
			}else{
				po.setRetryCount(job.getRetryCount()+1);
			}
		}

		sttJobDao.updateById(po);

	}

	@Override
	public void cancleJob(Integer id) {
		SttJobPO po = new SttJobPO();
		po.setId(id);
		//4 取消状态
		po.setStatus(4);
		sttJobDao.updateById(po);
	}

	@Override
	public void enableJob(Integer id) {
		SttJobPO po = new SttJobPO();
		po.setId(id);
		//1 新建状态
		po.setStatus(1);
		sttJobDao.updateById(po);
	}

	@Override
	public Double textRecognition(Integer type, Integer id) {
		SttJob jobInfo = this.get(id);
		//确定任务已结束
		if(jobInfo.getStatus() == 0){
			//确定对应音频存在原始文本
			if(StringUtils.isNotBlank(jobInfo.getOriginalText())){

				//去除原始文本干扰字符
				String oriStr = jobInfo.getOriginalText().replace("“", "")
						.replace("”", "");

				//STT文本
				JSONObject jo = JSONObject.parseObject(jobInfo.getResultText());

				Double score = null;

				switch (type){
					case 1:
						score = TextRecognitionUtils.calculateSimilarityByLevenshtein(
									oriStr.replace("\n", ""),
									jo.getString("text"));
						break;
					case 2:
						List<String> strs = Arrays.stream(oriStr.split("\n")).collect(Collectors.toList());

						List<JSONObject> segments = JSON.parseObject(jo.getString("segments"), new TypeReference<List<JSONObject>>() {});
						List<String> sttTexts = segments.stream().map(s -> s.getString("text")).collect(Collectors.toList());
						score = TextRecognitionUtils.calculateSimilarityByLevenshtein(strs,sttTexts);
						break;
					case 3:
						score = TextRecognitionUtils.calculateSimilaritybyJaccard(
									oriStr.replace("\n", ""),
									jo.getString("text"));
						break;

				}
				return score;
			}else{
				throw new BusinessException(GlobalResultEnum.FAILURE, "job original text is empty.");
			}
		}else{
			throw new BusinessException(GlobalResultEnum.FAILURE, "job status is not 0.");
		}
	}

	@Override
	public TextResult getTextResult(Integer id) {
		TextResult textResult = new TextResult();

		SttJobPO jobInfo = sttJobDao.selectById(id);
		if(jobInfo == null){
			throw new BusinessException(GlobalResultEnum.FAILURE, "job not found.");
		}

		textResult.setId(id);
		textResult.setAudioPath(appConfiguration.getBasePath() + jobInfo.getAudioPath());

		if(StringUtils.isNotBlank(jobInfo.getOriginalTextPath())){
			textResult.setOriginalText(FileUtils.readFile(appConfiguration.getBasePath() + jobInfo.getOriginalTextPath()));
		}

		if(StringUtils.isNotBlank(jobInfo.getTextPath())){
			String sttText = FileUtils.readFile(appConfiguration.getBasePath() + jobInfo.getTextPath());
			//STT结果文本
			JSONObject jo = JSONObject.parseObject(sttText);
			textResult.setLanguage(jo.getString("language"));
			textResult.setText(jo.getString("text"));

			List<TextResult.LanguageText> languageTextList = new ArrayList<>();

			//处理stt片段
			List<JSONObject> segments = JSON.parseObject(jo.getString("segments"), new TypeReference<List<JSONObject>>() {});
			List<String> sttSegments = segments.stream().map(s -> s.getString("text")).collect(Collectors.toList());

			QueryWrapper<TranslateJobPO> wrapper = new QueryWrapper<>();
			wrapper.eq("stt_job_id", jobInfo.getId());
			wrapper.eq("status", 0);
			List<TranslateJobPO> translateJobs = translateJobDao.selectList(wrapper);
			if(translateJobs != null && translateJobs.size() > 0){
				TextResult.LanguageText languageText = null;
				for(TranslateJobPO translateJob : translateJobs){
					languageText = new TextResult.LanguageText();
					languageText.setLanguage(translateJob.getTargetLanguage());

					List<TranslateItemResult> translateTextSegments = JSON.parseObject(
							FileUtils.readFile(appConfiguration.getBasePath() + translateJob.getResultPath()),
							new TypeReference<List<TranslateItemResult>>() {});

					List<TextResult.LanguageSegmentText> segmentList = new ArrayList<>();
					for(TranslateItemResult item : translateTextSegments){
						TextResult.LanguageSegmentText translateSeq = new TextResult.LanguageSegmentText();
						translateSeq.setSeq(item.getSeq());
						translateSeq.setText(sttSegments.get(item.getSeq()));
						translateSeq.setTranslateText(item.getText());

						segmentList.add(translateSeq);
					}

					languageText.setSegmentList(segmentList);
					languageTextList.add(languageText);
				}
			}

			textResult.setLanguageTextList(languageTextList);
		}

		this.setCache(textResult);
		return textResult;
	}

	@Override
	public String query(String language, Integer id, String ori) {
		return TextCache.get(language,id,ori);
	}

	private void setCache(TextResult textResult){
		//放入音频原始文本
		TextCache.set(textResult.getLanguage(),textResult.getId(),TextCache.ORI_TEXT,textResult.getOriginalText());

		//放入翻译的的文本
		List<TextResult.LanguageText> languageTextList = textResult.getLanguageTextList();
		if(languageTextList !=null && languageTextList.size()>0){
			for(TextResult.LanguageText item:languageTextList){
				TextCache.set(item.getLanguage(),textResult.getId(),TextCache.ORI_AUDIO,
						item.getSegmentList().stream().map(TextResult.LanguageSegmentText::getTranslateText).collect(Collectors.joining())
				);
			}
		}
	}
}
