/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.cache.internal;

import com.google.common.collect.Sets;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.gradle.util.GradleVersion;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;

import static org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter;

public class VersionSpecificCacheDirectoryService implements RecentlyUsedGradleVersions {

    private final File cacheBaseDir;

    public VersionSpecificCacheDirectoryService(File gradleUserHomeDirectory) {
        this.cacheBaseDir = new File(gradleUserHomeDirectory, DefaultCacheScopeMapping.GLOBAL_CACHE_DIR_NAME);
    }

    @Override
    public SortedSet<GradleVersion> getRecentlyUsedGradleVersions() {
        SortedSet<GradleVersion> result = Sets.newTreeSet();
        for (VersionSpecificCacheDirectory cacheDir : getExistingDirectories()) {
            result.add(cacheDir.getVersion());
        }
        return result;
    }

    public File getDirectory(GradleVersion gradleVersion) {
        return new File(cacheBaseDir, gradleVersion.getVersion());
    }

    public SortedSet<VersionSpecificCacheDirectory> getExistingDirectories() {
        SortedSet<VersionSpecificCacheDirectory> result = Sets.newTreeSet();
        for (File subDir : listVersionSpecificCacheDirs()) {
            GradleVersion version = tryParseGradleVersion(subDir);
            if (version != null) {
                result.add(new VersionSpecificCacheDirectory(subDir, version));
            }
        }
        return result;
    }

    private Collection<File> listVersionSpecificCacheDirs() {
        FileFilter combinedFilter = FileFilterUtils.and(directoryFileFilter(), new RegexFileFilter("^\\d.*"));
        File[] result = cacheBaseDir.listFiles(combinedFilter);
        return result == null ? Collections.<File>emptySet() : Arrays.asList(result);
    }

    private GradleVersion tryParseGradleVersion(File dir) {
        try {
            return GradleVersion.version(dir.getName());
        } catch (Exception e) {
            return null;
        }
    }
}
