package com.demo.service;

import com.demo.dao.QuestionDao;
import com.demo.model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private Environment env;

    @Autowired
    private QuestionService questionService;

    private HttpSolrClient client;

    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = new HttpSolrClient.Builder(env.getProperty("spring.data.solr.host"))
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    public List<Question> search(String keyword, int offset, int limit, String hlpre, String hlpost) {
        final Map<String, String> queryParamMap = new HashMap<String, String>();
        String queryStr = QUESTION_TITLE_FIELD + ":" + keyword + " OR "
                + QUESTION_CONTENT_FIELD + ":" + keyword;
        SolrQuery query = new SolrQuery(queryStr);
        query.setStart(offset);
        query.setRows(limit);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlpre);
        query.setHighlightSimplePost(hlpost);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);

        List<Question> questionList = new ArrayList<>();
        try {
            QueryResponse response = client.query(query);
            for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
                Question question = questionService.selectById(Integer.parseInt(entry.getKey()));
                if (question == null) {
                    continue;
                }
                if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                    List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                    if (titleList.size() > 0) {
                        question.setTitle(titleList.get(0));
                    }
                }
                if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                    List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                    if (contentList.size() > 0) {
                        question.setContent(contentList.get(0));
                    }
                }
                questionList.add(question);
            }
            return questionList;
        } catch (Exception e) {
            logger.error("在SearchService中搜索异常：", e);
        }
        return null;
    }

    public boolean indexQuery(int qid, String title, String content) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", qid);
        doc.addField(QUESTION_TITLE_FIELD, title);
        doc.addField(QUESTION_CONTENT_FIELD, content);
        try {
            UpdateResponse updateResponse = client.add(doc, 1000);
            return updateResponse != null && updateResponse.getStatus() == 0;
        } catch (Exception e) {
            logger.error("在SearchService中索引异常：", e);
        }
        return false;
    }
}
