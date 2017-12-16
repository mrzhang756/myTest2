package cn.itcast.lucene;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class SolrQueryTest {
	//复杂查询索引
	@Test
	public void TestQueryIndex() throws Exception{
		//1.创建连接
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		//2.创建一个query对象
		SolrQuery query = new SolrQuery();
		//3.设置查询条件
		query.setQuery("花儿");
		//过滤条件
		//query.setFilterQueries("product_catalog_name:幽默杂货");
		//query.addFilterQuery("product_price:[0 TO 50]");
		//排序条件
		query.setSort("product_price", ORDER.asc);
		//分页处理
		query.setStart(0);
		query.setRows(10);
		//结果中域的列表
		query.setFields("id","product_name","product_price","product_catalog_name","product_picture");
		//设置默认搜索域
		query.set("df", "product_keywords");
		//高亮显示
		query.setHighlight(true);
		//高亮显示的域
		query.addHighlightField("product_name");
		//高亮显示的前缀
		query.setHighlightSimplePre("<span color='red'>");
		//高亮显示的后缀
		query.setHighlightSimplePost("</span>");
		//4.执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//5.取查询结果,它父类就是ArrayList
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//共查询到商品数量
		System.out.println("共查询到商品数量:"+solrDocumentList.getNumFound());
		//遍历查询的结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			
			String productName="";
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if (list!=null && list.size()>0) {
				productName=list.get(0);
			}else{
				productName=(String) solrDocument.get("product_name");
			}
			System.out.println(productName);
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_catalog_name"));
			System.out.println(solrDocument.get("product_prcture"));
		}
	}
	@Test
	public void queryDocumentFuza() throws Exception {
		//创建一个solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("花儿");
		query.addFilterQuery("product_price:[0 TO 50]");
		query.setSort("product_price", ORDER.asc);
		query.setStart(0);
		query.setRows(10);
		query.setFields("id","product_name", "product_price", "product_catalog_name", "product_picture");
		query.set("df", "product_keywords");
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数：" + solrDocumentList.getNumFound());
		//取高亮结果
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			String name = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if (list != null && list.size() > 0) {
				name = list.get(0);
			} else {
				name = solrDocument.get("product_name").toString();
			}
			System.out.println(name);
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_catalog_name"));
			System.out.println(solrDocument.get("product_picture"));
		}
	}


}
