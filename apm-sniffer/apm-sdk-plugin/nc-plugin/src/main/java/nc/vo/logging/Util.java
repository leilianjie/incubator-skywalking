package nc.vo.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: 何冠宇
 * Date: 2004-11-10
 * Time: 13:14:54
 * <p/>
 * 一些工具方法的集合
 */
public class Util {

	/**
	 * 按照filename创建文件目录
	 *
	 * @param filename
	 */
	public static String makePath(String filename) {
		File dir;
		HashMap<String, String> varMap = new HashMap<String, String>();
		String server = System.getProperty("nc.server.name");

		if (server != null && !"".equals(server.trim())) {
			varMap.put("server", server);
		}

		filename = replaceVar(filename, varMap);
		filename = relateToServer(filename);
		if (System.getProperty("nc.server.startCount") != null) {
			int lastIndex = filename.lastIndexOf('.');
			String name = "";
			String ext = "";
			if (lastIndex > 0 && lastIndex < (filename.length() - 1)) {
				name = filename.substring(0, lastIndex);
				ext = filename.substring(lastIndex + 1);
			} else {
				name = filename;
			}

			int index1 = name.indexOf('[');
			int index2 = name.indexOf(']');

			if (index2 > index1) {
				name = name.substring(0, index1) + '['
						+ System.getProperty("nc.server.startCount") + ']';
			} else
				name = name + '[' + System.getProperty("nc.server.startCount")
						+ ']';

			if (ext != null) {
				filename = name + '.' + ext;
			} else
				filename = name;

		}

		dir = new File(filename).getParentFile();

		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (!success) {
				throw new LoggerException("Create log file directory failed:" + filename);
			}
		}
		return filename;
	}

	/**
	 * class, method, line,效率比较低，因此慎重使用.
	 * 该方法在容器环境下的调用推导是十分重要的
	 *
	 * @return
	 */
	public static String[] inferCaller(String fqcn) {
		String[] str = new String[3];
		CallerInfo callerInfo = new CallerInfo(new Throwable(), fqcn);
		str[0] = callerInfo.getClassName();
		str[1] = callerInfo.getMethodName();
		str[2] = callerInfo.getLineNumber();
		return str;
	}

	public static List<String> stringToList(String str, String delimer) {
		ArrayList<String> list = new ArrayList<String>();
		if (str != null && delimer != null) {
			StringTokenizer tokenizer = new StringTokenizer(str, delimer, false);
			while (tokenizer.hasMoreTokens()) {
				list.add(tokenizer.nextToken());
			}
		} else {
			if (delimer == null)
				list.add(str);
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 从系统中获取属性
	 *
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	public static String getSystemProperty(String propName, String defaultValue) {
		String retValue;
		try {
			retValue = System.getProperty(propName, defaultValue);
		} catch (SecurityException e) {
			retValue = defaultValue;
		}
		return retValue;
	}

	private static String relateTo(String home, String path) {

		if (".".equals(home) || "./".equals(home) || home == null
				|| path == null)
			return path;

		if (path.startsWith(home)) {
			return path;
		}

		if (path.startsWith("./")) {
			if (home.endsWith("/")) {
				return home + path.substring(2, path.length());
			} else {
				return home + "/" + path.substring(2, path.length());
			}

		} else
			return path;

	}

	private static String relateToServer(String path) {
		return relateTo(System.getProperty("nc.server.location", "."), path);
	}

	private static String replaceVar(String path, Map<String, String> map) {
		Iterator<String> itr = map.keySet().iterator();
		String newPath = path;

		String pattern = "\\$\\{.*\\}";

		while (itr.hasNext()) {
			String key = (String) itr.next();
			String tp = "\\$\\{" + key + "\\}";
			newPath = newPath.replaceAll(tp, (String) map.get(key));
		}

		newPath = newPath.replaceAll(pattern, "");
		newPath = newPath.replaceAll("//", "/");
		return newPath;

	}

}
