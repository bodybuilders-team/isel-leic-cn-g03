package server;

/**
 * Process the request.
 */
public class ProcessRequest {

    /**
     * Process the request.
     *
     * @param request Request to be processed.
     * @return Response to the request.
     * @throws InterruptedException If the thread is interrupted.
     */
    public static String process(String request) throws InterruptedException {
        System.out.println("Start processing request " + System.currentTimeMillis());
        String[] tmp = request.split(" ");
        String bigWord = "";
        for (String s : tmp) {
            if (s.length() > bigWord.length())
                bigWord = s;
        }

        // Simulate a long processing time
        Thread.sleep(10 * 1000);
        System.out.println("Processing complete: " + request + " *** Response: " + bigWord);
        System.out.println("End processing request " + System.currentTimeMillis());
        return bigWord;
    }

}
