/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2025 Lenar Shamsutdinov
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
 *******************************************************************************/
package home.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.config.Config;
import home.model.ConnectionModel;
import home.model.VehicleModel;
import home.model.VehicleType;
import home.utils.ThreadUtils;

public final class Dao {

    private static final Logger LOG = LoggerFactory.getLogger(Dao.class);

    private static final int MIN_BATCH_SIZE = 3;
    private static final int MAX_BATCH_SIZE = 1_000;

    private static final String SELECT = "";

    private static final String INSERT = """
            INSERT INTO t_vehicle
            (c_type, c_color, c_number)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE = "";

    private static final String DELETE = "";

    // https://www.ibm.com/docs/en/db2woc?topic=messages-sqlstate
    // 08 - Connection Exception
    private static final String CONNECTION_ERROR_CODE = "08";

    private Dao() {
    }

    private static class SingletonHolder {
        private static final Dao INSTANCE = new Dao();
    }

    public static Dao getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void select(Set<String> soughtValues) {
        // TODO
        //
        // if soughtValues is empty -> select all
        //
        // Output values ​​twice
        // First for an exact match of all parameters
        // Second if there is at least one parameter match
    }

    public void insert(List<VehicleModel> dataObjs) {
        Collection<ConnectionModel> connections = Config.getCurrent().getConnectionsForCurrentMode();
        ThreadFactory factory = ThreadUtils.getVirtualThreadFactory("-> db write operation");

        boolean isBatch = dataObjs.size() >= MIN_BATCH_SIZE;

        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
            for (ConnectionModel connection : connections) {
                executor.submit(() -> {
                    if (isBatch) {
                        insertBatch(dataObjs, connection);
                    } else {
                        insertOneByOne(dataObjs, connection);
                    }
                });
            }
        }
    }

    private void insertBatch(List<VehicleModel> dataObjs, ConnectionModel connModel) {
        try (Connection conn = Connector.getConnection(connModel)) {
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setAutoCommit(false);

            int lastCommitedIdx = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
                int operationsCount = 0;
                for (int i = 0; i < dataObjs.size(); i++) {
                    VehicleModel dataObj = dataObjs.get(i);

                    pstmt.clearParameters();
                    fillStmtByDataFromObj(pstmt, dataObj);
                    pstmt.addBatch();
                    operationsCount++;

                    // Execute every BATCH_SIZE items.
                    if (operationsCount % MAX_BATCH_SIZE == 0 || operationsCount == dataObjs.size()) {
                        checkBatchExecution(pstmt.executeBatch());
                        conn.commit();

                        lastCommitedIdx++;
                    }
                }
            } catch (SQLException e) {
                checkConnectionState(e);
                rollbackAndLog(conn, e);

                List<VehicleModel> remainingData = dataObjs.subList(lastCommitedIdx, dataObjs.size());
                LOG.warn("Batch failed at index {}. The remaining items {} will be processed sequentially.",
                        lastCommitedIdx, remainingData.size());
                insertOneByOne(remainingData, conn);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throwDatabaseError("Error writing to database", e);
        }
    }

    private void insertOneByOne(List<VehicleModel> dataObjs, Connection conn)
            throws SQLException {
        conn.setAutoCommit(true);
        try (PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            for (VehicleModel dataObj : dataObjs) {
                fillStmtByDataFromObj(pstmt, dataObj);
                pstmt.execute();
            }
        }
    }

    private void insertOneByOne(List<VehicleModel> dataObjs, ConnectionModel connModel) {
        try (Connection conn = Connector.getConnection(connModel);
                PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            for (VehicleModel dataObj : dataObjs) {
                fillStmtByDataFromObj(pstmt, dataObj);
                pstmt.execute();
            }
        } catch (SQLException e) {
            throwDatabaseError("Error writing to database", e);
        }
    }

    public void update(List<VehicleModel> dataObjs) {
        // TODO
    }

    public void delete(List<VehicleModel> dataObjs) {
        // TODO
    }

    private void fillStmtByDataFromObj(PreparedStatement pstmt, VehicleModel dataObj)
            throws SQLException {
        VehicleType dataObjType = dataObj.getType();

        pstmt.setString(1, dataObjType.getType());
        pstmt.setString(2, dataObj.getColor());
        pstmt.setString(3, dataObj.getNumber());
    }

    private void checkBatchExecution(int[] batchResults) throws SQLException {
        if (batchResults == null) {
            LOG.warn("Batch execution result is null!");
            return;
        }

        for (int batchResult : batchResults) {
            if (batchResult >= 0 || Statement.SUCCESS_NO_INFO == batchResult) {
                // Everything is fine.
                continue;
            }

            var msg = new StringBuilder();
            msg
            .append("Batch execution error:\n")
            .append("\n")
            .append("When executing the batch, ");

            if (Statement.EXECUTE_FAILED == batchResult) {
                msg.append("result code 'EXECUTE_FAILED' was received.");
                throw new SQLException(msg.toString());
            }

            msg
            .append("unknown result code '")
            .append(batchResult)
            .append("' was received.");
            throw new SQLException(msg.toString());
        }
    }

    private void checkConnectionState(SQLException e) throws SQLException {
        String sqlState = e.getSQLState();
        if (sqlState.startsWith(CONNECTION_ERROR_CODE)) {
            LOG.error("Connection error (code %s)".formatted(sqlState), e);
            throw e;
        }
    }

    private void rollbackAndLog(Connection conn, Exception originalException) {
        LOG.error(originalException.getMessage());
        try {
            conn.rollback();
            LOG.info("Data succesfully rolled back");
        } catch (SQLException rollbackException) {
            rollbackException.addSuppressed(originalException);
            throwDatabaseError("rolling back failed", rollbackException);
        }
    }

    private void throwDatabaseError(String msg, SQLException e) {
        throw new IllegalStateException(msg, e);
    }
}
