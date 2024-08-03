/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.microreport;

import jakarta.json.bind.annotation.JsonbProperty;
import org.eclipse.microprofile.config.ConfigProvider;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericReportEntry {
    private static final int LARGE_CONTENT = ConfigProvider.getConfig().getOptionalValue("largeContentSize", Integer.class).orElse(500);

    private final Object wrapped;
    private final String idName;
    private final String groupName;

    public GenericReportEntry(Object wrapped, String idName, String groupName) {
        this.wrapped = wrapped;
        this.idName = idName;
        this.groupName = groupName;
    }

    public String getId() {
        Method propertyMethod = Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).filter(method -> method.getAnnotation(JsonbProperty.class).value().equals(idName)).findFirst().orElse(null);

        return getValue(propertyMethod);
    }

    public String getGroupName() {
        Method propertyMethod = Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).filter(method -> method.getAnnotation(JsonbProperty.class).value().equals(groupName)).findFirst().orElse(null);

        return getValue(propertyMethod);
    }

    public String getGroupNameLabel() {
        Method propertyMethod = Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).filter(method -> method.getAnnotation(JsonbProperty.class).value().equals(groupName)).findFirst().orElse(null);

        return propertyMethod.getAnnotation(JsonbProperty.class).value();
    }

    public String get(String columName) {
        Method propertyMethod = Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).filter(method -> method.getAnnotation(JsonbProperty.class).value().equals(columName)).findFirst().orElse(null);

        return getValue(propertyMethod);
    }

    public boolean isLarge(String columName) {
        Method propertyMethod = Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).filter(method -> method.getAnnotation(JsonbProperty.class).value().equals(columName)).findFirst().orElse(null);

        return getValue(propertyMethod).length() > LARGE_CONTENT;
    }

    private String getValue(Method propertyMethod) {
        try {
            return Optional.ofNullable(propertyMethod.invoke(wrapped)).map(Object::toString).orElse("--");
        } catch (Exception e) {
            return "??";
        }
    }

    public List<String> getSummaryColumns() {
        return Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Summary.class)).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).sorted(new Comparator<Method>() {
            @Override
            public int compare(Method m1, Method m2) {
                return Integer.compare(m1.getAnnotation(Summary.class).order(), m2.getAnnotation(Summary.class).order());
            }
        }).map(method -> method.getAnnotation(JsonbProperty.class).value()).collect(Collectors.toList());
    }

    public List<String> getDetailColumns() {
        return Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Detail.class)).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).sorted(new Comparator<Method>() {
            @Override
            public int compare(Method m1, Method m2) {
                return Integer.compare(m1.getAnnotation(Detail.class).order(), m2.getAnnotation(Detail.class).order());
            }
        }).map(method -> method.getAnnotation(JsonbProperty.class).value()).collect(Collectors.toList());
    }

    public List<String> getColumns() {
        return Arrays.stream(wrapped.getClass().getMethods()).filter(method -> method.isAnnotationPresent(JsonbProperty.class)).map(method -> method.getAnnotation(JsonbProperty.class).value()).collect(Collectors.toList());
    }
}
