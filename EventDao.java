package dataAccess;

import infoObjects.EventResult;
import infoObjects.EventRequest;
import infoObjects.EventsRequest;
/**
 * Created by tyler on 2/10/2017.
 */

public class EventDao {

    public EventDao() {
    }

    /***
     * A method to get a specific event
     *
     * @PARAM userID, the ID for a specific user
     * @PARAM eventID, the ID for a specific event
     * @RETURN gets the result of attempting to get a specific event
     */
    EventResult getEvent(EventRequest request){
        return null;
    }

    /***
     * A method to get all of a user's ancestor's events
     *
     * @PARAM userID, the ID for a specific user
     * @RETURN gets the result of attempting to get all the users ancestor's events
     */
    EventResult getEvents(EventsRequest request){
        return null;
    }
}
