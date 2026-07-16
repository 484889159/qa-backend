package com.qa.service;

import com.qa.config.AIConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AIConfig aiConfig;

    @Value("${ai.deepseek.url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${ai.deepseek.model:deepseek-chat}")
    private String model;

    // ✅ 本地知识库（优先匹配）
    private static final Map<String, String> KNOWLEDGE_BASE = new HashMap<>();

    static {
        KNOWLEDGE_BASE.put("宿舍", "🏠 关于宿舍：\n• 新生宿舍通常安排在1-8号宿舍楼\n• 一般为4-6人间，上床下桌\n• 配有空调、独立卫浴、热水器\n• 宿舍有无线网络覆盖\n• 具体分配请关注学院通知");
        KNOWLEDGE_BASE.put("军训", "🎖️ 关于军训：\n• 军训时间：开学后第2周开始\n• 时长：约14天\n• 地点：学校操场\n• 必备物品：防晒霜、水杯、运动鞋\n• 注意：如身体不适可申请免训或缓训");
        KNOWLEDGE_BASE.put("食堂", "🍜 关于食堂：\n• 学校共有3个食堂\n• 一食堂：麻辣烫、自选快餐\n• 二食堂：煲仔饭、粤菜\n• 三食堂：牛肉面、西北风味\n• 营业时间：6:30-21:30");
        KNOWLEDGE_BASE.put("选课", "📚 关于选课：\n• 选课时间：开学前1周开放\n• 登录教务系统进行选课\n• 建议：提前了解课程评价\n• 注意：每人最多选2门选修课");
        KNOWLEDGE_BASE.put("社团", "🎭 关于社团：\n• 学校有100+个社团\n• 分类：学术、文艺、体育、公益\n• 招新时间：开学后第2-3周\n• 每人最多参加3个社团");
        KNOWLEDGE_BASE.put("转专业", "📝 关于转专业：\n• 时间：大一下学期末申请\n• 条件：成绩排名前30%\n• 流程：提交申请→学院审核→面试\n• 名额：每个专业有限定名额");
        KNOWLEDGE_BASE.put("奖学金", "💰 关于奖学金：\n• 国家奖学金：8000元/年\n• 国家励志奖学金：5000元/年\n• 校级奖学金：1000-3000元\n• 评选条件：成绩+综合素质");
        KNOWLEDGE_BASE.put("图书馆", "📖 关于图书馆：\n• 开放时间：8:00-22:00\n• 借阅规则：最多借10本/30天\n• 自习室：3-5楼有自习区\n• 电子资源：可访问知网等数据库");
        KNOWLEDGE_BASE.put("报到", "📋 关于报到：\n• 时间：详见录取通知书\n• 地点：学校体育馆/学院楼\n• 携带材料：身份证、录取通知书\n• 一寸照片(8张)、档案");
        KNOWLEDGE_BASE.put("快递", "📦 关于快递：\n• 地址：XX大学XX校区快递中心\n• 可收发：顺丰、中通、圆通等\n• 取件：凭取件码自助取件\n• 营业时间：8:00-20:00");
        KNOWLEDGE_BASE.put("肇庆医学院", "🏥 关于肇庆医学院：\n• 肇庆医学院（原肇庆医学高等专科学校）\n• 校区：端州校区（老校区）、鼎湖校区（新校区）\n• 宿舍：端州校区8-10人间，公共卫浴；鼎湖校区4-6人间，独立卫浴\n• 王牌专业：临床医学、护理学、药学\n• 是广东省重要的医学人才培养基地");
        KNOWLEDGE_BASE.put("乱码", "💡 乱码问题通常是由于编码不一致导致的。\n\n解决方案：\n1. 在浏览器中：右键 → 编码 → 选择 UTF-8\n2. 在编辑器中：用 UTF-8 编码重新打开文件\n3. 统一所有环节的编码为 UTF-8");
    }

    // ✅ 存储对话历史
    private final Map<String, List<Map<String, String>>> sessionHistory = new HashMap<>();

    public String getAnswer(String question) {
        return getAnswer(question, "default-user");
    }

    public String getAnswer(String question, String sessionId) {
        if (question == null || question.trim().isEmpty()) {
            return "请提出您的问题～";
        }

        System.out.println("📝 收到问题: " + question);
        System.out.println("👤 会话ID: " + sessionId);

        // ✅ 1. 先检查本地知识库
        String localAnswer = matchLocalKnowledge(question);
        if (localAnswer != null) {
            System.out.println("📚 命中本地知识库");
            // 保存到历史
            List<Map<String, String>> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
            addToHistory(history, "user", question);
            addToHistory(history, "assistant", localAnswer);
            return localAnswer + "\n\n📚 学长回答";
        }

        // ✅ 2. 本地没有匹配，调用 AI API
        System.out.println("🤖 本地无匹配，调用 AI...");
        List<Map<String, String>> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());

        String apiKey = aiConfig.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            try {
                String aiAnswer = callAIWithContext(question, history);
                if (aiAnswer != null && !aiAnswer.isEmpty()) {
                    addToHistory(history, "user", question);
                    addToHistory(history, "assistant", aiAnswer);
                    return aiAnswer + "\n\n🤖 以上由 DeepSeek AI 生成";
                }
            } catch (Exception e) {
                System.err.println("❌ AI 调用失败: " + e.getMessage());
            }
        }

        // ✅ 3. AI 也失败，返回通用回复
        return "🤔 暂时无法回答您的问题，建议您查看学校官网或咨询辅导员。";
    }

    /**
     * 本地知识库匹配
     */
    private String matchLocalKnowledge(String question) {
        for (Map.Entry<String, String> entry : KNOWLEDGE_BASE.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 调用 AI（带上下文）
     */
    private String callAIWithContext(String question, List<Map<String, String>> history) {
        String apiKey = aiConfig.getApiKey();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            List<Map<String, String>> messages = new ArrayList<>();

            // 系统提示词
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个大学新生智能助手，专门回答新生关于学校生活、学习、住宿、选课等方面的问题。请用友好、热情、亲切的语气回答，回答要简洁实用。");
            messages.add(systemMsg);

            // 添加历史对话（最多10条）
            int startIndex = Math.max(0, history.size() - 10);
            for (int i = startIndex; i < history.size(); i++) {
                messages.add(history.get(i));
            }

            // 当前问题
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", question);
            messages.add(userMsg);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 500);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            System.out.println("🤖 调用 DeepSeek AI (带上下文)");
            System.out.println("📚 历史消息数: " + history.size());

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                String content = extractContentFromResponse(body);
                if (content != null && !content.isEmpty()) {
                    System.out.println("✅ AI 回答成功");
                    return content;
                }
                return null;
            } else {
                System.err.println("❌ API 返回错误: " + response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            System.err.println("❌ API 异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void addToHistory(List<Map<String, String>> history, String role, String content) {
        Map<String, String> msg = new HashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        history.add(msg);
        if (history.size() > 20) {
            history.remove(0);
            history.remove(0);
        }
    }

    private String extractContentFromResponse(String jsonResponse) {
        try {
            String searchKey = "\"content\":\"";
            int startIndex = jsonResponse.indexOf(searchKey);
            if (startIndex == -1) return null;
            startIndex += searchKey.length();

            int endIndex = startIndex;
            while (endIndex < jsonResponse.length()) {
                char c = jsonResponse.charAt(endIndex);
                if (c == '"' && (endIndex == startIndex || jsonResponse.charAt(endIndex - 1) != '\\')) {
                    break;
                }
                endIndex++;
            }

            if (endIndex > startIndex) {
                String content = jsonResponse.substring(startIndex, endIndex);
                content = content.replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\t", "\t")
                        .replace("\\\\", "\\");
                return content;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> getHotQuestions() {
        Map<String, String> hot = new LinkedHashMap<>();
        hot.put("宿舍怎么样？", "");
        hot.put("军训多久？", "");
        hot.put("怎么选课？", "");
        hot.put("食堂好吃吗？", "");
        hot.put("肇庆医学院怎么样？", "");
        return hot;
    }
}