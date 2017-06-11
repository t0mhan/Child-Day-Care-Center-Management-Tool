package cdccm.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	public boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
	/*Instantiate this class in your code while entering email
	 * public static void main(String[] args)
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter Email");
		String str=sc.nextLine();
		EmailValidator emai=new EmailValidator();
		System.out.println(emai.validate(str));
	}*/
}
