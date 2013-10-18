package org.ttumtd.connector;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple java client to connect to HBase cluster.
 */

public class SimpleHBaseClusterConnector {
    //private final String MASTER_IP = "10.138.168.185";
    private final String MASTER_IP = "clouderavm";
    private final String ZOOKEEPER_PORT = "2181";

    final String TRAFFIC_INFO_TABLE_NAME = "TrafficLog";
    final String TRAFFIC_INFO_COLUMN_FAMILY = "TimeStampIMSI";

    final String KEY_TRAFFIC_INFO_TABLE_BTS_ID = "BTS_ID";
    final String KEY_TRAFFIC_INFO_TABLE_DATE = "DATE";
    final String COLUMN_IMSI = "IMSI";
    final String COLUMN_TIMESTAMP = "TIME_STAMP";

    private final byte[] columnFamily = Bytes.toBytes(TRAFFIC_INFO_COLUMN_FAMILY);
    private final byte[] qualifier= Bytes.toBytes(COLUMN_IMSI);

    private Configuration conf = null;

    public SimpleHBaseClusterConnector ()
            throws MasterNotRunningException, ZooKeeperConnectionException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",MASTER_IP);
        conf.set("hbase.zookeeper.property.clientPort",ZOOKEEPER_PORT);
        //conf.set("hbase.master", "clouderavm:60000");

        HBaseAdmin.checkHBaseAvailable(conf);
    }

    /**
     * This filter will return list of IMSIs for a given btsId and ime interval
     * @param btsId : btsId for which the query has to run
     * @param startTime : start time for which the query has to run
     * @param endTime : end time for which the query has to run
     * @return returns IMSIs as set of Strings
     * @throws IOException
     */
    public Set<String> getInfoPerBTSID(String btsId, String date,
                                       String startTime, String endTime)
            throws IOException {
        Set<String> imsis = new HashSet<String>();

        //ToDo : better exception handling
        HTable table = new HTable(conf, TRAFFIC_INFO_TABLE_NAME);
        Scan scan = new Scan();

        scan.addColumn(columnFamily,qualifier);
        scan.setFilter(prepFilter(btsId, date, startTime, endTime));

        // filter to build where timestamp

        Result result = null;
        ResultScanner resultScanner = table.getScanner(scan);

        while ((result = resultScanner.next())!= null) {
            byte[] obtainedColumn = result.getValue(columnFamily,qualifier);
            imsis.add(Bytes.toString(obtainedColumn));
        }

        resultScanner.close();

        return imsis;
    }

    //ToDo : Figure out how valid is this filter code?? How comparison happens
    // with eqaul or grater than equal etc


    private Filter prepFilter (String btsId, String date,
                               String startTime, String endTime)
    {
        byte[] tableKey = Bytes.toBytes(KEY_TRAFFIC_INFO_TABLE_BTS_ID);
        byte[] timeStamp = Bytes.toBytes(COLUMN_TIMESTAMP);

        // filter to build -> where BTS_ID = <<btsId>> and Date = <<date>>
        RowFilter keyFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(btsId+date)));

        // filter to build -> where timeStamp >= startTime
        SingleColumnValueFilter singleColumnValueFilterStartTime =
                new SingleColumnValueFilter(columnFamily, timeStamp,
                        CompareFilter.CompareOp.GREATER_OR_EQUAL,Bytes.toBytes(startTime));

        // filter to build -> where timeStamp <= endTime
        SingleColumnValueFilter singleColumnValueFilterEndTime =
                new SingleColumnValueFilter(columnFamily, timeStamp,
                        CompareFilter.CompareOp.LESS_OR_EQUAL,Bytes.toBytes(endTime));

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Arrays
                .asList((Filter) keyFilter,
                        singleColumnValueFilterStartTime, singleColumnValueFilterEndTime));
        return filterList;
    }


    public static void main(String[] args) throws IOException {
        SimpleHBaseClusterConnector flt = new SimpleHBaseClusterConnector();
        Set<String> imsis= flt.getInfoPerBTSID("AMCD000784", "26082013","104092","104095");
        System.out.println(imsis.toString());
    }
}
