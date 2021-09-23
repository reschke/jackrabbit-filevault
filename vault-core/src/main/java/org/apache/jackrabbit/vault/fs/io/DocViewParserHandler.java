/*************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ************************************************************************/
package org.apache.jackrabbit.vault.fs.io;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.spi.commons.namespace.NamespaceResolver;
import org.apache.jackrabbit.vault.util.DocViewNode2;
import org.xml.sax.SAXException;

public interface DocViewParserHandler {
    void startDocViewNode(String parentNodePath, DocViewNode2 docViewNode) throws IOException, RepositoryException;
    void endDocViewNode(String parentNodePath, DocViewNode2 docViewNode) throws IOException, RepositoryException; // TODO: how to access stack (i.e. 
    void endDocument() throws RepositoryException, IOException; // only called once
    default void startPrefixMapping(String prefix, String uri) {};
    default void endPrefixMapping(String prefix) {};
}
