package org.mbmg;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;

public class MyServiceTest {

	static AmazonSimpleDB sdb;
	static String domain;

	@BeforeClass
	public static void init() throws Exception {
		domain = "MuhuruBay";
		String userHome = System.getenv("HOME");
		Path homePath = Paths.get(userHome, "AwsCredentials.properties");
		sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
				homePath.toFile()));
	}

	@Test
	public void createDB() throws Exception {
		sdb.createDomain(new CreateDomainRequest(domain));
	}

	@Test
	public void insertTestData() throws Exception {
		List<ReplaceableItem> data = new ArrayList<ReplaceableItem>();
		data.add(new ReplaceableItem().withName("Wind_Turbine_01")
				.withAttributes(
						new ReplaceableAttribute().withName("Power").withValue(
								"10"),
						new ReplaceableAttribute().withName("Units").withValue(
								"KW")));
		sdb.batchPutAttributes(new BatchPutAttributesRequest(domain, data));
	}

	@Test
	public void queryDB() throws Exception {
		String qry = "select * from `" + domain + "` where Units = 'KW'";
		SelectRequest selectRequest = new SelectRequest(qry);
		for (Item item : sdb.select(selectRequest).getItems()) {
			System.out.println("Power=: " + item.getName());
		}
	}

}
