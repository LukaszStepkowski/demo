import com.example.demo.model.PriceStatPerDay
import com.example.demo.service.EmailService;
import com.example.demo.service.FileService;
import com.example.demo.service.GoldService;
import com.example.demo.service.WeedGoldCombineFacade;
import spock.lang.Specification;
import spock.lang.Subject
import spock.lang.Unroll;

public class WeedGoldCombineFacadeSpec extends Specification {

    EmailService emailService = Mock();
    GoldService goldService = Mock();
    FileService statService = Mock();

    @Subject
    WeedGoldCombineFacade weedGoldCombineFacade = new WeedGoldCombineFacade(
            goldService, statService, emailService, currencyConverter, currencyRetriever
    )

    def 'should calculate weed price in gold'() {
        given:
            goldService.getGold(_ as String) >> new BigDecimal(2)
            statService.statistics() >>
                    ['2020-08-23': Optional.of(new PriceStatPerDay(lowQualityPrice: new BigDecimal(10)))]

        when:
            Map<String, BigDecimal> gold = weedGoldCombineFacade.weedForGold()

        then:
            gold ['2020-08-23'] == new BigDecimal(5)
    }

    def 'should not send email when gold price is 1'() {
        given:
        goldService.getGold(_ as String) >> BigDecimal.ONE
        statService.statistics() >> [:]
        when:
            weedGoldCombineFacade.weedForGold()

        then:
            0 * emailService.sendEmail()
    }

    @Unroll
    def 'should send email when gold price is #price'() {
        given:
            goldService.getGold(_ as String) >> BigDecimal.valueOf(price)
            statService.statistics() >> [:]
        when:
            weedGoldCombineFacade.weedForGold()

        then:
            1 * emailService.sendEmail()

        where:
            price << [0.1, 0.2, 0.5, 0.9]
    }

}
