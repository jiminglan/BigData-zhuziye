

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKsegment {

	public static ArrayList<String> IKAnalysis(String str) throws Exception {
		//StringBuffer sb = new StringBuffer();
		ArrayList<String> aList = new ArrayList<String>();
		try {
			// InputStream in = new FileInputStream(str);//
			byte[] bt = str.getBytes();// str
			InputStream ip = new ByteArrayInputStream(bt);
			Reader read = new InputStreamReader(ip);
			IKSegmenter iks = new IKSegmenter(read, true);
			Lexeme t;
			while ((t = iks.next()) != null) {
				aList.add(t.getLexemeText());

			}
			//sb.delete(sb.length() - 1, sb.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aList;
	}

}