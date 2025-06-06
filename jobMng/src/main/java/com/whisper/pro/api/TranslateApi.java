package com.whisper.pro.api;

import com.whisper.pro.async.AsyncComponent;
import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.pojo.bo.SttJob;
import com.whisper.pro.pojo.bo.TextResult;
import com.whisper.pro.pojo.bo.TranslateJob;
import com.whisper.pro.service.SttJobService;
import com.whisper.pro.service.TranslateJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/translate")
public class TranslateApi {
    protected static Logger logger = LoggerFactory.getLogger(TranslateApi.class);

    @Autowired
    private TranslateJobService translateJobService;
    @Autowired
    private AppConfiguration appConfiguration;
    @Autowired
    private AsyncComponent asyncComponent;

    @PostMapping(value = "/addJob", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addJob(@RequestBody TranslateJob job){

        translateJobService.addJob(job);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TranslateJob getJob(
            @PathVariable(name = "id") Integer id){
        return translateJobService.get(id);
    }

    @GetMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void startJob(
            @PathVariable(name = "id") Integer id){
        TranslateJob job = translateJobService.get(id);
        if(job == null){
            throw new BusinessException(GlobalResultEnum.W_JOB_NOFOUND);
        }

        if(job.getStatus() == 4){
            throw new BusinessException(GlobalResultEnum.W_JOB_CANCLE);
        }

        asyncComponent.startTranslateJob(job);
    }

}
