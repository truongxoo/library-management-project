package study.demo.enums;

public class Constants {
    public static final String  LOGIN_REGEX  = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    public static final String  PHONE_REGEX  = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    public static final String  LOCATION_FILE  = "D:\\LibraryCSV\\";
    public static final String  LOCATION_IMAGE  = "D:\\LibraryImage\\";
    public static final String FILE_TYPE = "csv";
    public static final String IMAGE_TYPE = "jpg,png";
}
