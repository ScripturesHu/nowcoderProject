package com.hjw.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * @author hujw
 * @description
 * @create 2021-10-19 11:07
 */

@Component
public class SensitiveFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String Filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //定义三个指针
        TrieNode tempNode = root;//root不存放数据
        int begin = 0;
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            //跳过符号(不是普通字符(abc123...)且不是东亚字符)
            if (!CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF)) {
                // 若此时处于根结点，首指针++
                if (tempNode == root) {
                    begin++;
                    sb.append(c);
                }
                // 无论符号在开头还是中间，尾指针++
                position++;
                // 进行下一次循环
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根结点
                tempNode = root;
            } else if (tempNode.isKeywordEnd()) {
                //发现敏感词，将begin-position字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根结点
                tempNode = root;
            } else {
                //检查下一个字符
                position++;
            }

        }
        //将最后一批非敏感词字符进入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }


    //根据敏感词，初始化前缀树
    //根结点
    private TrieNode root = new TrieNode();

    // 初始化方法，当容器实例化这个bean，调用构造方法后调用
    @PostConstruct
    public void init() {
        try (
                // 使用类加载器获取资源（编译后会存在class目录下）
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            LOGGER.error("加载敏感词文件失败" + e.getMessage());
        }
    }

    //将一个敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = root.getSubNode(c);
            if (subNode == null) {
                //初始化子结点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            //指向子结点，进行下一轮循环
            tempNode = subNode;
            //设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }


    //定义前缀树
    private class TrieNode {
        //关键词结束标识
        private boolean isKeywordEnd = false;

        //子结点 (key是下级字符，value是下级结点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子结点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子结点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }

}
