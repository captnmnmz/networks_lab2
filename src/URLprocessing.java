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
		// Take into account that letters can be in lowercase or uppercase
		String _front = "<(?:a|A)(?:.*?)(?:[Hh][Rr][Ee][Ff])*=*";
		// Take everything that is between "" or ''
		String _address ="((\"([^>\"]*)\")|('([^>']*)'))";
		String _back = "(?:.*?)>";
		String match=null;
		String reg_exp = _front + _address+_back;

		Pattern pattern = Pattern.compile(reg_exp);
		Matcher matcher = pattern.matcher(data);
		int _find = 0
		while (matcher.find(_find)) {
            _find = matcher.end()
            if(matcher.group(3)==null){
                match =matcher.group(5)
            }
            if(matcher.group(5)==null){
            match =matcher.group(3)
            }
            try{
                if(new MyURL(match).getProtocol().equals("http")){
                    handler.takeUrl(match);
                }
            }catch(){




	}

}
