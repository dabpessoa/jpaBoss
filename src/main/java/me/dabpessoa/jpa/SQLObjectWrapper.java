package me.dabpessoa.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLObjectWrapper {

	public static <T> String wrap(Object value) {

		if (value == null) {
            return nullWrap();
		} else if (value instanceof String) {
			return stringWrap((String) value);
		} else if (value instanceof Number) {
			return numberWrap((Number) value);
		} else if (value instanceof Date) {
			return timestampWrap((Date) value);
		} else if (value instanceof Character) {
			return characterWrap((Character) value);
		} else if (value instanceof Boolean) {
			return booleanWrap((Boolean) value);
		}

		throw new RuntimeException("Unwrappable Object: "+value.getClass()+", value: "+value);

	}

	public static String dateWrap(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return String.format("to_date('%s', '%s')", sdf.format(date), "DD/MM/YYYY HH24:MI:SS");
	}

	public static String timestampWrap(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return String.format("to_timestamp('%s', '%s')", sdf.format(date), "DD/MM/YYYY HH24:MI:SS");
	}

	public static String numberWrap(Number number) {
		return number+"";
	}

	public static String booleanWrap(Boolean value) {
		return value.toString();
	}

	public static String stringWrap(String value) {
		if (value.indexOf("'") != -1) {
			value = value.replace("'", "''");
		}
		return "'"+value+"'";
	}

	public static String characterWrap(Character value) {
		return "'"+value+"'";
	}

	public static String nullWrap() {
		return null;
	}

}