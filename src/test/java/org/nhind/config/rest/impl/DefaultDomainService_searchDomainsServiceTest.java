package org.nhind.config.rest.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import org.nhind.config.client.SpringBaseTest;
import org.nhind.config.testbase.BaseTestPlan;

import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.common.rest.exceptions.ServiceMethodException;
import org.nhindirect.config.model.Address;
import org.nhindirect.config.model.Domain;
import org.nhindirect.config.model.EntityStatus;
import org.nhindirect.config.repository.DomainRepository;

public class DefaultDomainService_searchDomainsServiceTest extends SpringBaseTest
{
		abstract class TestPlan extends BaseTestPlan 
		{
			
			@Override
			protected void tearDownMocks()
			{

			}

			protected abstract Collection<Domain> getDomainsToAdd();
			
			protected abstract String getDomainNameToSearch();
			
			protected abstract String getEntityStatusToSearch();
			
			@Override
			protected void performInner() throws Exception
			{				
				
				final Collection<Domain> addDomains = getDomainsToAdd();
				
				if (addDomains != null)
				{
					for (Domain addDomain : addDomains)
					try
					{
						domainService.addDomain(addDomain);
					}
					catch (ServiceException e)
					{
		
						throw e;
					}
				}
				
				try
				{	
					EntityStatus entityStatus = (getEntityStatusToSearch() != null) ? EntityStatus.valueOf(getEntityStatusToSearch()) : null;
					
					final Collection<Domain> getDomains = domainService.searchDomains(getDomainNameToSearch(),  entityStatus);

					
					doAssertions(getDomains);
				}
				catch (ServiceMethodException e)
				{
					
					if (e.getResponseCode() == 404 || e.getResponseCode()== 204)
						doAssertions(new ArrayList<Domain>());
					else
						throw e;
				}
				
			}
			
			
			protected void doAssertions(Collection<Domain> domains) throws Exception
			{
				
			}
		}	
		
		@Test
		public void testSearchDomains_getExistingDomain_nullEntityStatus_assertDomainRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					final Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "test.com";
				}
				
				protected String getEntityStatusToSearch()
				{
					return null;
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(1, domains.size());
					
					Domain retrievedDomain = domains.iterator().next();
					Domain addedDomain = this.domains.iterator().next();
					
					assertEquals(addedDomain.getDomainName(), retrievedDomain.getDomainName());
					assertEquals(addedDomain.getStatus(), retrievedDomain.getStatus());
					assertEquals(addedDomain.getPostmasterAddress().getEmailAddress(), retrievedDomain.getPostmasterAddress().getEmailAddress());
				}
			}.perform();
		}	
		
		@Test
		public void testSearchDomains_getExistingDomain_newEntityStatus_assertNoDomainRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					final Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "test.com";
				}
				
				protected String getEntityStatusToSearch()
				{
					return "NEW";
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(0, domains.size());
					
				}
			}.perform();
		}	
		
		@Test
		public void testSearchDomains_getNonExistantDomain_assertNoDomainRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					final Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "test2.com";
				}
				
				protected String getEntityStatusToSearch()
				{
					return null;
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(0, domains.size());
					
				}
			}.perform();
		}		
		
		@Test
		public void testSearchDomains_getExistingDomain_emptySearchString_assertDomainRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					final Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "";
				}
				
				protected String getEntityStatusToSearch()
				{
					return "ENABLED";
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(1, domains.size());
					
					Domain retrievedDomain = domains.iterator().next();
					Domain addedDomain = this.domains.iterator().next();
					
					assertEquals(addedDomain.getDomainName(), retrievedDomain.getDomainName());
					assertEquals(addedDomain.getStatus(), retrievedDomain.getStatus());
					assertEquals(addedDomain.getPostmasterAddress().getEmailAddress(), retrievedDomain.getPostmasterAddress().getEmailAddress());
					
				}
			}.perform();
		}		
		
		@Test
		public void testSearchDomains_getExistingDomains_emptySearchString_assertDomainsRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					// domain 1
					Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					// domain 2
					postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test2.com");
					
					domain = new Domain();
					
					domain.setDomainName("test2.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);				
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "";
				}
				
				protected String getEntityStatusToSearch()
				{
					return null;
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(2, domains.size());
					
				}
			}.perform();
		}	
		
		@Test
		public void testSearchDomains_getExistingDomains_emptySearchString_enabledOnly_assertDomainRetrieved() throws Exception
		{
			new TestPlan()
			{
				protected Collection<Domain> domains;
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					domains = new ArrayList<Domain>();
					
					// domain 1
					Address postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test.com");
					
					Domain domain = new Domain();
					
					domain.setDomainName("test.com");
					domain.setStatus(EntityStatus.NEW);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);
					
					// domain 2
					postmasterAddress = new Address();
					postmasterAddress.setEmailAddress("me@test2.com");
					
					domain = new Domain();
					
					domain.setDomainName("test2.com");
					domain.setStatus(EntityStatus.ENABLED);
					domain.setPostmasterAddress(postmasterAddress);			
					
					domains.add(domain);				
					
					return domains;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "";
				}
				
				protected String getEntityStatusToSearch()
				{
					return "ENABLED";
				}
				
				@Override
				protected void doAssertions(Collection<Domain> domains) throws Exception
				{
					assertNotNull(domains);
					assertEquals(1, domains.size());
					
					assertNotNull(domains);
					assertEquals(1, domains.size());
					
					Domain retrievedDomain = domains.iterator().next();
					
					assertEquals("test2.com", retrievedDomain.getDomainName());

				}
			}.perform();
		}
		
		@Test
		public void testSearchDomains_errorInSearch_assertServerError() throws Exception
		{
			new TestPlan()
			{
				@Override
				protected void setupMocks()
				{
					try
					{
						super.setupMocks();

						DomainRepository mockDAO = mock(DomainRepository.class);
						doThrow(new RuntimeException()).when(mockDAO).findByDomainNameContainingIgnoreCase(eq("test.com"));
						
						domainResource.setDomainRepository(mockDAO);
					}
					catch (Throwable t)
					{
						throw new RuntimeException(t);
					}
				}	
				
				@Override
				protected void tearDownMocks()
				{
					super.tearDownMocks();
					
					domainResource.setDomainRepository(domainRepo);
				}
				
				@Override
				protected Collection<Domain> getDomainsToAdd()
				{
					return null;
				}
				
				@Override
				protected String getDomainNameToSearch()
				{
					return "test.com";
				}
				
				protected String getEntityStatusToSearch()
				{
					return null;
				}
				
				@Override
				protected void assertException(Exception exception) throws Exception 
				{
					assertTrue(exception instanceof ServiceMethodException);
					ServiceMethodException ex = (ServiceMethodException)exception;
					assertEquals(500, ex.getResponseCode());
				}
			}.perform();
		}		
	}
