package pt.isel.cn.temporary_occupation;

import java.util.Date;
import java.util.Map;

/**
 * Event.
 */
public class Event {
    public int eventId;
    public String name;
    public String type;
    public Date startDate;
    public Date endDate;
    public Licensing licensing;
    public Map<String, String> details;

    public Event() {
        // Empty constructor
    }
}
