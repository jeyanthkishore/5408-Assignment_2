
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSetMetaData;

public class ConvertResultSetToHashMap {

	public List<HashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException
	{
		ResultSetMetaData md = rs.getMetaData();
		int columnNumber = md.getColumnCount();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		while (rs.next())
		{
			HashMap<String, Object> row = new HashMap<String, Object>(columnNumber);
			for(int i=1; i<=columnNumber; ++i){     
				row.put(md.getColumnName(i),rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

}
