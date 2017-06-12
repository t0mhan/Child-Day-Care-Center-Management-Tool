package cdccm.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.Years;

public class CdccmUtilities {
	
	public static int getAge(String dateOfBirth) throws ParseException {
		//Returns the age by providing DOB.

		String[] dayMonthYear = dateOfBirth.split("-");
		if(dayMonthYear[0]==null || dayMonthYear[1]==null || dayMonthYear[2]==null)
		{
			return -1;
		}
		LocalDate birthdate = new LocalDate(Integer.parseInt(dayMonthYear[0]), Integer.parseInt(dayMonthYear[1]),
				Integer.parseInt(dayMonthYear[2])); // Birth year,month,date
		LocalDate now = new LocalDate(); // Today's date
		Years age = Years.yearsBetween(birthdate, now);
		return age.getYears();
	}
	
	public static boolean isValidFormat(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
           System.out.println("Wrong date format");
        }
        return date != null;
    }
}
