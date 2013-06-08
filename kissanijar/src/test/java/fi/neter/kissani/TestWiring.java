package fi.neter.kissani;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.neter.kissani.fb.FBService;
import fi.neter.kissani.server.KissaniServiceImpl;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestWiring {

	@Autowired
	public KissaniController controller;

	@Autowired
	@Qualifier("kis")
	public KissaniServiceImpl kis;

	@Autowired
	public FBService fbService;
	
	@Test
	public void test() {
		
	}
}
