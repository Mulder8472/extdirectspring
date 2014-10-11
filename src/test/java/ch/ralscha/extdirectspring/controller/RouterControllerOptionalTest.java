/**
 * Copyright 2010-2014 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.ralscha.extdirectspring.controller;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.ralscha.extdirectspring.bean.ExtDirectPollResponse;
import ch.ralscha.extdirectspring.provider.RemoteProviderSimple.BusinessObject;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/testApplicationContext.xml")
public class RouterControllerOptionalTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	public static void beforeTest() {
		Locale.setDefault(Locale.US);
	}

	@Before
	public void setupMockMvc() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testMethod1() {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called-1-3.100-requestParameter", 1, 3.1, "requestParameter");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.100-requestParameter2", null, 3.1,
				"requestParameter2");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called-2-3.141-str", 2, null, "str");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.141-str22", null, null, "str22");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method1",
				"method1() called--1-3.141-null", null, null, null);

	}

	@Test
	public void testMethod2() {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method2",
				"one", "one");

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method2",
				"default", new Object[] { null });
	}

	@Test
	public void testMethod4() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method4", "1;v;headerValue", 1, "v");
	}

	@Test
	public void testMethod5() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		headers.add("anotherName", "headerValue1");
		headers.add("anotherName", "headerValue2");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method5", "11;headerValue1", 11);
	}

	@Test
	public void testMethod6a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		headers.add("anotherName", "headerValue1");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method6", "headerValue1");
	}

	@Test
	public void testMethod6b() throws Exception {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method6",
				"default");
	}

	@Test
	public void testMethod7a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");
		ControllerUtil.sendAndReceiveWithSession(mockMvc, headers,
				"remoteProviderOptional", "method7", "headerValue");
	}

	@Test
	public void testMethod7b() throws Exception {
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method7",
				"default");
	}

	@Test
	public void testMethod8a() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "lastHeader");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;default1;default2;lastHeader", 100);
	}

	@Test
	public void testMethod8b() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "lastHeader");
		headers.add("header2", "2ndHeader");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;default1;2ndHeader;lastHeader", 100);
	}

	@Test
	public void testMethod8c() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("last", "last");
		headers.add("header1", "1st");
		headers.add("header2", "2nd");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method8", "100;1st;2nd;last", 100);
	}

	@Test
	public void testMethod9() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("intHeader", "2");
		headers.add("booleanHeader", "true");

		ControllerUtil.sendAndReceive(mockMvc, headers, "remoteProviderOptional",
				"method9", "2;true");
		ControllerUtil.sendAndReceiveWithSession(mockMvc, headers,
				"remoteProviderOptional", "method9", "2;true");
	}

	@Test
	public void testMethod10And11() throws Exception {
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
		// "Ralph;one-two-;10", "Ralph", new String[] { "one", "two" }, 10);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
				"Ralph;one-;11", "Ralph", new String[] { "one" }, 11);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
		// "Ralph;;12", "Ralph", new String[] {}, 12);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method10",
				"Ralph;;13", "Ralph", null, 13);

		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;1+2+;20", "aStr", new int[] { 1, 2 }, 20);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;1+2+3+;21", "aStr", new int[] { 3, 1, 2 }, 21);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
				"aStr;3+;22", "aStr", new int[] { 3 }, 22);
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
		// "aStr;;23", "aStr", new int[] {}, 23);
		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method11",
				"aStr;;24", "aStr", null, 24);
	}

	@Test
	public void testMethod12And13() throws Exception {
		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
		// "Ralph;one-two-;10", "Ralph", new String[] { "one", "two" }, 10);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
				"Ralph;one-;11", "Ralph", new String[] { "one" }, 11);

		// ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
		// "Ralph;;12", "Ralph", new String[] {}, 12);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method12",
				"Ralph;;13", "Ralph", null, 13);

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;1+2+;20", "aStr", 20, new int[] { 1, 2 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;3+1+2+;21", "aStr", 21, new int[] { 3, 1, 2 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;3+;22", "aStr", 22, new int[] { 3 });

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;;23", "aStr", 23, new int[] {});

		ControllerUtil.sendAndReceive(mockMvc, "remoteProviderOptional", "method13",
				"aStr;;24", "aStr", 24, null);
	}

	@Test
	public void testMethod16() {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("intCookie", "1"));
		cookies.add(new Cookie("booleanCookie", "true"));
		ControllerUtil.sendAndReceive(mockMvc, null, cookies, "remoteProviderOptional",
				"method16", "1;true", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method16", "-1;false", (Object[]) null);
	}

	@Test
	public void testMethod17() {
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method17", "theDefaultValue", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("stringCookie", "str")),
				"remoteProviderOptional", "method17", "str", (Object[]) null);
	}

	@Test
	public void testMethod18() {
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("nameOfTheCookie", "cookieValue")),
				"remoteProviderOptional", "method18", "cookieValue", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method18", "default", (Object[]) null);
	}

	@Test
	public void testMethod19() {
		ControllerUtil.sendAndReceive(mockMvc, null,
				Collections.singletonList(new Cookie("stringCookie", "aString")),
				"remoteProviderOptional", "method19", "aString", (Object[]) null);
		ControllerUtil.sendAndReceive(mockMvc, null, null, "remoteProviderOptional",
				"method19", Void.TYPE, (Object[]) null);
	}

	@Test
	public void testPoll1a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "2");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll1", "opoll1", params, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll1");
		assertThat(resp.getData()).isEqualTo("Result: 2");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll1b() throws Exception {

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll1", "opoll1", null, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("exception");
		assertThat(resp.getName()).isEqualTo("opoll1");
		assertThat(resp.getData()).isNull();
		assertThat(resp.getMessage()).isEqualTo("Server Error");
		assertThat(resp.getWhere()).isNull();
	}

	@Test
	public void testPoll2a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "7");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll2", "opoll2", params, null);
		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll2");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(14));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll2b() throws Exception {
		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll2", "opoll2", null, null, null, true);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll2");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(4));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll3a() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("id", "3");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll3", "opoll3", params, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll3");
		assertThat(resp.getData()).isEqualTo(Integer.valueOf(6));
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll3b() throws Exception {
		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll3", "opoll3", null, null);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll3");
		assertThat(resp.getData()).isNull();
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll4() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("header", "headerValue");

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll4", "opoll4", null, headers);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll4");
		assertThat(resp.getData()).isEqualTo("100;dummy;headerValue");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

	@Test
	public void testPoll5() throws Exception {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(new Cookie("cookie", "cookieValue"));

		ExtDirectPollResponse resp = ControllerUtil.performPollRequest(mockMvc,
				"remoteProviderOptional", "opoll5", "opoll5", null, null, cookies);

		assertThat(resp).isNotNull();
		assertThat(resp.getType()).isEqualTo("event");
		assertThat(resp.getName()).isEqualTo("opoll5");
		assertThat(resp.getData()).isEqualTo("23;dummy;cookieValue");
		assertThat(resp.getWhere()).isNull();
		assertThat(resp.getMessage()).isNull();
	}

}
