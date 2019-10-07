package pw.avvero.hw.jpipe

import spock.lang.Specification

class AppTests extends Specification {

    def "Run app"() {
        when:
        App.main()
        then:
        thrown(IllegalArgumentException)
    }

}
