/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import com.mysql.jdbc.ReplicationDriver;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

/**
 *
 * @author marcie
 */
public class Main {

	public static void main(String[] args) throws SQLException, IOException {
		String db0Host = args.length > 0 ? args[0] : "db0";
		String db0Port = args.length > 1 ? args[1] : "3306";
		String db1Host = args.length > 2 ? args[2] : "db1";
		String db1Port = args.length > 3 ? args[3] : "3306";

		ReplicationDriver driver = new ReplicationDriver();
		Properties props = new Properties();
		props.put("autoReconnect", "true");
		props.put("failOverReadOnly", "false");
		props.put("user", "foo");
		props.put("password", "bar");
		props.put("replicationConnectionGroup", "rep_group");
		// props.put("replicationEnableJMX", "true");
		String connStr = String.format("jdbc:mysql:replication://%s:%s,%s:%s/db", db0Host, db0Port, db1Host, db1Port);
		System.out.println("connection string: " + connStr);
		System.out.println();
		try (Connection conn = driver.connect(connStr, props)) {
			conn.setAutoCommit(false);
			
			printTbl(conn);
			System.out.println();
			insertTbl(conn);
			System.out.println();
			printTbl(conn);
			System.out.println();

			System.out.println("kill db0 and press any key.");
			System.in.read();

			printTbl(conn);
			System.out.println();
			insertTbl(conn);
			System.out.println();
			printTbl(conn);
			System.out.println();

		}
	}

	private static void printTbl(Connection conn) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT id, created FROM tbl ORDER BY id");
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				System.out.println("id: " + rs.getInt("id"));
				System.out.println("name: " + rs.getDate("created"));
			}
		}
	}

	private static void insertTbl(Connection conn) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("INSERT INTO tbl (created) values (?)")) {
			ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			int n = ps.executeUpdate();
			conn.commit();
			System.out.printf("insert %d row(s).\n", n);
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		}
	}

}
