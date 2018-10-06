package kr.ac.gachon.gtg.service;


import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.EventObject;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Service("EventObjectService")
public class EventObjectServiceImpl implements EventObjectService {
    @Override
    public EventObject courseToEventObject(Course course, Integer index) {
        return null;
    }

    // Course 결과 뽑은거 넣어주면 요령있게 현재 주차, 다음 주차 이런식으로 스케쥴 잡아서 fullcalender에서 4개를 보여주는 걸로 속이기
    // 강의 시간이 2일이상으로 나누어져 있으면 개수가 늘어나야 함;;;
    // 해당 사항을 반영하는 파싱이 필요...
    // 이벤트오브젝트에는 타이틀, 시간만 있으면 되는데...
    // 시간은 그 ISO 포멧에 맞게 작성해함.
    // start, end를 지정해주기
    // url을 통해서 click이벤트 처리가능
    // url에 그 수업정보를 보여주는 걸 할까? id에 과목코드 넣고 말이야...
    // index는 4개중 몇번째인지를 의미하는 것임. 그래야 1주차 2주차 3주차 4주차로 보여줄테니...
    @Override
    public ArrayList<EventObject> timetableToEventObjects(ArrayList<Course> timetable, Integer index) {
        ArrayList<EventObject> eventObjectList = new ArrayList<>();

        timetable.forEach(course -> {
            String[] timeList = course.getTime().replaceAll(" ", "").split(",");

            EventObject current = null;
            String start = "";
            String end = "";

            for (int i = 0; i < timeList.length; i++) {
                if (current == null) {
                    current = new EventObject();
                    current.setTitle(course.getTitle() + "-" + course.getInstructor());
                    start = timeList[i];
                }

                if (i == timeList.length - 1 || timeList[i].charAt(0) != timeList[i + 1].charAt(0)) {
                    end = timeList[i];

                    current.setStart(start);
                    current.setEnd(end);

                    eventObjectList.add(current);
                    current = null;
                }
            }
        });

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+9:00"));
        ZonedDateTime now = ZonedDateTime.now().plusWeeks(index);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        eventObjectList.forEach(eventObject -> {
            String start = eventObject.getStart();
            String end = eventObject.getEnd();

            eventObject.setStart(parseStartTime(now, today, start));
            eventObject.setEnd(parseEndTime(now, today, end));
        });

        return eventObjectList;
    }

    private String parseStartTime(ZonedDateTime now, int dayOfWeek, String start) {
        now = now.truncatedTo(ChronoUnit.DAYS);
        int days = 0;
        int hours = 0;
        int mins = 0;

        switch (start.charAt(0)) {
            case '월':
                days = Calendar.MONDAY - dayOfWeek;
                break;
            case '화':
                days = Calendar.TUESDAY - dayOfWeek;
                break;
            case '수':
                days = Calendar.WEDNESDAY - dayOfWeek;
                break;
            case '목':
                days = Calendar.THURSDAY - dayOfWeek;
                break;
            case '금':
                days = Calendar.FRIDAY - dayOfWeek;
                break;
        }

        String classTime = start.substring(1);

        // 50 minutes class
        if (classTime.matches("\\d+")) {
            int plus = Integer.parseInt(classTime);
            int base = 8;

            // night class
            if (plus >= 9) {
                hours = base + 9;
                mins = 30;
                mins += (55 * (plus - 9));
            } else {
                hours = base + plus;
            }
        } else {
            switch (classTime) {
                case "A":
                    hours = 9;
                    mins = 30;
                    break;
                case "B":
                    hours = 11;
                    mins = 0;
                    break;
                case "C":
                    hours = 12;
                    mins = 30;
                    break;
                case "D":
                    hours = 14;
                    mins = 0;
                    break;
                case "E":
                    hours = 15;
                    mins = 30;
            }
        }

        return now.plusDays(days)
                .plusHours(hours)
                .plusMinutes(mins)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String parseEndTime(ZonedDateTime today, int dayOfWeek, String end) {
        today = today.truncatedTo(ChronoUnit.DAYS);

        String start = parseStartTime(today, dayOfWeek, end);
        String classTime = end.substring(1);
        int mins = 0;

        // 50 minutes class
        if (classTime.matches("\\d+")) {
            mins = 50;
        } else {
            mins = 75;
        }

        return ZonedDateTime.parse(start + "+09:00")
                .plusMinutes(mins)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
