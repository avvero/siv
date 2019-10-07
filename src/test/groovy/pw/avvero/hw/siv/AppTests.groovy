package pw.avvero.hw.siv

import spock.lang.Specification

class AppTests extends Specification {

    def "Run app"() {
        when:
        App.main()
        then:
        thrown(IllegalArgumentException)
    }

}
