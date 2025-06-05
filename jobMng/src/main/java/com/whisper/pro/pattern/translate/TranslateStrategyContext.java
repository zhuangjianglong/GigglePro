package com.whisper.pro.pattern.translate;

import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.configuration.AppConfiguration;
import com.whisper.pro.pojo.bo.TranslateItemResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TranslateStrategyContext {

    @Autowired
    private AppConfiguration appConfig;

    public static final Integer GOOGLE = 1;
    public static final Integer BAIDU = 2;
    public static final Integer DLJ = 3;

    public static Map<Integer, TranslateStrategy> TSM = new HashMap<>();

    @PostConstruct
    public void init(){
        TSM.put(GOOGLE, new GoogleTranslateStrategy(appConfig.getGoogleApiKey()));
        TSM.put(DLJ, new DljTranslateStrategy(appConfig.getDljApiKey()));
    }

    public List<TranslateItemResult> executeStrategy(Integer type, List<String> textList, String targetLanguage) {
        if(TSM.containsKey(type)){
            TranslateStrategy translateStrategy = TSM.get(type);
            return translateStrategy.translate(textList, targetLanguage);
        }else{
            throw new BusinessException(GlobalResultEnum.T_NOFOUND_S);
        }

    }



}
