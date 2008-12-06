/*
 * Copyright 2008 the original author or authors.
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
package org.gradle.api.internal;

import org.gradle.api.plugins.Convention;

import java.util.Map;

import groovy.lang.MissingPropertyException;
import groovy.lang.MissingMethodException;

public interface DynamicObject {
    boolean hasProperty(String name);

    Object property(String name) throws MissingPropertyException;

    void setProperty(String name, Object value) throws MissingPropertyException;

    Map<String, Object> properties();

    boolean hasMethod(String name, Object... params);

    Object invokeMethod(String name, Object... params) throws MissingMethodException;
}
