package com.whisper.pro.api;

import com.whisper.pro.async.AsyncComponent;
import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TextResult;
import com.whisper.pro.service.SttJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stt")
public class SttApi {
    protected static Logger logger = LoggerFactory.getLogger(SttApi.class);

    @Autowired
    private SttJobService sttJobService;
    @Autowired
    private AppConfiguration appConfiguration;
    @Autowired
    private AsyncComponent asyncComponent;

    @PostMapping(value = "/addJob", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addJob(SttJob job){

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SttJob getJob(
            @PathVariable(name = "id") Integer id){
        return sttJobService.get(id);
    }

    @GetMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void startJob(
            @PathVariable(name = "id") Integer id){
        SttJob job = sttJobService.get(id);
        if(job == null){
            throw new BusinessException(GlobalResultEnum.W_JOB_NOFOUND);
        }

        if(job.getStatus() == 4){
            throw new BusinessException(GlobalResultEnum.W_JOB_CANCLE);
        }

        asyncComponent.startSttJob(job);
    }

    @PutMapping(value = "/enable/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void enableJob(
            @PathVariable(name = "id") Integer id){
        sttJobService.enableJob(id);
    }

    @PutMapping(value = "/cancle/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void cancleJob(
            @PathVariable(name = "id") Integer id){
        sttJobService.cancleJob(id);
    }

    @GetMapping(value = "/textRecognition/{type}/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Double textRecognition(
            @PathVariable(name = "type") Integer type,
            @PathVariable(name = "id") Integer id){
        return sttJobService.textRecognition(type,id);
    }

    @GetMapping(value = "/textresult/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TextResult getTextResult(
            @PathVariable(name = "id") Integer id){
        return sttJobService.getTextResult(id);
    }

    @GetMapping(value = "/query/{language}/{id}/{ori}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String query(
            @PathVariable(name = "language") String language,
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "ori") String ori){
        return sttJobService.query(language,id,ori);
    }
}
