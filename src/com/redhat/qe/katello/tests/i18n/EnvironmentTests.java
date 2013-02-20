package com.redhat.qe.katello.tests.i18n;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.redhat.qe.Assert;
import com.redhat.qe.katello.base.KatelloCliTestScript;
import com.redhat.qe.katello.base.obj.KatelloEnvironment;
import com.redhat.qe.katello.base.obj.KatelloOrg;
import com.redhat.qe.katello.common.KatelloUtils;
import com.redhat.qe.tools.SSHCommandResult;

public class EnvironmentTests extends KatelloCliTestScript {
	
	private String uid;
	private String org_name;
	private String env_name;
	private String env_name2;
	private String env_name3;
	private String env_desc;
	private String env_desc2;
	private String env_desc3;
	private String env_desc_edit;
	
	@BeforeClass(description="create org", alwaysRun=true)
	public void setUp() {
		uid = KatelloUtils.getUniqueID();
		org_name = getText("org.create.name")+" "+uid;
		env_name = getText("environment.create.name")+" "+uid;
		env_desc = getText("environment.create.description")+" "+uid;
		env_name2 = getText("environment.create.name")+" "+KatelloUtils.getUniqueID();
		env_name3 = getText("environment.create.name")+" "+KatelloUtils.getUniqueID();
		env_desc2 = getText("environment.create.description")+" "+KatelloUtils.getUniqueID();
		env_desc3 = getText("environment.create.description")+" "+KatelloUtils.getUniqueID();
		env_desc_edit = getText("environment.create.description")+" "+KatelloUtils.getUniqueID();
		
		KatelloOrg org = new KatelloOrg(org_name, null);
		SSHCommandResult res = org.cli_create();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code");
		
		KatelloEnvironment env = new KatelloEnvironment(env_name2, env_desc2, 
				org_name, KatelloEnvironment.LIBRARY);
		res = env.cli_create();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code");
		
		env = new KatelloEnvironment(env_name3, env_desc3, 
				org_name, KatelloEnvironment.LIBRARY);
		res = env.cli_create();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code");
	}
	
	@Test(description="environment create")
	public void test_createEnvironment() {
		SSHCommandResult res;
		KatelloEnvironment env = new KatelloEnvironment(env_name, env_desc, 
				org_name, KatelloEnvironment.LIBRARY);
		res = env.cli_create();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code");
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code (environment create)");
	}
	
	@Test(description="environment info", dependsOnMethods={"test_createEnvironment"})
	public void test_infoEnvironment() {
		KatelloEnvironment env = new KatelloEnvironment(env_name, null, org_name, null);
		SSHCommandResult res = env.cli_info();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code (environment info)");
		Assert.assertTrue(res.getStdout().trim().contains(env_desc), "");
	}
	
	@Test(description="environment list", dependsOnMethods={"test_createEnvironment"})
	public void test_listEnvironment() {
		KatelloEnvironment env = new KatelloEnvironment(null, null, org_name, null);
		SSHCommandResult res = env.cli_list();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code");
	}
	
	@Test(description="environment update", dependsOnMethods={"test_createEnvironment"})
	public void test_updateEnvironment() {
		KatelloEnvironment env = new KatelloEnvironment(env_name, null, org_name, null);
		SSHCommandResult res= env.cli_update(env_desc_edit);
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code (environment update)");
	}

	@Test(description="environment delete", dependsOnMethods={"test_infoEnvironment","test_updateEnvironment","test_listEnvironment"})
	public void test_deleteEnvironment() {
		SSHCommandResult res;

		KatelloEnvironment env = new KatelloEnvironment(env_name, null, org_name, null);
		res = env.cli_delete();
		Assert.assertTrue(res.getExitCode() == 0, "Check - return code (environment delete)");
	}
	
}
