package cn.itcast.lucene;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SearchIndex {

	private IndexReader indexReader;
	private IndexSearcher indexSearcher;

	@Before
	public void init() throws Exception {
		// 1、创建一个Directory对象，指定索引库的位置
		Directory directory = FSDirectory.open(new File("F:/upload/temp/index"));
		// 创建一个IndexReader对象
		indexReader = DirectoryReader.open(directory);
		// 创建一个IndexSearcher对象
		indexSearcher = new IndexSearcher(indexReader);
	}

	@After
	public void after() throws Exception {
		// 关闭
		indexReader.close();
	}

	public void executeSearch(Query query) throws Exception {
		TopDocs topDocs = indexSearcher.search(query, 10);
		System.out.println("查询结果的总记录数:" + topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println(document.get("fileName"));
			System.out.println(document.get("filePath"));
			System.out.println(document.get("fileContent"));
			System.out.println(document.get("fileSize"));
		}
	}

	// 1.查询所有 3.1.1 MatchAllDocsQuery
	@Test
	public void testMatchAllDocumentQuery() throws Exception {
		MatchAllDocsQuery query = new MatchAllDocsQuery();
		System.out.println(query);
		executeSearch(query);
	}

	// 3.1.2 TermQuery 指定要查询的域和要查询的关键词。
	// 3.1.3 NumericRangeQuery可以根据数值范围查询。
	@Test
	public void testNumericRangeQuery() throws Exception {
		Query query = NumericRangeQuery.newLongRange("fileSize", 0L, 1000L, true, false);
		System.out.println("执行语句:" + query);
		executeSearch(query);
	}

	// 3.1.4 BooleanQuery可以组合查询条件。
	@Test
	public void testBooleanQuery() throws Exception {
		BooleanQuery query = new BooleanQuery();
		TermQuery query1 = new TermQuery(new Term("fileName", "spring"));
		TermQuery query2 = new TermQuery(new Term("fileContext", "spring"));
		query.add(query1, Occur.SHOULD);
		query.add(query2, Occur.SHOULD);
		System.out.println(query);
		executeSearch(query);
	}

	// 使用queryparser查询 -->带分析的查询(需导包)
	@Test
	public void testQueryParser() throws Exception {
		QueryParser queryParser = new QueryParser("fileContext", new IKAnalyzer());
		Query query = queryParser.parse("lucene is a apache project 全");
		System.out.println(query);
		executeSearch(query);
	}

	// 3.2.2 MulitFieldQueryParser -->实现百度收索(最繁琐的查询) 可以指定多个默认搜索域
	@Test
	public void testMulitFieldQueryParser() throws Exception{
		String[] fields = {"fileName","fileContext"};
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields,new IKAnalyzer());
		Query query = queryParser.parse("spring");
		System.out.println(query);
		executeSearch(query);
	}
}
