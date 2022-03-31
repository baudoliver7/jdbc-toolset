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

import com.baudoliver7.jdbc.toolset.wrapper.DataSourceWrap;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Data source that is locked to one connection per thread.
 * <p>This connection automatically opens a transaction.
 * You have to commit or rollback via connection stored in the local thread before
 * passing to another one for the same thread.</p>
 *
 * @since 0.1
 */
public final class LocalLockedDataSource extends DataSourceWrap {

    /**
     * Thread connection.
     */
    private final ThreadLocal<Connection> connection;

    /**
     * Ctor.
     * @param origin Data source to wrap
     * @param connection Thread connection
     */
    public LocalLockedDataSource(
        final DataSource origin, final ThreadLocal<Connection> connection
    ) {
        super(origin);
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        final Connection conn;
        if (this.connection.get() == null) {
            conn = this.newConnection();
        } else {
            if (this.connection.get().isClosed()) {
                conn = this.newConnection();
            } else {
                conn = this.connection.get();
            }
        }
        return new LockedConnection(conn);
    }

    @Override
    public Connection getConnection(
        final String username, final String password
    ) throws SQLException {
        final Connection conn;
        if (this.connection.get() == null) {
            conn = this.newConnection(username, password);
        } else {
            if (this.connection.get().isClosed()) {
                conn = this.newConnection(username, password);
            } else {
                conn = this.connection.get();
            }
        }
        return new LockedConnection(conn);
    }

    /**
     * Generate new connection.
     * @return Connection
     * @throws SQLException If fails
     */
    private Connection newConnection() throws SQLException {
        synchronized (this.connection) {
            final Connection connect =  super.getConnection();
            connect.setAutoCommit(false);
            connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            this.connection.set(connect);
            return connect;
        }
    }

    /**
     * Generate new connection.
     * @param username Username
     * @param password Password
     * @return Connection
     * @throws SQLException If fails
     */
    private Connection newConnection(
        final String username, final String password
    ) throws SQLException {
        synchronized (this.connection) {
            final Connection connect =  super.getConnection(username, password);
            connect.setAutoCommit(false);
            connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            this.connection.set(connect);
            return connect;
        }
    }
}
