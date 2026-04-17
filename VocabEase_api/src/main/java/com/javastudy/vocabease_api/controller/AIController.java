package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController("aiController")
@RequestMapping("/ai")
public class AIController extends ABaseController {

    @Resource
    private AppConfig appConfig;

    private static final String DASHSCOPE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String YOUDAO_API = "https://dict.youdao.com/jsonapi";
    private final RestTemplate restTemplate = new RestTemplate();

    public AIController() {
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    // 统一入口：自动识别中英文
    @PostMapping("/translate")
    public ResponseVO<Map<String, Object>> translate(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.isBlank()) {
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        }

        try {
            boolean isEnglish = text.matches("[a-zA-Z\\s]+");
            Map<String, Object> result = new HashMap<>();

            if (isEnglish) {
                // 英文 → 查词
                Map<String, String> wordInfo = getYouDaoWordInfo(text);
                String memoryTip = generateMemoryTip(text, wordInfo.get("meaning"));
                result.put("type", "en");
                result.put("word", text);
                result.put("phonetic", wordInfo.get("phonetic"));
                result.put("meaning", wordInfo.get("meaning"));
                result.put("memoryTip", memoryTip);
            } else {
                // 中文 → 翻译多个英文
                List<Map<String, String>> translations = translateChineseToEnglish(text);
                result.put("type", "zh");
                result.put("chinese", text);
                result.put("translations", translations);
            }

            return getSuccessResponseVO(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    // 中文 → 多个英文
    private List<Map<String, String>> translateChineseToEnglish(String chinese) {
        String prompt = String.format("""
            把【%s】翻译成常用英文单词，返回3个最常用的，
            格式：单词 音标 词性|释义，每条一行，不要多余内容
            例：good /ɡʊd/ adj|好的
            """, chinese);

        String aiResult = callAI(prompt);
        return parseTranslationResult(aiResult);
    }

    // 解析翻译结果
    private List<Map<String, String>> parseTranslationResult(String aiResult) {
        List<String> lines = aiResult.lines()
                .filter(l -> !l.isBlank())
                .limit(3)
                .toList();

        return lines.stream().map(line -> {
            Map<String, String> map = new HashMap<>();
            try {
                String[] parts = line.split("\\s+", 3);
                String word = parts[0];
                String phonetic = parts[1];
                String meaning = parts[2];
                map.put("word", word);
                map.put("phonetic", phonetic);
                map.put("meaning", meaning);
            } catch (Exception e) {
                map.put("word", "解析失败");
                map.put("phonetic", "");
                map.put("meaning", line);
            }
            return map;
        }).toList();
    }

    // 旧接口兼容
    @PostMapping("/word")
    public ResponseVO<Map<String, String>> queryWord(@RequestBody Map<String, String> request) {
        String word = request.get("word");
        Map<String, String> dictResult = getYouDaoWordInfo(word);
        String memoryTip = generateMemoryTip(word, dictResult.get("meaning"));
        dictResult.put("memoryTip", memoryTip);
        return getSuccessResponseVO(dictResult);
    }

    @PostMapping("/chat")
    public ResponseVO<String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return getSuccessResponseVO(callAI(prompt));
    }

    private Map<String, String> getYouDaoWordInfo(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", word);
        params.add("type", "data");
        params.add("le", "en");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> res = restTemplate.exchange(YOUDAO_API, HttpMethod.POST, entity, Map.class);

        Map<String, Object> body = res.getBody();
        String phonetic = "";
        String meaning = "";

        try {
            if (body != null) {
                // 1. 优先从有道接口拿中文释义
                if (body.containsKey("basic")) {
                    Map<String, Object> basic = (Map<String, Object>) body.get("basic");
                    // 优先拿美式音标
                    if (basic.containsKey("us-phonetic")) {
                        phonetic = "/" + basic.get("us-phonetic") + "/";
                    } else if (basic.containsKey("phonetic")) {
                        phonetic = "/" + basic.get("phonetic") + "/";
                    }
                    // 拿中文基础释义
                    if (basic.containsKey("explains")) {
                        List<String> explains = (List<String>) basic.get("explains");
                        meaning = String.join("；", explains);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 兜底方案：如果有道没拿到数据，调用AI生成【中文释义+音标】
        if (phonetic.isEmpty() || meaning.isEmpty()) {
            // 重点修改：明确要求 AI 返回中文释义
            String aiPrompt = String.format("请给英文单词 %s 生成美式音标（格式：/xxx/）和中文释义，格式要求：音标|中文释义，不要多余内容", word);
            String aiResult = callAI(aiPrompt);
            if (aiResult.contains("|")) {
                String[] parts = aiResult.split("\\|");
                phonetic = parts[0].trim();
                // 确保拿到的是中文
                meaning = parts[1].trim();
            } else {
                // 极端情况兜底
                phonetic = "/未知音标/";
                meaning = "暂无中文释义";
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("word", word);
        result.put("phonetic", phonetic);
        result.put("meaning", meaning);
        return result;
    }

    // 超强记忆法提示词
    private String generateMemoryTip(String word, String meaning) {
        String prompt = String.format("""
                请为单词【%s】（释义：%s）生成中文记忆法，要求：
                1. 结合英文发音谐音+拼写拆分
                2. 一句话讲清、好记
                3. 50字以内，无格式，直接给内容
            """, word, meaning);
        return callAI(prompt);
    }

    private String callAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(appConfig.getApiKey());

        Map<String, Object> body = Map.of(
                "model", "qwen-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> res = restTemplate.postForEntity(DASHSCOPE_URL, entity, Map.class);

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) Objects.requireNonNull(res.getBody()).get("choices");
            Map<String, String> msg = (Map<String, String>) choices.get(0).get("message");
            return msg.get("content").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "AI生成异常";
        }
    }
}