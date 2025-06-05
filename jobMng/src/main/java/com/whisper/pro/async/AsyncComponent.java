package com.whisper.pro.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.pattern.translate.TranslateStrategyContext;
import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TranslateItemResult;
import com.whisper.pro.pojo.bo.TranslateJob;
import com.whisper.pro.service.SttJobService;
import com.whisper.pro.service.TranslateJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AsyncComponent {
    protected static Logger logger = LoggerFactory.getLogger(AsyncComponent.class);

    @Autowired
    private SttJobService sttJobService;
    @Autowired
    private TranslateJobService translateJobService;
    @Autowired
    private AppConfiguration appConfiguration;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TranslateStrategyContext translateStrategyContext;

    @Async
    public void startSttJob(SttJob job) {
        logger.info("Thread: [{}], start sst job...", Thread.currentThread().getName());

        try{
            //调用 whisper python程序
            String sttText = this.callWhisper(job.getAudioPath());

            job.setStatus(0);
            job.setResultText(sttText);
        }catch (BusinessException e){
            job.setStatus(3);
        }

        sttJobService.updateJob(job);

        logger.info("Thread: [{}], sst job end.", Thread.currentThread().getName());
    }

    @Async
    public void startTranslateJob(TranslateJob job) {
        logger.info("Thread: [{}], start translte job...", Thread.currentThread().getName());

        try{
            JSONObject sttTextObj = JSONObject.parseObject(job.getSttText());
            List<JSONObject> segments = JSON.parseObject(sttTextObj.getString("segments"), new TypeReference<List<JSONObject>>() {});
            List<String> sttTexts = segments.stream().map(s -> s.getString("text")).collect(Collectors.toList());
            List<TranslateItemResult> resultText = translateStrategyContext.executeStrategy(job.getType(), sttTexts, job.getTargetLanguage());
            job.setStatus(0);
            job.setResultText(JSONObject.toJSONString(resultText));
        }catch (BusinessException e){
            job.setStatus(3);
        }

        translateJobService.updateJob(job);

        logger.info("Thread: [{}], translte job end.", Thread.currentThread().getName());
    }

    private String callWhisper(String audioPath){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            JSONObject params = new JSONObject();
            params.put("path", audioPath);

            logger.debug(params.toString());

            HttpEntity<JSONObject> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    appConfiguration.getWhisperSttUrl(),
                    request,
                    String.class);
            String sttText = response.getBody();
            return sttText;
//            JSONObject sttTextObj = JSONObject.parseObject(sttText);
//            return String.valueOf(sttTextObj.get("text"));
        }catch (Exception e){
            logger.error("call whisper python error,", e);
            throw new BusinessException(GlobalResultEnum.CALL_WHISPER_ERROR);
        }
    }

}
