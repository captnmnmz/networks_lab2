import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLprocessing {

	public interface URLhandler {
		void takeUrl(String url);
	}

	public static URLhandler handler = new URLhandler() {
		public void takeUrl(String url) {
			System.out.println(url);
		}
	};

	/**
	 * Parse the given buffer to fetch embedded links and call the handler to
	 * process these links.
	 * 
	 * @param data
	 *          the buffer containing the http document, here Charbuffer.wrap(buffer,...)
	 */
	public static void parseDocument(CharSequence data) {
		// call handler.takeUrl for each matched url

		String _front = "<A*href=";
		String match=null;
		String reg_exp = _front;

		Pattern pattern = Pattern.compile(reg_exp);
		Matcher matcher = pattern.matcher(data);
		
		if (matcher.find()) {
			match=matcher.group();
		}
		
		handler.takeUrl(match);



	}

}
