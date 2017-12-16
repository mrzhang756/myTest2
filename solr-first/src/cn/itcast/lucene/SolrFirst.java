package cn.itcast.lucene;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrFirst {

	// 1.添加
	@Test
	public void execute() throws Exception {
		testAddDocument("2", "标题2lahfds爱上了的返回键案例三");
		testAddDocument("3", "标题3;了空间or童颜巨乳他和内部各方面");
		testAddDocument("4", "标题4solr打算发好");
		testAddDocument("5", "标题5Lucene实得分");
		testAddDocument("6", "标题6spring倒海翻江as");
	}

	public void testAddDocument(String id, String title) throws Exception {
		// 2）创建一个SolrServer对象。需要使用HttpSolrServer对象。参数就是solr服务器的url
		// 如果http://localhost:8080/solr/默认管理的是Collection1
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 3）创建一个文档对象。SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		// 4）向文档中添加域
		// 每个文档中必须有id域
		// 每个域必须在schema.xml中定义
		document.addField("id", id); // key,value形式
		document.addField("title", title);
		// document.addField("content", "测试文档1的内容,精彩火辣z发的房间搜了了附加阿里");
		// 5）把文档写入索引库
		solrServer.add(document);
		// 6）提交
		solrServer.commit();
	}

	// 2.根据id删除
	@Test
	public void testDeleteById() throws Exception {
		// 1、创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 2、使用SolrServer的deleteById方法删除文档
		solrServer.deleteById("2");
		// 3、commit
		solrServer.commit();
	}

	// 3.根据查询删除
	@Test
	public void testDeleteByQuery() throws Exception {
		// 1、创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 2、使用SolrServer的deleteById方法删除文档
		solrServer.deleteByQuery("title:文档4,是否可以添加成功!");
		// 3、commit
		solrServer.commit();
	}

	// 3.根据查询删除
	@Test
	public void testDeleteAllByQuery() throws Exception {
		// 1、创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 2、使用SolrServer的deleteById方法删除文档
		solrServer.deleteByQuery("*:*");
		// 3、commit
		solrServer.commit();
	}

	// 4.查询所有文档
	@Test
	public void testQueryIndex() throws Exception {
		// 1.创建连接
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 2.创建一个query对象 -->需要创建一个查询对象
		SolrQuery query = new SolrQuery();
		// 3.设置查询条件
		query.setQuery("*:*");
		// 4.执行查询
		QueryResponse queryResponse = solrServer.query(query);
		// 5.取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 6.共查询到商品数量
		System.out.println("共查询到商品数量:" + solrDocumentList.getNumFound());
		// 7.遍历查询的结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("title"));
			System.out.println(solrDocument.get("content"));
		}
	}

	// 5.根据条件查询文档
	@Test
	public void testGetByQuery() throws Exception {
		// 1.创建连接
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		// 2.创建一个query对象 -->需要创建一个查询对象
		SolrQuery query = new SolrQuery();
		// 3.设置查询条件
		// query.setQuery("id:1");
		query.setQuery("title:测试文档1");
		// 4.执行查询
		QueryResponse queryResponse = solrServer.query(query);
		// 5.取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();

		// 6.打印查询的结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("title"));
			System.out.println(solrDocument.get("content"));
		}
	}

}
