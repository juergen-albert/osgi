/*
 * Copyright (c) OSGi Alliance (2001, 2013). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.w3c.dom.html;
public interface HTMLTextAreaElement extends org.w3c.dom.html.HTMLElement {
	void blur();
	void focus();
	java.lang.String getAccessKey();
	int getCols();
	java.lang.String getDefaultValue();
	boolean getDisabled();
	org.w3c.dom.html.HTMLFormElement getForm();
	java.lang.String getName();
	boolean getReadOnly();
	int getRows();
	int getTabIndex();
	java.lang.String getType();
	java.lang.String getValue();
	void select();
	void setAccessKey(java.lang.String var0);
	void setCols(int var0);
	void setDefaultValue(java.lang.String var0);
	void setDisabled(boolean var0);
	void setName(java.lang.String var0);
	void setReadOnly(boolean var0);
	void setRows(int var0);
	void setTabIndex(int var0);
	void setValue(java.lang.String var0);
}
