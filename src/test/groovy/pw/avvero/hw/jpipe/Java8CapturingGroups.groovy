package pw.avvero.hw.jpipe

import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Matcher
import java.util.regex.Pattern

class Java8CapturingGroups extends Specification {

    @Unroll
    def "Capture single group"() {
        when:
        Pattern pattern = Pattern.compile(patternString)
        Matcher matcher = pattern.matcher(string)
        matcher.find()
        then:
        matcher.group("id") == id
        where:
        template            | patternString      | string    | id
        "id = <id>"         | "id = (?<id>\\w+)" | "id = 12" | "12"
        "foo id = <id> bar" | "id = (?<id>\\w+)" | "id = 12" | "12"
    }

    def "Example"() {
        when:
        String data = "Flight AA JFK.101.KRK[2016-12-06]";
        Pattern flightPattern = Pattern.compile("\\w+ (?<airline>..) "
                + "(?<origin>...)\\." + "(?<number>\\d+)\\." + "(?<destination>...)"
                + "\\[(?<deptDate>\\d+-\\d+-\\d+)\\]");
        Matcher flight = flightPattern.matcher(data);
        flight.find();
        System.out.println("Airline: " + flight.group("airline"));
        System.out.println("Origin: " + flight.group("origin"));
        System.out.println("Number: " + flight.group("number"));
        System.out.println("Destination: " + flight.group("destination"));
        System.out.println("Departure date: " + flight.group("deptDate"));
        then:
        1 == 1
    }

}
