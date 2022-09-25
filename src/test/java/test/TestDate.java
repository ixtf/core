package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TestDate {
  public static void main(String[] args) throws ParseException {
    System.out.println(ZoneId.from(ZonedDateTime.now()));
    //    testBirthday();
    //    LocalDate
    //    LocalTime
    //    LocalDateTime
    //    OffsetDateTime;
    //    OffsetTime;
    //    ZonedDateTime;
  }

  private static void testBirthday() throws ParseException {
    final var patterStr = "yyyy-MM-dd";
    final var dateFormat = new SimpleDateFormat(patterStr);

    final var birthdayStr = "1988-09-11";
    // 字符串 -> Date -> 字符串
    var birthday = dateFormat.parse(birthdayStr);
    final var birthdayTimestamp = birthday.getTime();
    System.out.println("老王的生日是：" + birthday);
    System.out.println("老王的生日的时间戳是：" + birthdayTimestamp);

    System.out.println("==============程序经过一番周转，我的同时 方法入参传来了生日的时间戳=============");
    // 字符串 -> Date -> 时间戳 -> Date -> 字符串
    birthday = new Date(birthdayTimestamp);
    System.out.println("老王的生日是：" + birthday);
    System.out.println("老王的生日的时间戳是：" + dateFormat.format(birthday));
  }
}
