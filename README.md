实体库检索代码块简介：
## 1.实体库检索代码块实现功能：
输入为一个用户的提问。

```
第一步：切分用户提问，分为陈述部分和各个小问句
第二步：判断各个小问句的提问模式
第三步：抽取各个小问句中包含的实体和属性
第四步：根据第二步中得到的提问模式和第三步中得到的实体和属性情况，给出用户答案
```

## 2.实体库检索代码块压缩包文件夹介绍
    .src
        .default package
            SearchEntityAndGiveSimilar.java---->主函数，程序入口（调用PreTreatment.java，SearchEntityXML.java）
            PreTreatment.java---->读入实体名表、属性名表、词语权重表、实体库、父子关系库
            SearchEntityXML.java---->（）
            PatternMatches.java---->判断一个简单问句的提问模式，得到一个小问题的所有可能的问题模式和一个小问题的所有可能的问题模式对应的去除提问词以后的字符串
            SplitMULT.java---->对一个复杂问句进行切分，得到陈述部分、问题列表、模式列表
            IKsegment.java---->对一个字符串进行分词功能，得到一个字符串列表
            DoToString.java---->对字符串做分词去停用词处理，得到短字符串
            WeightSimilarAlgorithm.java---->计算两个字符串对权重相似度
            WeightMatching.java---->对字符串进行权重相似度匹配
            StringOptimize.java---->字符串紧致化方法，用于计算两个字符串对余弦相似度，提高准确率
            CosineSimilarAlgorithm.java---->计算两个字符串对余弦相似度
            CosineMatching.java---->对字符串进行余弦相似度匹配
            MatchingResult.java---->对一个小问句进行实体名称和属性名称匹配，得到小问句中包涵对实体和属性
            ProcessingMode.java---->检索方法的集合，根据不同对情况调用不同的检索方法
            GiveSimilar.java---->根据问答库，给出与输入字符串相似度最高的问题的答案
        .test.dic
            MyDict.dic---->
            stopword.dic---->
        IKAnalyzer.cfg.xml---->IKAnalyzer分词配置文件
        IKAnalyzer.cfg.xml.bak---->IKAnalyzer分词配置文
    .Referenced Libraries
        dom4j-1.6.1.jar---->开源XML解析包
        jaxen-1.1-beta-7-2.jar---->开源XPath库
        IKAnalyzer2012_FF.jar---->IKAnalyzer分词
    .lib
        .keywords---->问题模式判断模块中的关键字
            1-keywords_what.txt
            2-keywords_how.txt
            3-keywords_where.txt
            4-keywords_when.txt
            5-keywords_why.txt
            6-keywords_which.txt
            6-keywords_which_logic.txt
            7-keywords_yes_no.txt
            8-keywords_selection.txt
            9-keywords_estimate.txt
            10-keywords_compare.txt
            11-keywords_ask.txt
        .stopwords---->问题模式判断模块中的停用词
            1-stopwords_what.txt
            2-stopwords_how.txt
            3-stopwords_where.txt
            4-stopwords_when.txt
            5-stopwords_why.txt
            6-stopwordswords_which.txt
            6-stopwords_which_logic.txt
            7-stopwords_yes_no.txt
            8-stopwords_selection.txt
            9-stopwords_estimate.txt
            10-stopwords_compare.txt
            11-stopwords_ask.txt
        .QAbase---->问答库
            01.xml
            02.xml
            extra.xml
        .test
            EntityBase.xml---->实体库
            EntityName.txt---->实体名称
            AttributeName.txt---->属性名称
            WordWeight.txt---->关键词语的权重值表
            FatherChildRelation.xml---->实体间的父子关系库
    
 
## 3.程序输入：inputContent(String型)

## 4.程序输出：answerList(ArrayList型)