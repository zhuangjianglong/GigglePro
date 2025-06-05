package com.whisper.pro.pattern.translate;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.pojo.bo.TranslateItemResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GoogleTranslateStrategy implements TranslateStrategy{
    protected static Logger logger = LoggerFactory.getLogger(GoogleTranslateStrategy.class);

    private static Translate translate = null;

    GoogleTranslateStrategy(){}
    GoogleTranslateStrategy(String apiKey){
        initializeTranslateClient(apiKey);
    }

    @Override
    public List<TranslateItemResult> translate(List<String> textList, String targetLanguage) {
        try {
            List<Translation> translationList = translate.translate(
                    textList,
                    Translate.TranslateOption.targetLanguage(targetLanguage)
            );

            List<TranslateItemResult> result = new ArrayList<>();
            for(int i=0;i<translationList.size();i++){
                Translation translation = translationList.get(i);
                result.add(new TranslateItemResult(i, translation.getTranslatedText(), translation.getSourceLanguage(), translation.getModel()));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to Google Translate.", e);
            throw new BusinessException(GlobalResultEnum.T_G_ERROR);
        }
    }

    private void initializeTranslateClient(String apiKey) {
        if (translate == null) {
            try {
               translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
               logger.info("Google Translate client initialized.");
            } catch (Exception e) {
                logger.error("Failed to initialize Google Translate client.", e);
            }
        }
    }

    public static void main(String[] args) {
        String apiKey = "";
        GoogleTranslateStrategy GoogleTranslateStrategy = new GoogleTranslateStrategy(apiKey);
        try {
            List<Language> languages = translate.listSupportedLanguages();
            for (Language language : languages) {
                System.out.println((String.format("Name: %s, Code: %s", language.getName(), language.getCode())));
            }
        } catch (Exception e) {
            // 异常处理
        }

    }

}
