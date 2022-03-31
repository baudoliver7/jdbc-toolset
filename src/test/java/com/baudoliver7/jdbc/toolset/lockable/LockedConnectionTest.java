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
package com.baudoliver7.jdbc.toolset.lockable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link LockedConnection}.
 *
 * @since 0.1
 */
final class LockedConnectionTest {

    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Test
    void attemptsToClose() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection("jdbc:h2:~/test1", "sa1", "pwd1")
        ) {
            new LockedConnection(conn).close();
            MatcherAssert.assertThat(
                conn.isClosed(),
                Matchers.is(false)
            );
        }
    }
}
