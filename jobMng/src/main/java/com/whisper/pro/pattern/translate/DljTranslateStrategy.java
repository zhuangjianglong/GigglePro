package com.whisper.pro.pattern.translate;
import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.pojo.bo.TranslateItemResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DljTranslateStrategy implements TranslateStrategy{
    protected static Logger logger = LoggerFactory.getLogger(DljTranslateStrategy.class);

    private static DeepLClient client = null;

    DljTranslateStrategy(){}
    DljTranslateStrategy(String apiKey){
        initializeTranslateClient(apiKey);
    }

    @Override
    public List<TranslateItemResult> translate(List<String> textList, String targetLanguage) {
        try {
            List<TextResult> results =
                    client.translateText(textList, null, targetLanguage);

            List<TranslateItemResult> result = new ArrayList<>();
            for(int i=0;i<results.size();i++){
                TextResult translation = results.get(i);
                result.add(new TranslateItemResult(i, translation.getText(), translation.getDetectedSourceLanguage(), translation.getModelTypeUsed()));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to dlj Translate.", e);
            throw new BusinessException(GlobalResultEnum.T_G_ERROR);
        }
    }

    private void initializeTranslateClient(String apiKey) {
        if (client == null) {
            try {
                if(StringUtils.isNotBlank(apiKey)){
                    client = new DeepLClient(apiKey);
                    logger.info("DLJ Translate client initialized.");
                }
            } catch (Exception e) {
                logger.error("Failed to initialize DLJ Translate client.", e);
            }
        }
    }

}
