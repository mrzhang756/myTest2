package cn.itcast.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneFirst {

	@Test
	public void creatIndex() throws Exception {
		// 第一步：创建一个java工程，并导入jar包。
		// 第二步：创建一个indexwriter对象。
		// 1）指定索引库的存放位置Directory对象
		Directory directory = FSDirectory.open(new File("F:/upload/temp/index"));
		// 2）指定一个分析器，对文档内容进行分析。
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		File file = new File("F:/mybatis/lucene&solr/00.参考资料/searchsource");
		for (File f : file.listFiles()) {
			String fileName = f.getName();
			String filePath = f.getPath();
			String fileContext = FileUtils.readFileToString(f);
			long fileSize = FileUtils.sizeOf(f);
			Field fileNameField = new TextField("fileName", fileName, Store.YES);
			// 文件内容域
			Field fileContentField = new TextField("fileContext", fileContext, Store.YES);
			// 文件路径域（不分析、不索引、只存储）
			Field filePathField = new StoredField("filePath", filePath);
			// 文件大小域
			Field fileSizeField = new LongField("fileSize", fileSize, Store.YES);
			Document document = new Document();
			document.add(fileNameField);
			document.add(fileContentField);
			document.add(filePathField);
			document.add(fileSizeField);
			indexWriter.addDocument(document);
		}
		indexWriter.close();
	}

	@Test
	public void searchIndex() throws Exception {
		// 第一步：创建一个Directory对象，也就是索引库存放的位置。
		Directory directory = FSDirectory.open(new File("F:/upload/temp/index"));
		// 第二步：创建一个indexReader对象，需要指定Directory对象。
		IndexReader indexReader = DirectoryReader.open(directory);
		// 第三步：创建一个indexsearcher对象，需要指定IndexReader对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// 第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
		Query query = new TermQuery(new Term("fileName", "检"));

		// 第五步：执行查询。
		TopDocs topDocs = indexSearcher.search(query, 10);
		// 第六步：返回查询结果。遍历查询结果并输出。
		System.out.println("查询结果的总条数:" + topDocs.totalHits);
		for (ScoreDoc scoreDocs : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDocs.doc);
			System.out.println(document.get("filename"));
			//System.out.println(document.get("fileContext"));
			System.out.println(document.get("filePath"));
			System.out.println(document.get("fileSize"));
			System.out.println("-----------------------------");
		}
		// 第七步：关闭IndexReader对象
		indexReader.close();

	}
	@Test
	public void testTokenStream() throws Exception{
		Analyzer analyzer = new IKAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("test", "传智播客+黑马程序员庖丁解牛最新版在 https://code.google.com/p/paoding/ 中最多支持Lucene 3.0，且最新提交的代码在 2008-06-03，在svn中最新也是2010年提交，已经过时，不予考虑。comprehensive programming and configuration model.");
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		tokenStream.reset();
		while(tokenStream.incrementToken()){
//			System.out.println("开始->"+offsetAttribute.startOffset());
			System.out.println(charTermAttribute);
//			System.out.println("结束->"+offsetAttribute.endOffset());
		
		}
		tokenStream.close();
		
	}
	
}
