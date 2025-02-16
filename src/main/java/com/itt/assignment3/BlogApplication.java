package com.itt.assignment3;

import com.itt.assignment3.constant.Constants;
import com.itt.assignment3.dto.TumblrApiResponse;
import com.itt.assignment3.service.TumblrApiService;
import com.itt.assignment3.util.Parser;

import java.util.Optional;
import java.util.Scanner;

public class BlogApplication {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            TumblrApiService service = new TumblrApiService();
            Parser parser = new Parser();

            final String apiUrl = buildApiURL(scanner);
            final Optional<String> jsonResponse = service.getResponse(apiUrl);

            if (jsonResponse.isPresent()) {
                TumblrApiResponse response = parser.parse(jsonResponse.get());
                response.logToConsole();
            } else {
                System.err.println("No Response from the API");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String buildApiURL(Scanner scanner) {
        System.out.print("Enter the Tumblr blog name: ");
        String blogName = scanner.next();

        System.out.print("Enter the range: ");
        String[] range = scanner.next().split("-");

        try {
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            return String.format(Constants.REFERENCE_API_URL, blogName, (end - start + 1), start - 1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}
