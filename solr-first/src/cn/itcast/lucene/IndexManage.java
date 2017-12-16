package cn.itcast.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexManage {

	private IndexWriter indexWriter;

	@Before
	public void init() throws Exception {
		// 1、创建一个Directory对象，指定索引库的位置
		Directory directory = FSDirectory.open(new File("F:/upload/temp/index"));
		// 2、创建一个IndexWriter对象。以写的方式打开索引库。
		indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
	}
	@After
	public void after() throws Exception{
		//提交
		indexWriter.commit();
		//关闭
		indexWriter.close();
	}
	
	//1.添加索引
	@Test
	public void testAddDocument() throws Exception{
		Document documnet = new Document();
		Field field = new TextField("fileName","测试文档测试文档测试文档测试文档测试文档spring",Store.YES);
		field.setBoost(100);
		documnet.add(field);
		documnet.add(new TextField("content", "测试文档内容",Store.YES));
		documnet.add(new StoredField("path", "F:/upload"));
		documnet.add(new LongField("size", 100L, Store.YES));
		
		indexWriter.addDocument(documnet);
	}
	//2.删除所有
	@Test
	public void testDeleteAll() throws Exception{
		indexWriter.deleteAll();
	}
	//3.根据条件删除
	@Test
	public void testDeleteByQuery() throws Exception{
		Query query = new TermQuery(new Term("fileName", "apache"));
		System.out.println(query); //fileName:apache
		indexWriter.deleteDocuments(query);
	}
	//4.修改(先删除后添加)
	@Test
	public void testUpdate() throws Exception{
		Document document = new Document();
		document.add(new TextField("name", "更改之后的文档6",Store.YES));
		document.add(new TextField("name6", "更改之后的文档6",Store.YES));
		document.add(new TextField("name6", "更改之后的文档6",Store.YES));
		indexWriter.updateDocument(new Term("fileContent","string"),document);
	}
	
}
