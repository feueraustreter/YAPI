/**
 * Copyright 2019,2020 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package main;

import yapi.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LicenseSystem {

    private static List<String> changedFilesName = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        String directory = args[0].substring(0, args[0].lastIndexOf('/')) + "/src/main/java/";
        if (!new File(directory).exists()) {
            return;
        }
        if (!new File(directory).isDirectory()) {
            return;
        }
        File[] files = new File(directory).listFiles();
        List<File> directories = new ArrayList<>();
        long fileCount = 0;
        do {
            if (files == null) {
                break;
            }
            if (files.length == 0) {
                break;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    File[] fs = f.listFiles();
                    if (fs == null) {
                        continue;
                    }
                    if (fs.length == 0) {
                        continue;
                    }
                    directories.addAll(Arrays.stream(fs).collect(Collectors.toList()));
                } else {
                    if (!f.getAbsolutePath().endsWith(".java")) {
                        continue;
                    }
                    fileCount++;
                    try {
                        String[] strings = addLicense(FileUtils.fileContentAsString(f), f);
                        FileUtils.dump(f, strings);
                    } catch (IOException e) {
                        System.out.println("File Exception (IOException): " + f.getName());
                    }
                }
            }
            files = directories.toArray(new File[0]);
            directories.clear();
        } while (files.length != 0);

        System.out.println("> Stats: License");
        System.out.println("  > File Count:    " + fileCount);
        System.out.println("  > Changed Files: " + changedFiles);
        if (!changedFilesName.isEmpty()) {
            System.out.println("  - " + changedFilesName.stream().collect(Collectors.joining("\n  - ")));
        }
    }

    private static long changedFiles = 0;

    private static final String longLicense = "/**\n" +
            " * Copyright 2019,2020 yoyosource\n" +
            " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            " * you may not use this file except in compliance with the License.\n" +
            " * You may obtain a copy of the License at\n" +
            " * http://www.apache.org/licenses/LICENSE-2.0\n" +
            " * Unless required by applicable law or agreed to in writing, software\n" +
            " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " * See the License for the specific language governing permissions and\n" +
            " * limitations under the License.\n" +
            " */";
    private static final String shortLicense = "// SPDX-License-Identifier: Apache-2.0\n" +
            "// YAPI\n" +
            "// Copyright (C) 2019,2020 yoyosource";

    private static String[] addLicense(String[] strings, File f) {
        if (!f.getAbsolutePath().endsWith(".java")) {
            return strings;
        }
        StringBuilder st = new StringBuilder();
        int max = Math.min(12, strings.length);
        for (int i = 0; i < max; i++) {
            if (i != 0) {
                st.append('\n');
            }
            st.append(strings[i]);
        }

        if (st.toString().startsWith(shortLicense)) {
            return strings;
        }
        if (st.toString().startsWith(longLicense)) {
            return strings;
        }
        changedFiles++;
        changedFilesName.add(f.getName());

        String license = shortLicense;
        if (st.toString().startsWith("/**")) {
            license = longLicense;
        }

        st = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (st.length() != 0) {
                st.append('\n');
                st.append(strings[i]);
            } else if (strings[i].trim().startsWith("package")) {
                st.append(strings[i]);
            }
        }

        return (license + "\n\n" + st.toString()).split("\n");
    }

}