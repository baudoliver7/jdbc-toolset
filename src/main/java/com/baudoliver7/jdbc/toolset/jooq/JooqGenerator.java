/*
 * MIT License
 *
 * Copyright (c) 2022 Olivier B. OURA
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.baudoliver7.jdbc.toolset.jooq;

import javax.sql.DataSource;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;

/**
 * JOOQ generator.
 *
 * @since 0.1
 */
public final class JooqGenerator {

    /**
     * Data source.
     */
    private final DataSource src;

    /**
     * Package where to generate classes.
     */
    private final String pkg;

    /**
     * Tables to include.
     * <p>
     *     To include all public tables, write: .*
     *     To include all public tables prefixed with ad, write: ad_(.*)
     * </p>
     */
    private final String inclusions;

    /**
     * Target directory.
     */
    private final String target;

    /**
     * Ctor.
     * @param src Data source
     * @param pkg Package where to generate classes
     * @param inclusions Database tables to include
     * @param target Target directory
     * @checkstyle ParameterNumberCheck (4 lines)
     */
    public JooqGenerator(
        final DataSource src, final String pkg,
        final String inclusions, final String target
    ) {
        this.src = src;
        this.pkg = pkg;
        this.inclusions = inclusions;
        this.target = target;
    }

    /**
     * Starts generation.
     *
     * @throws Exception If fails
     */
    public void start() throws Exception {
        final GenerationTool tool = new GenerationTool();
        tool.setDataSource(this.src);
        tool.run(
            new Configuration()
                .withGenerator(
                    new Generator()
                        .withDatabase(
                            new Database()
                                .withIncludes(this.inclusions)
                                .withExcludes("databasechangelog|databasechangeloglock")
                                .withInputSchema("public")
                        ).withTarget(
                            new Target()
                                .withPackageName(this.pkg)
                                .withDirectory(this.target)
                        )
                )
        );
    }
}