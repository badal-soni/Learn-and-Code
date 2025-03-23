package com.itt.assignment3;

import com.itt.assignment3.constant.Constants;
import com.itt.assignment3.constant.ErrorMessage;
import com.itt.assignment3.dto.TumblrApiResponse;
import com.itt.assignment3.util.ConsoleLogger;
import com.itt.assignment3.service.TumblrApiService;
import com.itt.assignment3.util.Parser;
import com.itt.assignment3.validation.Validator;

import java.util.Optional;
import java.util.Scanner;

public class BlogApplication {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            TumblrApiService service = new TumblrApiService();
            Parser parser = new Parser();

            final String apiUrl = buildApiURL(scanner);
            final Optional<String> jsonResponse = service.getResponse(apiUrl);

            jsonResponse.ifPresentOrElse(responseBody -> {
                TumblrApiResponse response = parser.parse(responseBody);
                ConsoleLogger.logTumblrApiResponse(response);
            }, () -> {
                System.err.println(ErrorMessage.NO_RESPONSE_ERROR_MESSAGE);
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String buildApiURL(Scanner scanner) {
        System.out.print("Enter the Tumblr blog name: ");
        String blogName = scanner.next();

        System.out.print("Enter the range: ");
        String[] rangeArray = scanner.next().split("-");

        Validator.validateRange(rangeArray);

        int start = Integer.parseInt(rangeArray[0]);
        int end = Integer.parseInt(rangeArray[1]);
        final int range = end - start + 1;

        return String.format(Constants.REFERENCE_API_URL, blogName, range, start - 1);

    }

}
