package com.whisper.pro.service;

import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.dao.TranslateJobDao;
import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TranslateJob;
import com.whisper.pro.pojo.po.TranslateJobPO;
import com.whisper.pro.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TranslateJobServiceImpl implements TranslateJobService {
	
	protected static Logger logger = LoggerFactory.getLogger(TranslateJobServiceImpl.class);

	@Autowired
	private SttJobService sttJobService;
	@Autowired
	private TranslateJobDao translateJobDao;
	@Autowired
	private AppConfiguration appConfiguration;

	@Override
	@Transactional
	public void addJob(TranslateJob job) {

		TranslateJobPO po = new TranslateJobPO();
		po.setType(job.getType());
		po.setStatus(1);
		po.setTargetLanguage(job.getTargetLanguage());
		po.setSttJobId(job.getSttJobId());

		translateJobDao.insert(po);
	}

	@Override
	public TranslateJob get(Integer id) {
		TranslateJobPO jobInfo = translateJobDao.selectById(id);

		TranslateJob translateJob = new TranslateJob();
		translateJob.setId(jobInfo.getId());
		translateJob.setStatus(jobInfo.getStatus());
		translateJob.setType(jobInfo.getType());

		translateJob.setTargetLanguage(jobInfo.getTargetLanguage());
		translateJob.setRetryCount(jobInfo.getRetryCount());

		SttJob sttJobInfo = sttJobService.get(jobInfo.getSttJobId());
		if(sttJobInfo == null){
			throw new BusinessException(GlobalResultEnum.FAILURE,"对应数据错误");
		}
		translateJob.setSttJobId(sttJobInfo.getId());
		translateJob.setSttText(sttJobInfo.getResultText());

		if(StringUtils.isNotBlank(jobInfo.getResultPath())){
			translateJob.setResultText(FileUtils.readFile(appConfiguration.getBasePath() + jobInfo.getResultPath()));
		}
		return translateJob;
	}

	@Override
	@Transactional
	public void updateJob(TranslateJob job) {
		TranslateJobPO po = new TranslateJobPO();
		po.setId(job.getId());
		po.setStatus(job.getStatus());

		if(job.getStatus() == 0){
			String textPath = String.format("/%s.txt", UUID.randomUUID().toString());
			FileUtils.createAndWriteFile(appConfiguration.getBasePath()+ textPath, job.getResultText());
			po.setResultPath(textPath);
		}else{
			if(job.getRetryCount() == null){
				po.setRetryCount(1);
			}else{
				po.setRetryCount(job.getRetryCount()+1);
			}
		}

		translateJobDao.updateById(po);
	}

}
