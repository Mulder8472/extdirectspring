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
package ch.ralscha.extdirectspring.bean;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents the response of a Ext Direct call. Internal class
 */
@JsonInclude(Include.NON_NULL)
public class ExtDirectResponse extends BaseResponse {

	private int tid;

	private String action;

	private String method;

	private Object result;

	private boolean streamResponse;

	@JsonIgnore
	private Class<?> jsonView;

	public ExtDirectResponse() {
		// needs a default constructor for testing
	}

	public ExtDirectResponse(ExtDirectRequest directRequest) {
		action = directRequest.getAction();
		method = directRequest.getMethod();
		tid = directRequest.getTid();
		setType(directRequest.getType());
	}

	public ExtDirectResponse(HttpServletRequest request) {
		action = request.getParameter("extAction");
		method = request.getParameter("extMethod");
		tid = Integer.parseInt(request.getParameter("extTID"));
		setType(request.getParameter("extType"));
	}

	public String getAction() {
		return action;
	}

	public String getMethod() {
		return method;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getTid() {
		return tid;
	}

	@JsonIgnore
	public boolean isStreamResponse() {
		return streamResponse;
	}

	public void setStreamResponse(boolean streamResponse) {
		this.streamResponse = streamResponse;
	}

	public Class<?> getJsonView() {
		return jsonView;
	}

	public void setJsonView(Class<?> jsonView) {
		this.jsonView = jsonView;
	}

	@Override
	public String toString() {
		return "ExtDirectResponse [tid=" + tid + ", action=" + action + ", method="
				+ method + ", result=" + result + ", streamResponse=" + streamResponse
				+ ", jsonView=" + jsonView + "]";
	}

}
