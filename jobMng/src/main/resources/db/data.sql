DELETE FROM tb_stt_job;
INSERT INTO tb_stt_job (id, audio_id, audio_path, original_text_path, type, status, retry_count, text_path) VALUES
    (1, 1, '/whisperCase/1.mp3', '/whisperCase/original-1.txt', 1, 0, 0, '/9600387d-fa4a-433b-b99a-29038039a9e8.txt'),
    (2, 2, '/whisperCase/2.mp3', '', 1, 1, 0, null),
    (3, 3, '/whisperCase/1.mp3', '', 1, 1, 0, '/zh.txt');



DELETE FROM tb_translate_job;
INSERT INTO tb_translate_job (id, type, target_language, stt_job_id, status, retry_count, result_path) VALUES
    (1, 1, 'zh', 1, 0, 1, '/c41f8c8f-5a79-4988-a904-b24c276a8a8e.txt'),
    (2, 1, 'zh-TW', 1, 1, 0, null),
    (3, 1, 'ja', 1, 1, 0, null);

INSERT INTO tb_translate_job (id, type, target_language, stt_job_id, status, retry_count, result_path) VALUES
    (5, 1, 'en', 3, 1, 0, null),
    (6, 1, 'zh-TW', 3, 1, 0, null),
    (7, 1, 'ja', 3, 1, 0, null);