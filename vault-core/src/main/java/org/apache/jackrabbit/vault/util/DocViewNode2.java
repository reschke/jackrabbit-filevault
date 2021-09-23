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
package org.apache.jackrabbit.vault.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.jackrabbit.spi.Name;
import org.apache.jackrabbit.spi.commons.name.NameConstants;
import org.apache.jackrabbit.spi.commons.name.NameFactoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class that represents an immutable JCR node abstraction encapsulating multiple
 * {@link org.apache.jackrabbit.vault.util.DocViewProperty2 properties}.
 */
public class DocViewNode2 {

    private final @NotNull Name name;
    /** usually equal to {@link #name} except when this node has a same name sibling, in that case label has format {@code <name>[index]}, https://docs.adobe.com/content/docs/en/spec/jcr/2.0/22_Same-Name_Siblings.html#22.2%20Addressing%20Same-Name%20Siblings%20by%20Path */
    private final int index; // either 0 if no same-name sibling or 1 and above for SNS
    private final @NotNull Map<Name, DocViewProperty2> properties;

    public DocViewNode2(@NotNull Name name, @NotNull Collection<DocViewProperty2> properties) {
        this(name, 0, properties);
    }

    public DocViewNode2(@NotNull Name name, int index, @NotNull Collection<DocViewProperty2> properties) {
        this.name = name;
        if (index < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }
        this.index = index;
        this.properties = properties.stream().collect(Collectors.toMap(DocViewProperty2::getName, Function.identity()));
    }

    public @NotNull DocViewNode2 cloneWithDifferentProperties(@NotNull Collection<DocViewProperty2> properties) {
        return new DocViewNode2(name, index, properties);
    }

    public @NotNull Name getName() {
        return name;
    }

    /**
     * 
     * @return 0, except if there is a same-name sibling in the docview. In that case the index gives the order of the SNS nodes.
     */
    public int getIndex() {
        return index;
    }

    /**
     * 
     * @return the name suffixed by an index as outlined in https://docs.adobe.com/content/docs/en/spec/jcr/2.0/22_Same-Name_Siblings.html#22.2%20Addressing%20Same-Name%20Siblings%20by%20Path in case there is a same-name sibling, otherwise the same value as for {@link #getName()}.
     */
    public @NotNull Name getSnsAwareName() {
        if (index > 0) {
            return NameFactoryImpl.getInstance().create(name.getNamespaceURI(), name.getLocalName() + "[" + getIndex() + "]");
        } else {
            return name;
        }
    }

    public @NotNull Collection<DocViewProperty2> getProperties() {
        return Collections.unmodifiableCollection(properties.values());
    }

    public @NotNull Optional<DocViewProperty2> getProperty(Name name) {
        return Optional.ofNullable(properties.get(name));
    }

    public boolean hasProperty(Name name) {
        return properties.containsKey(name);
    }

    public @NotNull Collection<String> getPropertyValues(@NotNull Name name) {
        DocViewProperty2 prop = properties.get(name);
        return prop == null ? Collections.emptyList() : prop.getStringValues();
    }

    public @NotNull Optional<String> getPropertyValue(@NotNull Name name) {
        DocViewProperty2 prop = properties.get(name);
        if (prop == null) {
            return Optional.empty();
        } else {
            return prop.getStringValue();
        }
    }

    public @NotNull Optional<String> getPrimaryType() {
        return getPropertyValue(NameConstants.JCR_PRIMARYTYPE);
    }

    public @NotNull Collection<String> getMixinTypes() {
        return getPropertyValues(NameConstants.JCR_MIXINTYPES);
    }

    public @NotNull Optional<String> getIdentifier() {
        return getPropertyValue(NameConstants.JCR_UUID);
    }
}